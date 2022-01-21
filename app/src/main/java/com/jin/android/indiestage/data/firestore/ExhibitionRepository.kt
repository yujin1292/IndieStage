package com.jin.android.indiestage.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class ExhibitionRepository {
    private val fireStore = FirebaseFirestore.getInstance()

    fun getAllExhibitions() = callbackFlow {
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

    fun getOpenedExhibitions() = callbackFlow {
        val collection = fireStore.collection("exhibition").whereEqualTo("isOpened",true)
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

    fun getClosedExhibitions() = callbackFlow {
        val collection = fireStore.collection("exhibition").whereEqualTo("isOpened",false)
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
