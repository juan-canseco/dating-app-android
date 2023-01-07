package com.org.datingapp.features.auth

import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.CollectionReference
import com.org.datingapp.core.di.UserCollection
import com.org.datingapp.core.domain.user.User
import com.org.datingapp.core.exception.Failure
import com.org.datingapp.core.functional.Either
import com.org.datingapp.core.interactor.UseCase.None
import com.org.datingapp.core.platform.NetworkHandler
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.jvm.Throws

interface Authenticator {

    suspend fun signIn(email : String, password : String) : Either<Failure, None>
    suspend fun signUp(email : String, password : String) : Either<Failure, None>
    suspend fun completeProfile(userProfile : User) : Either<Failure, None>
    suspend fun getCurrentUser() : User
    suspend fun profileComplete() : Boolean
    suspend fun updateProfile(user : User) : Either<Failure, None>
    fun userLoggedIn() : Boolean
    @Throws(NullPointerException::class)
    fun userId() : String

    class Failures {
        class UserNotLoggedIn : Failure.FeatureFailure()
        class UserNotFound : Failure.FeatureFailure()
        class EmailCollision : Failure.FeatureFailure()
        class UsernameCollision : Failure.FeatureFailure()
        class WrongCredentials : Failure.FeatureFailure()
        class TooManyRequest : Failure.FeatureFailure()
    }

    class Firebase @Inject constructor(private val firebaseAuth : FirebaseAuth,
                                       @UserCollection
                                       private val userCollection : CollectionReference,
                                       private val networkHandler : NetworkHandler) : Authenticator {

        override suspend fun signIn(email: String, password: String): Either<Failure, None> {
            return try {
                if (!networkHandler.isNetworkAvailable())
                    Either.Left(Failure.NetworkConnection)
                firebaseAuth.signInWithEmailAndPassword(email, password).await()
                Either.Right(None())
            }
            catch (exception : Exception) {
                val failure = when (exception) {
                    is FirebaseAuthInvalidCredentialsException -> Failures.WrongCredentials()
                    is FirebaseAuthInvalidUserException -> Failures.WrongCredentials()
                    is FirebaseTooManyRequestsException -> Failures.TooManyRequest()
                    else -> Failure.ServerError
                }
                Either.Left(failure)
            }
        }

        override suspend fun signUp(email: String, password: String) : Either<Failure, None> {
            return try {
                if (!networkHandler.isNetworkAvailable())
                    Either.Left(Failure.NetworkConnection)
                firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                Either.Right(None())
            }
            catch (e : FirebaseAuthUserCollisionException) {
                Either.Left(Failures.EmailCollision())
            }
            catch (e : Exception) {
                e.printStackTrace()
                Either.Left(Failure.ServerError)
            }
        }

        override suspend fun completeProfile(userProfile : User) : Either<Failure, None> {
            return try {
                if (!networkHandler.isNetworkAvailable())
                    Either.Left(Failure.NetworkConnection)
                userCollection.document(userProfile.id).set(userProfile).await()
                Either.Right(None())
            }
            catch (e : Exception) {
                e.printStackTrace()
                Either.Left(Failure.ServerError)
            }
        }

        override suspend fun getCurrentUser(): User {
            return try {
                val currentUserId = userId()
                val task = userCollection
                    .document(currentUserId)
                    .get()
                val document = task.await()
                document.toObject(User::class.java)!!
            }
            catch (exception : Exception) {
                exception.printStackTrace()
                throw exception
            }
        }

        private suspend fun checkIfUserExists() : Boolean {
            val task = userCollection.whereEqualTo("id", userId()).get()
            val querySnapshot = task.await()
            val documents = querySnapshot.documents
            return documents.any()
        }


        private suspend fun checkIfUsernameExists(username : String) : Boolean {
            val task = userCollection.whereEqualTo("normalizedUsername", username.lowercase()).get()
            val querySnapshot = task.await()
            val documents = querySnapshot.documents
            return documents.any()
        }

        override suspend fun profileComplete(): Boolean {
            if (userLoggedIn())
                return checkIfUserExists()
            return false
        }

        override suspend fun updateProfile(user: User): Either<Failure, None> {
            return try {
                if (checkIfUsernameExists(user.username)) {
                    Either.Left(Failures.UsernameCollision())
                }
                userCollection.document(userId()).set(user).await()
                Either.Right(None())
            }
            catch (exception : Exception) {
                exception.printStackTrace()
                Either.Left(Failure.ServerError)
            }
        }

        override fun userLoggedIn(): Boolean = firebaseAuth.currentUser != null

        override fun userId(): String = firebaseAuth.uid!!

    }

}
