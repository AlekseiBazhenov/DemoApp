package uz.ucell.feature_otp.enter_phone.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.ucell.core.models.Language
import uz.ucell.feature_otp.R
import uz.ucell.ui_kit.components.Texts

@Composable
fun SelectLanguage(
    modifier: Modifier = Modifier,
    language: Language,
    onSelectCLick: () -> Unit
) {
    Row(
        modifier = modifier.clickable {
            onSelectCLick()
        }
            .focusable(true),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(end = 8.dp)
                .clip(RoundedCornerShape(2.dp)),
            painter = painterResource(language.flagSmall),
            contentDescription = ""
        )
        Texts.ParagraphText(
            modifier = Modifier.padding(end = 5.dp),
            title = language.code.uppercase()
        )
        Image(
            painter = painterResource(R.drawable.ic_arrow_filled_bottom),
            contentDescription = ""
        )
    }
}
