package com.antsyferov.astronerd.ui.panes

data class AsteroidsListState(
    val loading: LoadingState = LoadingState.Initial,
    val filters: List<Filter> = emptyList(),
    val range: ClosedFloatingPointRange<Float> = 0f.rangeTo(0f)
)

sealed class LoadingState {
    data object Initial : LoadingState()
    data object Network : LoadingState()
    data object Done : LoadingState()
    data object Error : LoadingState()
}

sealed class Filter {
    data class Dangerous(val isDangerous: Boolean): Filter()
    data class Name(val name: String): Filter()
    data class Orbiting(val body: String): Filter()
    data class Date(val date: String): Filter()
    data class Diameter(val range: ClosedFloatingPointRange<Float>): Filter()
}
