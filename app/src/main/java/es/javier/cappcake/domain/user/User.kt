package es.javier.cappcake.domain.user

import android.graphics.Bitmap

data class User(val userId: String,
                val username: String,
                val email: String,
                val profileImage: String? = null,
                val posts: Int,
                val followers: Int,
                val following: Int)
