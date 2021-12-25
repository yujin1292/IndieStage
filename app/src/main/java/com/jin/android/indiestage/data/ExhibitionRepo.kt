package com.jin.android.indiestage.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ExhibitionRepo {
    private val firestore = FirebaseFirestore.getInstance()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getExhibitions() = callbackFlow {
        val collection = firestore.collection("exhibition")
        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccess(value)
            } else {
                OnError(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun getExhibitionsById(id:String) = callbackFlow {
        val collection = firestore.collection("exhibition")
            .whereEqualTo("id",id)

        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                OnSuccess(value)
            } else {
                OnError(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun getArtist(id:String) = callbackFlow {
        val collection = firestore.collection("exhibition")
            .document(id)
            .collection("artist")

        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                ArtistOnSuccess(value)
            } else {
                ArtistOnError(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun getArtWorks(id:String) = callbackFlow {
        val collection = firestore.collection("exhibition")
            .document(id)
            .collection("artwork")

        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                ArtWorkOnSuccess(value)
            } else {
                ArtWorkOnError(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }
}
