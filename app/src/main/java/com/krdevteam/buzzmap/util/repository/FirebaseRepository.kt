package com.krdevteam.buzzmap.util.repository

import androidx.lifecycle.MutableLiveData
import com.krdevteam.buzzmap.entity.ChatMessage
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.krdevteam.buzzmap.entity.News
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*

class FirebaseRepository {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
//    private val settings = FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build()

    var roomId = ""
    val chatMessagesLiveData = MutableLiveData<List<ChatMessage>>()

    val newsLiveData = MutableLiveData<List<News>>()

    fun user(): FirebaseUser? = auth.currentUser

    suspend fun login(email: String, password: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun register(email: String, password: String): AuthResult {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun addUserToRoom(roomId: String): DocumentReference {

        return firestore.collection("rooms")
            .document(roomId)
            .collection("users")
            .add(mapOf(
                Pair("id", user()!!.uid),
                Pair("name", user()!!.displayName),
                Pair("image", user()!!.photoUrl)
            ))
            .await()
    }

    fun sendChatMessage(message: String) {

        firestore.collection("rooms")
            .document(roomId)
            .collection("messages")
            .add(mapOf(
                Pair("text", message),
                Pair("user", user()!!.uid),
                Pair("timestamp", Timestamp.now())
            ))
    }

    fun observeChatMessages() {

        firestore.collection("rooms")
            .document(roomId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { messagesSnapshot, exception ->

                if (exception != null) {
                    exception.printStackTrace()
                    return@addSnapshotListener
                }

                val messages = messagesSnapshot?.documents?.map {
                    ChatMessage(
                        it["text"] as String,
                        it["user"] as String,
                        (it["timestamp"]) as Date
                    )
                }

                messages?.let { chatMessagesLiveData.postValue(messages!!) }
            }
    }

    fun observeNews() {

//        firestore.collection("news_article")
////            .document(roomId)
////            .collection("messages")
////            .orderBy("timestamp")
//            .addSnapshotListener { messagesSnapshot, exception ->
//
//                if (exception != null) {
//                    exception.printStackTrace()
//                    return@addSnapshotListener
//                }
//
//                val messages = messagesSnapshot?.documents?.map {
//                    News(
//                        it["uid"] as String,
//                        it["location"] as GeoPoint,
//                        it["dateTime"] as Timestamp,
//                        it["title"] as String,
//                        it["details"] as String,
//                        it["imageURL"] as ArrayList<String>
//                    )
//                }
//
//                messages?.let { newsLiveData.postValue(messages!!) }
//            }
//        firestore.firestoreSettings = settings
        firestore.collection("news_article")
            .get()
            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    Timber.d("${document.id} => ${document.data}")
//                }

                val messages = documents.map {
                    Timber.d("${it.id} => ${it.data}")
                    News(
                        it["uid"] as String,
                        it["location"] as GeoPoint,
                        it["dateTime"] as Timestamp,
                        it["title"] as String,
                        it["details"] as String,
                        it["imageURL"] as ArrayList<String>
                    )
                }
                messages.let { newsLiveData.postValue(messages) }
            }
            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
                exception.printStackTrace()
            }

    }
}