package uz.ucell.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhoneUtils @Inject constructor(@ApplicationContext val context: Context) {

    fun getDeviceLanguage(): String = Locale.getDefault().language

    fun getDeviceId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    /**
     * @return [AppName]/[AppVersion] ([PlatfromID]; [Device]; [PlatfromVersion])
     */
    fun getUserAgent(): String =
        "${context.packageManager.getApplicationLabel(context.applicationInfo)}/" +
            "${context.packageManager.getPackageInfo(context.packageName, 0).versionName} " +
            "(${Build.MANUFACTURER}; ${Build.MODEL}; ${Build.VERSION.SDK_INT})"

    fun isBiometricReady(): Boolean =
        BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG) ==
            BiometricManager.BIOMETRIC_SUCCESS
}
