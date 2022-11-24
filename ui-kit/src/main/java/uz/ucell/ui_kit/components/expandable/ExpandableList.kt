package uz.ucell.ui_kit.components.expandable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@Composable
fun ExpandableList(
    title: String,
    initialStateExpanded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val isExpanded = remember { MutableTransitionState(initialStateExpanded) }
    val iconRotationState by animateFloatAsState(
        targetValue = if (isExpanded.targetState) 180f else 0f,
        animationSpec = tween(durationMillis = EXPAND_ANIMATION_LONG)
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.clickableWithoutIndication {
                isExpanded.targetState = !isExpanded.currentState
            }
        ) {
            Texts.H3(
                modifier = Modifier.weight(1f),
                title = title
            )
            DropdownButton(state = iconRotationState)
        }

        val animationSpecIntSize = tween<IntSize>(EXPAND_ANIMATION_LONG)
        val animationSpecFloat = tween<Float>(EXPAND_ANIMATION_LONG)
        val enterAnimation = fadeIn(animationSpec = animationSpecFloat) +
            expandVertically(animationSpec = animationSpecIntSize)
        val exitAnimation = fadeOut(animationSpec = animationSpecFloat) +
            shrinkVertically(animationSpec = animationSpecIntSize)
        AnimatedVisibility(
            visibleState = isExpanded,
            enter = enterAnimation,
            exit = exitAnimation,
        ) {
            content()
        }
    }
}

@Composable
private fun DropdownButton(modifier: Modifier = Modifier, state: Float) {
    Box(
        modifier = modifier
            .size(24.dp)
            .shadow(
                elevation = 4.dp, shape = CircleShape,
                ambientColor = colorResource(R.color.white),
                spotColor = colorResource(R.color.sky3)
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
        ) {
            Image(
                modifier = Modifier.rotate(state),
                painter = painterResource(R.drawable.ic_arrow_filled_bottom),
                contentDescription = "",
                contentScale = ContentScale.None
            )
        }
    }
}
