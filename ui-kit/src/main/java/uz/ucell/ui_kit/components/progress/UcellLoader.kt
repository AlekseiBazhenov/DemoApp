package uz.ucell.ui_kit.components.progress

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.AnimationData
import uz.ucell.ui_kit.components.LottieAnimationUiKit

// TODO support different loader color

@Composable
fun UcellLoader() {

    val loaderComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader))

    val animationData = AnimationData(
        lottieComposition = loaderComposition,
        withAnimation = true,
        modifier = Modifier,
        onClick = { },
    )

    LottieAnimationUiKit(animationData, LottieConstants.IterateForever)
}
