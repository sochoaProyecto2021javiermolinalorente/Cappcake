package es.javier.cappcake.presentation.editprofilescreen

sealed class EditProfileScreenState {
    object LoadBaseDate : EditProfileScreenState()
    object BaseData : EditProfileScreenState()
    object DataChanged : EditProfileScreenState()
    object Updating : EditProfileScreenState()
    object Updated : EditProfileScreenState()
}