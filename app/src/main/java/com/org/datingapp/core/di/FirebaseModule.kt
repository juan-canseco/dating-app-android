package com.org.datingapp.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class UserCollection
@Qualifier
annotation class UserStorage

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Singleton
    @Provides
    fun providesFirebaseAuth() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun providesFirebaseFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun providesFirebaseStorage() : FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


    @Singleton
    @Provides
    @UserCollection
    fun provideUsersCollectionReference(rootRef : FirebaseFirestore) : CollectionReference {
        return rootRef.collection("users")
    }

    @Singleton
    @Provides
    @UserStorage
    fun providesStorageReference(storage : FirebaseStorage) : StorageReference {
        return storage.reference.child("profile_pictures")
    }

}