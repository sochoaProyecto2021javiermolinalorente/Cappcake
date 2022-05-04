package es.javier.cappcake.domain

import java.lang.Exception

sealed class Response<T>(val message: String? = null, val data: T? = null) {
    class Success<T>(data: T) : Response<T>(data = data)
    class Failiure<T>(message: String?, data: T? = null) : Response<T>(message, data)
}