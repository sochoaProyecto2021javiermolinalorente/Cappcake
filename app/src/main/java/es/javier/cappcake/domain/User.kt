package es.javier.cappcake.domain

import android.graphics.Bitmap

data class User(val userId: String,
                val username: String,
                val email: String,
                val profileImage: String? = null)
