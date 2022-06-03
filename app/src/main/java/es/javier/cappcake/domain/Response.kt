package es.javier.cappcake.domain

sealed class Response<T>(val throwable: Throwable? = null, val data: T? = null) {
    class Success<T>(data: T) : Response<T>(data = data)
    class Failiure<T>(throwable: Throwable?, data: T? = null) : Response<T>(throwable, data)
}