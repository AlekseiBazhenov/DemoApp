package uz.ucell.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.ucell.app.BuildConfig.APP_CENTER_APP_SECRET_KEY
import uz.ucell.core.BuildConfig
import uz.ucell.core_storage.api.CoreStorage
import uz.ucell.debugpanel.ShakeListener
import uz.ucell.navigation.DebugPanel
import uz.ucell.navigation.NavigationDestination
import uz.ucell.navigation.NavigationHost
import uz.ucell.networking.SetupNetworkInterface
import uz.ucell.utils.PhoneUtils
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity(), NavigationHost {

    @Inject
    lateinit var shakeListener: ShakeListener

    @Inject
    lateinit var phoneUtils: PhoneUtils

    @Inject
    lateinit var coreStorage: CoreStorage

    @Inject
    lateinit var networkInterface: SetupNetworkInterface

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContentView(R.layout.activity_app)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            launch { onShakeListener() }
            launch { initUserAgent() }
            launch { onNetworkingHeadersSync() }
        }

        AppCenter.start(
            application,
            APP_CENTER_APP_SECRET_KEY,
            Analytics::class.java,
            Crashes::class.java
        )
    }

    override fun onResume() {
        shakeListener.registerListener()
        super.onResume()
    }

    override fun onPause() {
        shakeListener.unregisterListener()
        super.onPause()
    }

    override fun navigateTo(destination: NavigationDestination) {
        navController.navigate(NavigationResolver.toDirection(destination))
    }

    override fun navigateBack() {
        navController.popBackStack()
    }

    private suspend fun onShakeListener() {
        shakeListener.shakeListener()
            .filter { BuildConfig.DEBUG }
            .debounce(200)
            .filter { navController.currentDestination?.id != R.id.debugpanel }
            .collect {
                navigateTo(DebugPanel)
            }
    }

    private suspend fun initUserAgent() {
        val userAgent = coreStorage.getUserAgent().first()
        if (userAgent.isNullOrEmpty()) {
            coreStorage.setUserAgent(phoneUtils.getUserAgent())
        }
    }

    private suspend fun onNetworkingHeadersSync() {
        merge(
            coreStorage.getUserAgent()
                .onEach { networkInterface.setUserAgent(it) },
            coreStorage.getDeviceId()
                .onEach { networkInterface.setDeviceId(it) },
            coreStorage.getSelectedLanguage()
                .onEach { networkInterface.setLocalization(it) }
        ).collect()
    }
}
