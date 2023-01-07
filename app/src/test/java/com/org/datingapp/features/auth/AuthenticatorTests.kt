package com.org.datingapp.features.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.org.datingapp.UnitTest
import com.org.datingapp.core.domain.user.User
import com.org.datingapp.core.platform.NetworkHandler
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should not be`
import org.amshove.kluent.shouldBe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthenticatorTests : UnitTest() {

    private lateinit var authenticator: Authenticator.Firebase

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth


    @MockK
    private lateinit var networkHandler : NetworkHandler


    @RelaxedMockK
    private lateinit var userCollection: CollectionReference

    @Before
    fun setUp() {
        every { networkHandler.isNetworkAvailable() } returns true
        authenticator = Authenticator.Firebase(firebaseAuth, userCollection, networkHandler)
    }

    @Test
    fun `retrieving userId when user logged in returns validId`() {
        val mockId = "some-valid-id"
        every { firebaseAuth.uid!! } returns mockId
        val userId = authenticator.userId()
        userId `should not be` null
    }

    @Test
    fun `retrieving userId when user not logged in throws exception`() {
        every { firebaseAuth.uid }.returns(null)
        assertThrows(NullPointerException::class.java) {
            authenticator.userId()
        }
    }

    @Test
    fun `userLoggedIn when user is logged in returns true`() {
        val mockFirebaseUser = mockkClass(FirebaseUser::class)
        every { firebaseAuth.currentUser } returns (mockFirebaseUser)
        authenticator.userLoggedIn() shouldBe true
    }

    @Test
    fun `userLoggedIn when user is not logged in returns false`() {
        every { firebaseAuth.currentUser } returns null
        authenticator.userLoggedIn() shouldBe false
    }

    @Test
    fun `profileComplete when user not logged in returns false`() = runTest {
        every { firebaseAuth.currentUser } returns null
        authenticator.profileComplete() shouldBe false
    }

    @Test
    fun `profileComplete when user profile is complete returns true`() = runTest {

        val mockId = "some-valid-user-id"
        val mockFirebaseUser = mockkClass(FirebaseUser::class)
        val mockQuerySnapshot = mockk<QuerySnapshot>(relaxed = true)

        val taskMock = mockk<Task<QuerySnapshot>>()
        val documentsMock = mutableListOf(mockk<DocumentSnapshot>())

        every { firebaseAuth.uid } returns mockId
        every { firebaseAuth.currentUser } returns mockFirebaseUser

        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns null
        every { taskMock.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.documents } returns documentsMock
        every { userCollection.whereEqualTo("id", mockId).get()} returns taskMock


        authenticator.profileComplete() shouldBe true
    }

    @Test
    fun `profileComplete when user profile is not complete returns false`()  = runTest {
        val mockId = "some-valid-user-id"
        val mockFirebaseUser = mockkClass(FirebaseUser::class)
        val mockQuerySnapshot = mockk<QuerySnapshot>(relaxed = true)

        val taskMock = mockk<Task<QuerySnapshot>>()
        val documentsMock : MutableList<DocumentSnapshot> = mutableListOf()

        every { firebaseAuth.uid } returns mockId
        every { firebaseAuth.currentUser } returns mockFirebaseUser

        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns null
        every { taskMock.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.documents } returns documentsMock
        every { userCollection.whereEqualTo("id", mockId).get()} returns taskMock

        authenticator.profileComplete() shouldBe false
    }

    @Test
    fun `getCurrentUser when user not logged in returns failure`() = runTest {
        every { firebaseAuth.uid } returns null
        val result = authenticator.getCurrentUser()
        result.isLeft shouldBe true
    }

    @Test
    fun `getCurrentUser when user not found returns failure`() = runTest {

        val mockUserId = "some-invalid-userId"
        val taskMock = mockk<Task<DocumentSnapshot>>()
        val documentMock = mockk<DocumentSnapshot>(relaxed = true)

        every { firebaseAuth.uid } returns mockUserId
        every { userCollection.document(mockUserId).get() } returns taskMock
        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns null
        every { taskMock.result } returns documentMock
        every { documentMock.toObject(User::class.java) } returns null

        val result = authenticator.getCurrentUser()
        result.isLeft shouldBe true
    }

    @Test
    fun `getCurrentUser when user exists returns user`() = runTest {

        val mockUserId = "some-valid-user-id"
        val taskMock = mockk<Task<DocumentSnapshot>>()
        val documentMock = mockk<DocumentSnapshot>(relaxed = true)
        val userMock = mockkClass(User::class)

        every { firebaseAuth.uid } returns mockUserId
        every { userCollection.document(mockUserId).get() } returns taskMock
        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns null
        every { taskMock.result } returns documentMock
        every { documentMock.toObject(User::class.java) } returns userMock

        val result = authenticator.getCurrentUser()

        result.isRight shouldBe true
    }


    @Test
    fun `signUp when email already exists returns failure`() = runTest {
        val mockEmail = "john.doe@test.com"
        val mockPassword = "test1234"
        val taskMock  = mockk<Task<AuthResult>>()
        val exceptionMock = mockk<FirebaseAuthUserCollisionException>()
        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns exceptionMock
        every { firebaseAuth.createUserWithEmailAndPassword(mockEmail, mockPassword) } returns taskMock
        val result = authenticator.signUp(mockEmail, mockPassword)
        result.isLeft shouldBe true
    }

    @Test
    fun `signIn with invalid credentials returns failure`() = runTest {
        val mockEmail = "john.doe@test.com"
        val mockPassword = "1234"
        val taskMock = mockk<Task<AuthResult>>()
        val exceptionMock = mockk<FirebaseAuthInvalidCredentialsException>()
        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns exceptionMock
        every { firebaseAuth.signInWithEmailAndPassword(mockEmail, mockPassword) } returns taskMock
        val result = authenticator.signIn(mockEmail, mockPassword)
        result.isLeft shouldBe true
    }

    // https://medium.com/@debuggingisfun/android-relaxed-mocks-602304f6e1f
    @Test
    fun `signUp when sign up is successful returns none`() = runTest {
        val mockEmail = "john.doe@test.com"
        val mockPassword = "test1234"
        val taskMock  = mockk<Task<AuthResult>>()
        val authResultMock = mockk<AuthResult>()
        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns null
        every { taskMock.result } returns authResultMock
        every { firebaseAuth.createUserWithEmailAndPassword(mockEmail, mockPassword) } returns taskMock
        val result = authenticator.signUp(mockEmail, mockPassword)
        result.isRight shouldBe true
    }

    @Test
    fun `signIn with valid credentials returns None`() = runTest {
        val mockUserId = "some-valid-user-id"
        val mockEmail = "john.doe@test.com"
        val mockPassword = "Test1234"
        val taskMock = mockk<Task<AuthResult>>()
        val resultMock = mockk<AuthResult>()
        val userMock = mockk<FirebaseUser>()
        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns null
        every { taskMock.result } returns resultMock
        every { resultMock.user } returns userMock
        every { userMock.uid } returns mockUserId
        every { firebaseAuth.signInWithEmailAndPassword(mockEmail, mockPassword) } returns taskMock
        val result = authenticator.signIn(mockEmail, mockPassword)
        result.isRight shouldBe true
    }

    @Test
    fun `signIn too many returns failure`() = runTest {
        val mockEmail = "john.doe@test.com"
        val mockPassword = "1234"
        val taskMock = mockk<Task<AuthResult>>()
        val exceptionMock = mockk<FirebaseTooManyRequestsException>()
        every { taskMock.isComplete } returns true
        every { taskMock.isCanceled } returns false
        every { taskMock.exception } returns exceptionMock
        every { firebaseAuth.signInWithEmailAndPassword(mockEmail, mockPassword) } returns taskMock
        val result = authenticator.signIn(mockEmail, mockPassword)
        result.isLeft shouldBe true
    }

}