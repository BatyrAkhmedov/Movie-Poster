package kz.batyr.movieposters.domain

sealed class State (
    val isLoading: Boolean
        ){
    object Loading : State (true)
    object Success : State (false)
}
