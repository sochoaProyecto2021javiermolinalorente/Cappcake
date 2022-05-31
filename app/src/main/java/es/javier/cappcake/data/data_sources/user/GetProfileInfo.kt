package es.javier.cappcake.data.data_sources.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.user.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetProfileInfo @Inject constructor() {

    private val auth = Firebase.auth
    private val firestore = Firebase.firestore

    suspend fun getUserProfile(uid: String) : Response<Pair<User, Boolean>?> {

        val userRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(uid)

        return suspendCoroutine { continuation ->
            firestore.runTransaction { transaction ->

                val userDoc = transaction.get(userRef)
                val userId = userDoc.id
                val username = userDoc.getString(FirebaseContracts.USER_NAME) ?: FirebaseContracts.UNKNOWN
                val email = userDoc.getString(FirebaseContracts.USER_EMAIL) ?: FirebaseContracts.UNKNOWN
                val profileImage = userDoc.getString(FirebaseContracts.USER_PROFILE_IMAGE)
                val posts = userDoc.getLong(FirebaseContracts.USER_POSTS)?.toInt() ?: FirebaseContracts.NUMBER_UNKNOWN
                val following = userDoc.getLong(FirebaseContracts.USER_FOLLOWING)?.toInt() ?: FirebaseContracts.NUMBER_UNKNOWN

                val userFollowers = transaction.get(firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION).document(username))
                val followers = userFollowers[FirebaseContracts.FOLLOWERS_USERS] as ArrayList<*>

                val isFollowing = followers.contains(auth.uid!!)

                Pair(User(userId, username, email, profileImage, posts, following), isFollowing)

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = task.result))
                } else {
                    continuation.resume(Response.Failiure(data = null, message = null))
                }
            }
        }
    }

    suspend fun getFollowersCount(uid: String) : Response<Int?> {
        val userFollowersRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(uid).collection(FirebaseContracts.USER_FOLLOWERS_COUNTER_COLLECTION)
        return suspendCoroutine { continuation ->
            userFollowersRef.get(Source.DEFAULT).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val counters = task.result.documents.map {
                        it.getLong(FirebaseContracts.USER_FOLLOWERS_COUNTER)?.toInt() ?: 0
                    }
                    var count = 0
                    counters.forEach { count += it }
                    continuation.resume(Response.Success(data = count))
                } else {
                    continuation.resume(Response.Failiure(data = null, message = task.exception?.message))
                }
            }
        }
    }

}