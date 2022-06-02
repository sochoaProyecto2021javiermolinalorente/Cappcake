package es.javier.cappcake.data.data_sources.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.floor

class UnfollowUser @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    suspend fun unfollowUser(unfollowedUser: String) : Response<Boolean> {

        val unfollowedUserRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(unfollowedUser)
        val countersRef = unfollowedUserRef.collection(FirebaseContracts.USER_FOLLOWERS_COUNTER_COLLECTION)

        return suspendCoroutine { continuation ->
            firestore.runTransaction { transaction ->

                val unfollowedUserDocument = transaction.get(unfollowedUserRef)

                transaction.update(
                    firestore.collection(FirebaseContracts.USER_COLLECTION).document(auth.uid!!),
                    FirebaseContracts.USER_FOLLOWING,
                    FieldValue.increment(-1)
                )

                val shardId = floor(Math.random() * FirebaseContracts.USER_FOLLOWER_COUNTERS).toInt()
                transaction.update(countersRef.document(shardId.toString()), FirebaseContracts.USER_FOLLOWERS_COUNTER, FieldValue.increment(-1))

                transaction.update(
                    firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION).document(unfollowedUserDocument.getString(FirebaseContracts.USER_NAME)!!),
                    FirebaseContracts.FOLLOWERS_USERS,
                    FieldValue.arrayRemove(auth.uid)
                )

            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(Response.Success(data = true))
                } else {
                    continuation.resume(Response.Failiure(data = false, throwable = task.exception))
                }
            }

        }

    }

}