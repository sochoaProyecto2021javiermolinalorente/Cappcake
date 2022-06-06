package es.javier.cappcake.utils

sealed class UsernameFieldError {
    object NoError : UsernameFieldError()
    object UserExistsError: UsernameFieldError()
    object UsernameWithWhiteSpaces: UsernameFieldError()
}