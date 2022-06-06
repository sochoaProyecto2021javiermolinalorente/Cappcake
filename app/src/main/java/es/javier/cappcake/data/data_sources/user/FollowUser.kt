package es.javier.cappcake.data.data_sources.user

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import es.javier.cappcake.data.entities.FirebaseContracts
import es.javier.cappcake.domain.Response
import es.javier.cappcake.domain.activity.ActivityType
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.floor

class FollowUser @Inject constructor() {

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth

    /**
     * Method to follow an user
     *
     * @param followedUserId The if of the user to follow
     * @return The response with the status of the operation
     */
    suspend operator fun invoke(followedUserId: String) : Response<Boolean> {

        val followedUserRef = firestore.collection(FirebaseContracts.USER_COLLECTION).document(followedUserId)
        val countersRef = followedUserRef.collection(FirebaseContracts.USER_FOLLOWERS_COUNTER_COLLECTION)

        return suspendCoroutine { continuation ->
            firestore.runTransaction { transaction ->

                val userDocument = transaction.get(followedUserRef)

                transaction.update(
                    firestore.collection(FirebaseContracts.FOLLOWERS_COLLECTION).document(userDocument.getString(FirebaseContracts.USER_NAME)!!),
                    FirebaseContracts.FOLLOWERS_USERS,
                    FieldValue.arrayUnion(auth.uid)
                )

                transaction.update(
                    firestore.collection(FirebaseContracts.USER_COLLECTION).document(auth.uid!!),
                    FirebaseContracts.USER_FOLLOWING,
                    FieldValue.increment(1)
                )

                val shardId = floor(Math.random() * FirebaseContracts.USER_FOLLOWER_COUNTERS).toInt()
                transaction.update(
                    countersRef.document(shardId.toString()),
                    FirebaseContracts.USER_FOLLOWERS_COUNTER,
                    FieldValue.increment(1)
                )

                // Add activity data
                val activity = firestore.collection(FirebaseContracts.ACTIVITY_COLLECTION).document()
                transaction.set(activity, hashMapOf(
                    FirebaseContracts.ACTIVITY_USER_ID to auth.uid!!,
                    FirebaseContracts.ACTIVITY_AFFECTED_USER_ID to followedUserId,
                    FirebaseContracts.ACTIVITY_TYPE to ActivityType.FOLLOW.name,
                    FirebaseContracts.ACTIVITY_TIMESTAMP to Timestamp.now()
                ))

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