package com.org.datingapp.features.onboarding.photos

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storageMetadata
import com.org.datingapp.core.di.UserStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

interface PhotoUploader {

    // Refector this and meke more redeable


    suspend fun remove(uri : Uri)

    suspend fun upload(newUri : Uri, oldUri : Uri) : String

    suspend fun upload(uri : Uri) : String

    class Firebase
    @Inject
    constructor(private val firebaseStorage: FirebaseStorage,
                @UserStorage
                private val userStorage : StorageReference) : PhotoUploader {


        private fun buildUploadPhotoTask(photoUri : Uri) : Task<Uri> {

            val uuid = UUID.randomUUID().toString()


            val metadata = storageMetadata {
                contentType = "image/jpg"
            }

            val newStorageRef = userStorage
                .child("${uuid}.jpg")

            val uploadTask = newStorageRef.putFile(photoUri, metadata)

            return uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                newStorageRef.downloadUrl
            }
        }


        private fun buildDeleteOldUriTask(oldUri : Uri) : Task<Void>  {
            val storageReference = firebaseStorage.reference
            val imageReference = storageReference.storage.getReferenceFromUrl(oldUri.toString())
            return imageReference.delete()
        }


        private fun buildUploadAndDeleteOldUriTask(newUri: Uri, oldUri: Uri) : Task<Uri> {
            val downloadUriTask = buildUploadPhotoTask(newUri)
            val deleteOldUriTask = buildDeleteOldUriTask(oldUri)
            return deleteOldUriTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                downloadUriTask
            }
        }

        override suspend fun remove(uri: Uri) {
            val deletePhotoTask = buildDeleteOldUriTask(uri)
            deletePhotoTask.await()
        }

        override suspend fun upload(newUri: Uri, oldUri: Uri): String {
            return withContext(Dispatchers.IO)  {
                val deleteAndDownloadUriTask = buildUploadAndDeleteOldUriTask(newUri, oldUri)
                deleteAndDownloadUriTask.await().toString()
            }
        }

        override suspend fun upload(uri: Uri): String {
            return withContext(Dispatchers.IO) {
                val downloadUriTask = buildUploadPhotoTask(uri)
                downloadUriTask.await().toString()
            }
        }

    }
}
