package uz.ucell.utils

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricPromptUtils {

    fun createBiometricPrompt(
        activity: FragmentActivity,
        success: (() -> Unit)? = null,
        failure: (() -> Unit)? = null,
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errCode, errString)
                failure?.invoke()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                failure?.invoke()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                success?.invoke()
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    fun createPromptInfo(context: Context): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(context.getString(R.string.auth_biometry_prompt_title))
            setDescription(context.getString(R.string.auth_biometry_prompt_message))
            setConfirmationRequired(false)
            setNegativeButtonText(context.getString(R.string.auth_biometry_prompt_button))
        }.build()
}
