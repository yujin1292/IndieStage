package com.jin.android.indiestage.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class ExhibitionRepo {
    private val fireStore = FirebaseFirestore.getInstance()

    fun getExhibitions() = callbackFlow {
        val collection = fireStore.collection("exhibition")
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
        val collection = fireStore.collection("exhibition")
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

    fun getExhibitionEnterCode(id: String) = callbackFlow {
        val collection = fireStore.collection("exhibition")
            .document(id)

        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                EnterCodeOnSuccess(value?.get("enterCode").toString())
            } else {
                EnterCodeOnError(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun getArtist(id: String) = callbackFlow {
        val collection = fireStore.collection("exhibition")
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

    fun getArtWorks(id: String) = callbackFlow {
        val collection = fireStore.collection("exhibition")
            .document(id)
            .collection("artwork")

        val snapshotListener = collection.addSnapshotListener { value, error ->
            val response = if (error == null) {
                ArtWorksOnSuccess(value)
            } else {
                ArtWorksOnError(error)
            }

            this.trySend(response).isSuccess
        }

        awaitClose {
            snapshotListener.remove()
        }
    }

    fun getArtWork(exhibitionId: String, artWorkId: String) = callbackFlow {
        val collection = fireStore.collection("exhibition")
            .document(exhibitionId)
            .collection("artwork")
            .document(artWorkId)

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
