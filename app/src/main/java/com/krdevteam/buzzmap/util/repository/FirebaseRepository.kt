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
import com.krdevteam.buzzmap.entity.User
import com.krdevteam.buzzmap.util.AppConstants.Companion.NEWS_ARTICLE
import com.krdevteam.buzzmap.util.AppConstants.Companion.USER_TYPE
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
    val userLiveData = MutableLiveData<User>()
    var articleId = ""

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
            .add(
                mapOf(
                    Pair("id", user()!!.uid),
                    Pair("name", user()!!.displayName),
                    Pair("image", user()!!.photoUrl)
                )
            )
            .await()
    }

    suspend fun addUser(type: String): Void? {

//        return firestore.collection(USER_TYPE)
//                .document(user()!!.uid)
//                .collection(type)
//                .add(mapOf(
//                        Pair("id", user()!!.uid),
//                        Pair("name", user()!!.displayName),
//                        Pair("image", user()!!.photoUrl)
//                ))
//                .await()

        return firestore.collection(USER_TYPE)
            .document("${user()!!.uid}$type")
            .set(
                mapOf(
                    Pair("email", user()!!.email),
                    Pair("id", user()!!.uid),
                    Pair("type", type)
                )
            )
            .await()
    }

    fun sendChatMessage(message: String) {

        firestore.collection("rooms")
            .document(roomId)
            .collection("messages")
            .add(
                mapOf(
                    Pair("text", message),
                    Pair("user", user()!!.uid),
                    Pair("timestamp", Timestamp.now())
                )
            )
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

    fun observeNews(type: String) {
        if (type == "Editor")
            loadEditorNews(type)
        else
            loadJournalistNews(type)
    }

    private fun loadEditorNews(type: String) {
        firestore.collection(NEWS_ARTICLE)
            .whereEqualTo("status", type != "Editor")
            .get()
            .addOnSuccessListener { documents ->
                val messages = documents.map {
                    Timber.d("${it.id} => ${it.data}")
                    News(
                        it["uid"] as String,
                        it["location"] as GeoPoint,
                        it["dateTime"] as Timestamp,
                        it["title"] as String,
                        it["details"] as String,
                        it["imageURL"] as ArrayList<String>,
                        it["status"] as Boolean,
                        it["editor"] as String
                    )
                }
                messages.let { newsLiveData.postValue(messages) }
            }
            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
                exception.printStackTrace()
            }
    }

    private fun loadJournalistNews(type: String) {
        firestore.collection(NEWS_ARTICLE)
            .whereEqualTo("status", type != "Editor")
            .whereEqualTo("uid", user()?.uid)
            .get()
            .addOnSuccessListener { documents ->
                val messages = documents.map {
                    Timber.d("${it.id} => ${it.data}")
                    News(
                        it["uid"] as String,
                        it["location"] as GeoPoint,
                        it["dateTime"] as Timestamp,
                        it["title"] as String,
                        it["details"] as String,
                        it["imageURL"] as ArrayList<String>,
                        it["status"] as Boolean,
                        it["editor"] as String
                    )
                }
                messages.let { newsLiveData.postValue(messages) }
            }
            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
                exception.printStackTrace()
            }
    }

    fun sendArticle(news: News) {
        val newsData = mapOf(
            Pair("uid", user()!!.uid),
            Pair("location", news.location),
            Pair("dateTime", Timestamp.now()),
            Pair("title", news.title),
            Pair("details", news.details),
            Pair("imageURL", news.imageURL)
        )
        if (articleId != "") {
            firestore.collection(NEWS_ARTICLE)
                .document(articleId)
                .set(newsData)
        } else {
            firestore.collection(NEWS_ARTICLE)
                .add(newsData)
        }
    }

    fun observeUser() {
        firestore.collection(USER_TYPE)
            .whereEqualTo("id", user()?.uid)
            .get()
            .addOnSuccessListener { documents ->
                var user: User? = null
                documents.map {
                    Timber.d("${it.id} => ${it.data}")
                    user = User(
                        it["id"] as String,
                        it["type"] as String,
                        it["email"] as String
                    )
                }
                user.let { userLiveData.postValue(user!!) }
            }
            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
                exception.printStackTrace()
            }
    }
}