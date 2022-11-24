package uz.ucell.feature_otp.sms.views

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.core.R
import uz.ucell.ui_kit.components.ActionLabel
import uz.ucell.ui_kit.components.BitmapImage
import uz.ucell.ui_kit.components.progress.UcellLoader

@Composable
fun Captcha(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    image: Bitmap?,
    onRefreshCaptchaClick: () -> Unit,
) {
    Row(
        modifier = modifier.padding(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isLoading) {
            CaptchaLoader()
        } else {
            image?.let {
                Box(
                    modifier = Modifier
                        .width(148.dp)
                        .height(56.dp)
                ) {
                    BitmapImage(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = it
                    )
                }
            }
        }
        ActionLabel(
            text = stringResource(R.string.button_refresh),
            painter = painterResource(R.drawable.ic_refresh_16),
            isEnabled = !isLoading
        ) {
            onRefreshCaptchaClick()
        }
    }
}

@Composable
private fun CaptchaLoader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(148.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorResource(R.color.sky0)),
        contentAlignment = Alignment.Center
    ) {
        UcellLoader()
    }
}
