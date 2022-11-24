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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@Composable
fun ExpandableItem(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.sky0)),
    ) {
        val isExpanded = remember { MutableTransitionState(false) }
        val iconRotationState by animateFloatAsState(
            targetValue = if (isExpanded.targetState) 180f else 0f,
            animationSpec = tween(durationMillis = EXPAND_ANIMATION_LONG)
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .clickableWithoutIndication {
                    isExpanded.targetState = !isExpanded.currentState
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Texts.H4(
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
            .clip(CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
        ) {
            Image(
                modifier = Modifier.rotate(state),
                painter = painterResource(R.drawable.ic_arrow_bottom),
                contentDescription = "",
                contentScale = ContentScale.None
            )
        }
    }
}
