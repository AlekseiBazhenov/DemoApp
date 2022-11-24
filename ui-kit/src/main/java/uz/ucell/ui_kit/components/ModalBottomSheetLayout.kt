package uz.ucell.ui_kit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.ucell.ui_kit.R

@Composable
@ExperimentalMaterialApi
fun UcellModalBottomSheet(
    modalBottomSheetState: ModalBottomSheetState,
    content: @Composable () -> Unit,
    sheetContent: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        scrimColor = Color.Black.copy(alpha = 0.25f),
        sheetElevation = 12.dp,
        sheetState = modalBottomSheetState,
        sheetContent = {
            UcellModelSheetContent { sheetContent() }
        },
        content = { content() }
    )
}

@Composable
private fun UcellModelSheetContent(
    sheetContent: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .padding(top = 12.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
    ) {
        Row {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.bottom_sheet_swipe),
                contentDescription = null
            )
        }
        Column(
            content = sheetContent
        )
    }
}
