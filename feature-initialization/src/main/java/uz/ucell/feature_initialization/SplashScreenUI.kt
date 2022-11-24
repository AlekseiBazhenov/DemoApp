package uz.ucell.feature_initialization

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import uz.ucell.core.R
import uz.ucell.ui_kit.components.AnimationData
import uz.ucell.ui_kit.components.LottieAnimationUiKit
import uz.ucell.ui_kit.components.UcellLogo

@Composable
fun SplashScreenUI(onAnimationEnded: () -> Unit) {

    val loaderComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_screen_animation))

    val animationData = AnimationData(
        lottieComposition = loaderComposition,
        withAnimation = true,
        modifier = Modifier
            .width(200.dp)
            .height(320.dp),
        onClick = { },
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(200.dp))
            LottieAnimationUiKit(
                animationData,
                LottieConstants.IterateForever,
                onAnimationEnded
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(254.dp))
            UcellLogo(
                modifier = Modifier
                    .size(160.dp, 50.dp)
            )
        }
    }
}
