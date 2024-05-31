package com.antsyferov.impl.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.antsyferov.impl.local.AppDatabase
import com.antsyferov.impl.local.AsteroidEntity
import com.antsyferov.impl.local.AsteroidsDao
import com.antsyferov.impl.mappers.toEntities
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

