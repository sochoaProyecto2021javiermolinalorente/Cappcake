package es.javier.cappcake.data.data_sources.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetFollowedUsers @Inject constructor() {

    private val firestore = Firebase.firestore
    private val storage = Firebase.storage
    private val auth = Firebase.auth

    suspend fun getFollowedUsers() : Response<List<User>> {

        val query = firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION)

        return suspendCoroutine { continuation ->

            query.whereArrayContains(FirebaseContracts.FOLLOWERS_USERS, auth.uid!!)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val users = task.result.map { document ->
                            val userId = document.getString(FirebaseContracts.FOLLOWERS_USER_ID)
                            val profileImagePath = document.getString(FirebaseContracts.FOLLOWERS_PROFILE_IMAGE)
                            return@map User(
                                userId!!,
                                "",
                                "",
                                profileImagePath,
                                0,
                                0
                            )
                        }

                        continuation.resume(Response.Success(data = users))

                    } else {
                        continuation.resume(Response.Failiure(data = emptyList(), message = task.exception?.message))
                    }
            }
        }
    }


}