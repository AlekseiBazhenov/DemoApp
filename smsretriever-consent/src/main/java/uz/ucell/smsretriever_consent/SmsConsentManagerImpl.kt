package uz.ucell.smsretriever_consent

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

class SmsConsentManagerImpl(private val context: Context) : SmsConsentManager {

    private var listener: ((Intent) -> Unit)? = null

    override fun setListener(listener: (Intent) -> Unit) {
        this.listener = listener
    }

    override fun parseSmsCode(data: Intent) =
        data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)?.let { message ->
            message.filter { it.isDigit() }
        } ?: ""

    private val smsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                stopReceiver()
                val extras = intent.extras
                val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status
                when (status?.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)

                        consentIntent?.let {
                            listener?.invoke(it)
                        }
                    }
                    else -> {
                        // log to monitoring (sentry, firebase)
                    }
                }
            }
        }
    }

    override fun start() {
        try {
            val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
            context.registerReceiver(smsReceiver, intentFilter)
            SmsRetriever.getClient(context).startSmsUserConsent(null)
        } catch (e: Exception) {
            // log to monitoring (sentry, firebase)
        }
    }

    private fun stopReceiver() {
        context.unregisterReceiver(smsReceiver)
    }
}
