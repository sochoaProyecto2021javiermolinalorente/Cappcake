package es.javier.cappcake.data.data_sources

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetProfile @Inject constructor() {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    suspend fun getUserProfile(uid: String) : Response<User?> {

        val userRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(uid)

        val followers = getFollowers(userDocRef = userRef)

        return suspendCoroutine {  continuation ->
            userRef.get(Source.SERVER).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (task.result.exists()) {
                        continuation.resume(
                            Response.Success(data = User(
                            userId = task.result.id,
                            username = task.result.getString(FirebaseContracts.USER_NAME) ?: FirebaseContracts.UNKNOWN,
                            email = task.result.getString(FirebaseContracts.USER_EMAIL) ?: FirebaseContracts.UNKNOWN,
                            profileImage = task.result.getString(FirebaseContracts.USER_PROFILE_IMAGE),
                            posts = task.result.getLong(FirebaseContracts.USER_POSTS)?.toInt() ?: FirebaseContracts.NUMBER_UNKNOWN,
                            followers = followers,
                            following = task.result.getLong(FirebaseContracts.USER_FOLLOWING)?.toInt() ?: FirebaseContracts.NUMBER_UNKNOWN)
                        ))
                    } else {
                        continuation.resume(Response.Failiure(data = null, message = null))
                    }

                } else {
                    continuation.resume(Response.Failiure(data = null, message = null))
                }
            }
        }
    }

    private suspend fun getFollowers(userDocRef: DocumentReference) : Int {
        val userFollowersRef = userDocRef.collection(FirebaseContracts.USER_FOLLOWERS_COUNTER_COLLECTION)
        return suspendCoroutine { continuation ->
            userFollowersRef.get(Source.DEFAULT).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val counters = task.result.documents.map {
                        it.getLong(FirebaseContracts.USER_FOLLOWERS_COUNTER)?.toInt() ?: 0
                    }
                    var count = 0
                    counters.forEach { count += it }
                    continuation.resume(count)
                } else {
                    continuation.resume(0)
                }
            }
        }
    }

}