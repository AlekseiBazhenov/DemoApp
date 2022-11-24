package uz.ucell.ui_kit.components.expandable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.extentions.clickableWithoutIndication

@Composable
fun ExpandableCaption(modifier: Modifier = Modifier, text: String, initialStateExpanded: Boolean) {
    Column(modifier = modifier) {
        val isExpanded = remember { MutableTransitionState(initialStateExpanded) }

        Row(
            modifier = Modifier.clickableWithoutIndication {
                isExpanded.targetState = !isExpanded.currentState
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Texts.CaptionMedium(
                modifier = Modifier.padding(end = 4.dp),
                title = stringResource(
                    if (isExpanded.targetState)
                        R.string.label_hide_info
                    else
                        R.string.label_show_more_info
                ),
                color = colorResource(R.color.rich_purple)
            )
            Image(
                painter = painterResource(
                    if (isExpanded.targetState)
                        R.drawable.ic_arrow_filled_top_small
                    else
                        R.drawable.ic_arrow_filled_bottom_small
                ),
                contentDescription = "",
                contentScale = ContentScale.None
            )
        }

        val animationSpecIntSize = tween<IntSize>(EXPAND_ANIMATION_SHORT)
        val animationSpecFloat = tween<Float>(EXPAND_ANIMATION_SHORT)
        val enterAnimation = fadeIn(animationSpec = animationSpecFloat) +
            expandVertically(animationSpec = animationSpecIntSize)
        val exitAnimation = fadeOut(animationSpec = animationSpecFloat) +
            shrinkVertically(animationSpec = animationSpecIntSize)
        AnimatedVisibility(
            visibleState = isExpanded,
            enter = enterAnimation,
            exit = exitAnimation,
        ) {
            Texts.Caption(
                modifier = Modifier.padding(top = 8.dp),
                title = text,
                color = colorResource(R.color.sky3)
            )
        }
    }
}
