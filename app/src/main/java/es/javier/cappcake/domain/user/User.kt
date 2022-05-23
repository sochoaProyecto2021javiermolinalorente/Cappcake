package es.javier.cappcake.domain.user

data class User(val userId: String,
                val username: String,
                val email: String,
                val profileImage: String? = null,
                val posts: Int,
                val following: Int)
