package com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.filter
import com.antsyferov.impl.local.AsteroidEntity
import com.antsyferov.impl.local.AsteroidsDao
import com.antsyferov.impl.local.RemoteKey
import com.antsyferov.impl.local.RemoteKeyDao
import com.antsyferov.impl.mappers.toEntities
import com.antsyferov.impl.network.AsteroidsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AsteroidsListViewModel @Inject constructor(
    private val asteroidsDao: AsteroidsDao,
    private val asteroidsApi: AsteroidsApi,
    private val remoteKeyDao: RemoteKeyDao,
) : ViewModel() {

    private var pagingSource: PagingSource<Int, AsteroidEntity>? = null
    private var loadingJob: Job? = null

    private val _stateFlow: MutableStateFlow<AsteroidsListState> = MutableStateFlow(
        AsteroidsListState()
    )
    val stateFlow: StateFlow<AsteroidsListState> = _stateFlow.asStateFlow()
    val asteroidsFlow = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 20,
            initialLoadSize = 20
        )
    ) { asteroidsDao.pagingSource().also { pagingSource = it } }
        .flow
        .cachedIn(viewModelScope)
        .combine(stateFlow) { pagingData, state ->
            pagingData.filter { filter(it, state.filters) }
        }
        .onEach { _ ->
            _stateFlow.update { it.copy(loading = LoadingState.Done) }
        }
        .flowOn(Dispatchers.IO)

    init {

        viewModelScope.launch {
            remoteKeyDao.getFirstKey()?.let { key ->
                val daysSinceLastUpdate = TimeUnit.MILLISECONDS.toDays(Date().time - key.timestamp)
                if (daysSinceLastUpdate >= 1) {
                    initLoad()
                }
            } ?: initLoad()
        }

        asteroidsDao
            .getMaxMinDiameter()
            .onEach { diameter ->
                _stateFlow.update { it.copy(range = diameter.toRange()) }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun initLoad() {
        loadingJob?.cancel()
        remoteKeyDao.clearAll()
        asteroidsDao.clearAll()
        _stateFlow.update { it.copy(loading = LoadingState.Initial) }
        loadNextPage()
    }

    fun loadNextPage() {
        loadingJob = viewModelScope.launch {
            try {
                if (_stateFlow.value.loading != LoadingState.Network) {
                    _stateFlow.update { it.copy(loading = LoadingState.Network) }
                    val key = getLastKey()
                    val asteroids = asteroidsApi.getAsteroids(key.nextStart, key.nextEnd)
                    asteroidsDao.insertAll(asteroids.toEntities())
                    val uri = Uri.parse(asteroids.links.previous)
                    val start = uri.getQueryParameters("start_date").first()
                    val end = uri.getQueryParameters("end_date").first()
                    remoteKeyDao.insert(RemoteKey(nextStart = start, nextEnd = end, timestamp = Date().time))
                    pagingSource?.invalidate()
                    _stateFlow.update { it.copy(loading = LoadingState.Done) }
                }
            } catch (e: Exception) {
                _stateFlow.update { it.copy(loading = LoadingState.Error) }
            }
        }
    }

    fun addFilter(filter: Filter) {
        var filters = _stateFlow.value.filters
        filters.find { it::class.java == filter::class.java }?.let {
            filters = filters - it
        }
        if (filter is Filter.Name && filter.name.isBlank() ||
            filter is Filter.Orbiting && filter.body.isBlank()) {
            _stateFlow.update { it.copy(filters = filters) }
        } else {
            _stateFlow.update { it.copy(filters = filters + filter) }
        }

    }

    fun removeFilter(filter: Filter) {
        _stateFlow.update { it.copy(filters =  it.filters - filter) }
    }

    private suspend fun getLastKey(): RemoteKey {
        val key = remoteKeyDao.getLastKey()
        return if (key != null) {
            key
        } else {
            val (start, end) = getDateRange()
            RemoteKey(nextStart = start, nextEnd = end, timestamp = Date().time)
        }
    }

    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    private fun getDateRange(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val endDate = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.time

        return Pair(formatter.format(startDate), formatter.format(endDate))
    }

    private fun filter(asteroid: AsteroidEntity, filters: List<Filter>): Boolean {
        for (filter in filters) {
            when(filter) {
                is Filter.Dangerous -> {
                    if (asteroid.isPotentiallyHazardousAsteroid != filter.isDangerous) return false
                }
                is Filter.Name -> {
                    if (!asteroid.name.contains(filter.name, true)) return false
                }
                is Filter.Orbiting -> {
                    if (asteroid.closeApproachData?.orbitingBody?.contains(filter.body, true) != true) return false
                }
                is Filter.Date -> {
                    if (asteroid.closeApproachData?.closeApproachDate?.equals(filter.date) != true) return false
                }
                is Filter.Diameter -> {
                    if (!(filter.range.contains(asteroid.estimatedDiameter.kilometers.min) ||
                        filter.range.contains(asteroid.estimatedDiameter.kilometers.min))) return false
                }
            }
        }
        return true
    }

}