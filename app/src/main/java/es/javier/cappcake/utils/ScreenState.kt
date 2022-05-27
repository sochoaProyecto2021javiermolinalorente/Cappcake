package es.javier.cappcake.utils

sealed class ScreenState {
    object LoadingData : ScreenState()
    object DataLoaded: ScreenState()
}