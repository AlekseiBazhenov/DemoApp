package uz.ucell.ui_kit.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.rememberLottieAnimatable
import kotlinx.coroutines.delay
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

/**
 * It is important to use different [LottieAnimationUiKit] for different lottie compositions
 * to prevent flashing when switching statuses
 */
@Composable
fun LottieAnimationUiKit(
    animationData: AnimationData,
    iterations: Int,
    onAnimationEnded: (() -> Unit)? = null
) = with(animationData) {
    val lottieAnimatable = rememberLottieAnimatable()

    LaunchedEffect(lottieComposition) {
        if (withAnimation) {
            delay(delay)
            lottieAnimatable.animate(lottieComposition)
        } else {
            lottieAnimatable.animate(
                composition = lottieComposition,
                initialProgress = LottieClipSpec.Progress().max
            )
        }
    }
    LottieAnimation(
        composition = lottieComposition,
        iterations = iterations,
        modifier = Modifier
            .then(modifier)
            .fillMaxSize()
            .clickableWithoutIndication(onClick)
    )

    if (animationData.withAnimation && lottieAnimatable.isAtEnd) {
        onAnimationEnded?.invoke()
    }
}

data class AnimationData(
    val lottieComposition: LottieComposition?,
    val withAnimation: Boolean,
    val delay: Long = 0L,
    val modifier: Modifier,
    val onClick: () -> Unit,
)
