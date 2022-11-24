package uz.ucell.ui_kit.components.nav_bar.collapsing

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ParentDataModifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.ucell.ui_kit.R
import uz.ucell.ui_kit.components.nav_bar.NavigationAction
import uz.ucell.ui_kit.components.nav_bar.NavigationIcon
import uz.ucell.ui_kit.components.nav_bar.ScrollStrategy
import uz.ucell.ui_kit.theme.medium
import kotlin.math.max

class CollapsingToolbarScaffoldState(
    val toolbarState: CollapsingToolbarState,
    initialOffsetY: Int = 0
) {
    val offsetY: Int
        get() = offsetYState.value

    internal val offsetYState = mutableStateOf(initialOffsetY)
}

private class CollapsingToolbarScaffoldStateSaver :
    Saver<CollapsingToolbarScaffoldState, List<Any>> {
    override fun restore(value: List<Any>): CollapsingToolbarScaffoldState =
        CollapsingToolbarScaffoldState(
            CollapsingToolbarState(value[0] as Int),
            value[1] as Int
        )

    override fun SaverScope.save(value: CollapsingToolbarScaffoldState): List<Any> =
        listOf(
            value.toolbarState.height,
            value.offsetY
        )
}

@Composable
fun rememberCollapsingToolbarScaffoldState(
    toolbarState: CollapsingToolbarState = rememberCollapsingToolbarState()
): CollapsingToolbarScaffoldState {
    return rememberSaveable(toolbarState, saver = CollapsingToolbarScaffoldStateSaver()) {
        CollapsingToolbarScaffoldState(toolbarState)
    }
}

interface CollapsingToolbarScaffoldScope {
    fun Modifier.align(alignment: Alignment): Modifier
}

@Composable
fun CollapsingToolbarScaffold(
    modifier: Modifier,
    state: CollapsingToolbarScaffoldState,
    scrollStrategy: ScrollStrategy,
    enabled: Boolean = true,
    title: String,
    toolbarModifier: Modifier = Modifier,
    navigationAction: NavigationAction = NavigationAction.Empty,
    content: @Composable CollapsingToolbarScaffoldScope.() -> Unit
) {
    val flingBehavior = ScrollableDefaults.flingBehavior()
    val layoutDirection = LocalLayoutDirection.current

    val toolbarState = state.toolbarState

    val nestedScrollConnection = remember(scrollStrategy, state) {
        scrollStrategy.create(state.offsetYState, toolbarState, flingBehavior)
    }

    Layout(
        content = {
            CollapsingToolbar(
                modifier = if (toolbarState.progress == 0f) {
                    toolbarModifier.shadow(AppBarDefaults.TopAppBarElevation)
                } else {
                    toolbarModifier
                },
                collapsingToolbarState = toolbarState
            ) {
                val textSize = (18 + (30 - 18) * toolbarState.progress).sp
                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.white))
                        .fillMaxWidth()
                        .height(124.dp)
                        .pin()
                )

                NavigationIcon(navigationAction)?.let {
                    CompositionLocalProvider(content = it)
                }

                Text(
                    text = title,
                    modifier = Modifier
                        .road(
                            whenCollapsed = Alignment.Center,
                            whenExpanded = Alignment.BottomStart
                        )
                        .padding(if (toolbarState.progress == 0f) 24.dp else 16.dp),
                    color = colorResource(R.color.base_black),
                    fontSize = textSize,
                    fontWeight = FontWeight.W500,
                    fontFamily = medium.toFontFamily(),
                )
            }

            CollapsingToolbarScaffoldScopeInstance.content()
        },
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier.nestedScroll(nestedScrollConnection)
                } else {
                    Modifier
                }
            )
    ) { measurables, constraints ->
        check(measurables.size >= 2) {
            "the number of children should be at least 2: toolbar, (at least one) body"
        }

        val toolbarConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )
        val bodyConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
            maxHeight = when (scrollStrategy) {
                ScrollStrategy.ExitUntilCollapsed ->
                    (constraints.maxHeight - toolbarState.minHeight).coerceAtLeast(0)

                ScrollStrategy.EnterAlways, ScrollStrategy.EnterAlwaysCollapsed ->
                    constraints.maxHeight
            }
        )

        val toolbarPlaceable = measurables[0].measure(toolbarConstraints)

        val bodyMeasurables = measurables.subList(1, measurables.size)
        val childrenAlignments = bodyMeasurables.map {
            (it.parentData as? ScaffoldParentData)?.alignment
        }
        val bodyPlaceables = bodyMeasurables.map {
            it.measure(bodyConstraints)
        }

        val toolbarHeight = toolbarPlaceable.height

        val width = max(
            toolbarPlaceable.width,
            bodyPlaceables.maxOfOrNull { it.width } ?: 0
        ).coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = max(
            toolbarHeight,
            bodyPlaceables.maxOfOrNull { it.height } ?: 0
        ).coerceIn(constraints.minHeight, constraints.maxHeight)

        layout(width, height) {
            bodyPlaceables.forEachIndexed { index, placeable ->
                val alignment = childrenAlignments[index]

                if (alignment == null) {
                    placeable.placeRelative(0, toolbarHeight + state.offsetY)
                } else {
                    val offset = alignment.align(
                        size = IntSize(placeable.width, placeable.height),
                        space = IntSize(width, height),
                        layoutDirection = layoutDirection
                    )
                    placeable.place(offset)
                }
            }
            toolbarPlaceable.placeRelative(0, state.offsetY)
        }
    }
}

internal object CollapsingToolbarScaffoldScopeInstance : CollapsingToolbarScaffoldScope {
    override fun Modifier.align(alignment: Alignment): Modifier =
        this.then(ScaffoldChildAlignmentModifier(alignment))
}

private class ScaffoldChildAlignmentModifier(
    private val alignment: Alignment
) : ParentDataModifier {
    override fun Density.modifyParentData(parentData: Any?): Any {
        return (parentData as? ScaffoldParentData) ?: ScaffoldParentData(alignment)
    }
}

private data class ScaffoldParentData(
    var alignment: Alignment? = null
)
