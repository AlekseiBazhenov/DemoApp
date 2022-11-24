package uz.ucell.ui_kit.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
fun BitmapImage(modifier: Modifier = Modifier, bitmap: Bitmap) {
    Image(
        modifier = modifier,
        bitmap = bitmap.asImageBitmap(),
        contentScale = ContentScale.Fit,
        contentDescription = "some useful description",
    )
}
