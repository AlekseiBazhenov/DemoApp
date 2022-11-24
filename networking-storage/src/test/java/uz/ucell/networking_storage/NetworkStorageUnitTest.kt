package uz.ucell.networking_storage

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class NetworkStorageUnitTest {
    private lateinit var profilePrefs: NetworkStorage
    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        context = Mockito.mock(Context::class.java)
        mockEditor = Mockito.mock(SharedPreferences.Editor::class.java)
        Mockito.`when`(context.getSharedPreferences(anyString(), anyInt()))
            .thenReturn(sharedPreferences)
        Mockito.`when`(sharedPreferences.edit())
            .thenReturn(mockEditor)

        profilePrefs = NetworkStorageImpl(sharedPreferences)
    }

    @Test
    fun testSaveRefreshToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)
        assertEquals(profilePrefs.getRefreshToken().token, token)
        verify(sharedPreferences, times(1)).getString(eq(NetworkStorageImpl.Constants.KEY_REFRESH_TOKEN), anyString())
    }

    @Test
    fun testObservableRefreshToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)

        val testObservable: TestObserver<Token> = profilePrefs.getRefreshTokenObservable().test()

        testObservable.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable.dispose()
        verify(sharedPreferences, times(1)).getString(eq(NetworkStorageImpl.Constants.KEY_REFRESH_TOKEN), anyString())
    }

    @Test
    fun testMultipleObservableRefreshToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)

        val testObservable1: TestObserver<Token> =
            profilePrefs.getRefreshTokenObservable().doOnNext { System.out.println(it) }.test()
        val testObservable2: TestObserver<Token> = profilePrefs.getRefreshTokenObservable().test()

        testObservable1.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable2.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable1.dispose()
        testObservable2.dispose()
        verify(sharedPreferences, times(2)).getString(eq(NetworkStorageImpl.Constants.KEY_REFRESH_TOKEN), anyString())
    }

    @Test
    fun testCompletableSetRefreshToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)
        Mockito.`when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.commit()).thenReturn(true)

        val testObservable: TestObserver<Token> = profilePrefs.getRefreshTokenObservable().test()
        Observable.just(token)
            .concatMapCompletable {
                profilePrefs.setRefreshTokenCompletable(it)
            }.subscribe()

        testObservable.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable.dispose()
        verify(mockEditor, times(1)).putString(eq(NetworkStorageImpl.Constants.KEY_REFRESH_TOKEN), anyString())
        verify(sharedPreferences, times(1)).getString(eq(NetworkStorageImpl.Constants.KEY_REFRESH_TOKEN), anyString())
    }

    @Test
    fun testSaveAccessToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)
        assertEquals(profilePrefs.getAccessToken().token, token)
        verify(sharedPreferences, times(1)).getString(eq(NetworkStorageImpl.Constants.KEY_ACCESS_TOKEN), anyString())
    }

    @Test
    fun testObservableAccessToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)

        val testObservable: TestObserver<Token> = profilePrefs.getAccessTokenObservable().test()

        testObservable.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable.dispose()
        verify(sharedPreferences, times(1)).getString(eq(NetworkStorageImpl.Constants.KEY_ACCESS_TOKEN), anyString())
    }

    @Test
    fun testMultipleObservableAccessToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)

        val testObservable1: TestObserver<Token> = profilePrefs.getAccessTokenObservable().test()
        val testObservable2: TestObserver<Token> = profilePrefs.getAccessTokenObservable().test()

        testObservable1.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable2.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable1.dispose()
        testObservable2.dispose()
        verify(sharedPreferences, times(2)).getString(eq(NetworkStorageImpl.Constants.KEY_ACCESS_TOKEN), anyString())
    }

    @Test
    fun testCompletableSetAccessToken() {
        val token = "some_token"
        Mockito.`when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(token)
        Mockito.`when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)
        Mockito.`when`(mockEditor.commit()).thenReturn(true)

        val testObservable: TestObserver<Token> = profilePrefs.getAccessTokenObservable().test()
        Observable.just(token)
            .concatMapCompletable {
                profilePrefs.setAccessTokenCompletable(it)
            }.subscribe()

        testObservable.assertNoErrors()
            .assertValueCount(1)
            .assertValue { it == Token(token) }

        testObservable.dispose()
        verify(mockEditor, times(1)).putString(eq(NetworkStorageImpl.Constants.KEY_ACCESS_TOKEN), anyString())
        verify(sharedPreferences, times(1)).getString(eq(NetworkStorageImpl.Constants.KEY_ACCESS_TOKEN), anyString())
    }
}
