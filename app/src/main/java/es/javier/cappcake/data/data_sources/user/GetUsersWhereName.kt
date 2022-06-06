package es.javier.cappcake.data.data_sources.user

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetUsersWhereName @Inject constructor() {

    private val firestore = Firebase.firestore

    /**
     * Method to get a list of users whose has the username passed
     *
     * @param username The username of the users to search
     * @return The response with the list of users that matches that username
     */
    suspend fun getUsersWhereName(username: String) : Response<List<User>> {

        val query = firestore
            .collection(FirebaseContracts.USER_COLLECTION)
            .whereEqualTo(FirebaseContracts.USER_NAME, username)
            .get()

        return suspendCoroutine { continuation ->
            query.addOnCompleteListener { task ->

                val result = task.result.map { document ->
                    val userId = document.id
                    val documentUsername = document.getString(FirebaseContracts.USER_NAME) ?: "Username"
                    val documentEmail = document.getString(FirebaseContracts.USER_EMAIL) ?: "Username"
                    val profileImage = document.getString(FirebaseContracts.USER_PROFILE_IMAGE)
                    val posts = document.getLong(FirebaseContracts.USER_POSTS)?.toInt() ?: 0
                    val following = document.getLong(FirebaseContracts.USER_FOLLOWING)?.toInt() ?: 0

                    return@map User(
                        userId,
                        documentUsername,
                        documentEmail,
                        profileImage,
                        posts,
                        following
                    )
                }

                if (task.isSuccessful) {
                    if (result.isNotEmpty()) {
                        continuation.resume(Response.Success(data =result))
                    } else {
                        continuation.resume(Response.Failiure(data = emptyList(), throwable = task.exception))
                    }
                } else {
                    continuation.resume(Response.Failiure(data = emptyList(), throwable = null))
                }
            }
        }

    }

}