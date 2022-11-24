package uz.ucell.ui_kit.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.ireward.htmlcompose.HtmlText
import uz.ucell.ui_kit.extentions.drawDashedUnderline
import uz.ucell.ui_kit.theme.accentText
import uz.ucell.ui_kit.theme.book
import uz.ucell.ui_kit.theme.medium
import uz.ucell.ui_kit.theme.primaryText
import uz.ucell.ui_kit.theme.secondaryText

object Texts {

    @Composable
    fun H1(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = primaryText,
        textAlign: TextAlign = TextAlign.Left
    ) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.h1,
            fontFamily = medium.toFontFamily(),
            fontWeight = FontWeight.W500,
            fontSize = 30.sp,
            color = color,
            textAlign = textAlign
        )
    }

    @Composable
    fun H2(modifier: Modifier = Modifier, title: String, color: Color = primaryText) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.h2,
            fontFamily = medium.toFontFamily(),
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            color = color,
        )
    }

    @Composable
    fun H3(
        modifier: Modifier = Modifier,
        title: String,
        textAlign: TextAlign = TextAlign.Left,
        color: Color = primaryText
    ) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.h3,
            fontFamily = medium.toFontFamily(),
            fontWeight = FontWeight.W500,
            fontSize = 18.sp,
            textAlign = textAlign,
            color = color
        )
    }

    @Composable
    fun H4(
        modifier: Modifier = Modifier,
        title: String,
        textAlign: TextAlign = TextAlign.Left,
        color: Color = primaryText,
    ) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.h4,
            fontFamily = medium.toFontFamily(),
            fontWeight = FontWeight.W500,
            fontSize = 15.sp,
            color = color,
            textAlign = textAlign
        )
    }

    @Composable
    fun ParagraphText(
        modifier: Modifier = Modifier,
        title: String,
        textAlign: TextAlign = TextAlign.Left,
        color: Color = primaryText,
        lineHeight: TextUnit = TextUnit.Unspecified,
        wide: Boolean = false
    ) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.subtitle2,
            fontFamily = book.toFontFamily(),
            fontWeight = FontWeight.W400,
            fontSize = 15.sp,
            color = color,
            textAlign = textAlign,
            lineHeight = if (wide) 24.sp else 18.sp
        )
    }

    @Composable
    fun Caption(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = secondaryText,
        overflow: TextOverflow = TextOverflow.Ellipsis,
        textAlign: TextAlign = TextAlign.Left
    ) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.caption,
            fontFamily = book.toFontFamily(),
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            color = color,
            overflow = overflow,
            textAlign = textAlign,
            lineHeight = 18.sp
        )
    }

    @Composable
    fun CaptionMedium(
        modifier: Modifier = Modifier,
        title: String,
        color: Color = primaryText,
        overflow: TextOverflow = TextOverflow.Ellipsis,
        textAlign: TextAlign = TextAlign.Left
    ) {
        Text(
            modifier = modifier,
            text = title,
            style = MaterialTheme.typography.caption,
            fontFamily = medium.toFontFamily(),
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            color = color,
            overflow = overflow,
            textAlign = textAlign
        )
    }

    @Composable
    fun Html(
        modifier: Modifier = Modifier,
        title: String,
        linkClicked: (String) -> Unit
    ) {
        HtmlText(
            modifier = modifier,
            text = title,
            fontSize = 15.sp,
            style = MaterialTheme.typography.subtitle1,
            URLSpanStyle = SpanStyle(
                color = accentText,
                textDecoration = TextDecoration.None
            ),
            linkClicked = { linkClicked(it) }
        )
    }

    // TODO: make clickable
    @Composable
    fun DashedUnderline(
        modifier: Modifier = Modifier,
        text: String,
        textAlign: TextAlign = TextAlign.Left,
        color: Color = primaryText,
        onClick: (() -> Unit)? = null
    ) {
        var layout by remember { mutableStateOf<TextLayoutResult?>(null) }

        Text(
            modifier = modifier.drawDashedUnderline(layout),
            text = text,
            style = MaterialTheme.typography.subtitle2,
            fontFamily = book.toFontFamily(),
            fontWeight = FontWeight.W400,
            fontSize = 15.sp,
            color = color,
            textAlign = textAlign,
            onTextLayout = {
                layout = it
            },
        )
    }
}
