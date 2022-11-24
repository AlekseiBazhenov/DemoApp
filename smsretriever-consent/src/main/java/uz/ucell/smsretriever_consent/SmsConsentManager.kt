package uz.ucell.smsretriever_consent

import android.content.Intent

interface SmsConsentManager {
    fun start()
    fun setListener(listener: (Intent) -> Unit)
    fun parseSmsCode(data: Intent): String
}
