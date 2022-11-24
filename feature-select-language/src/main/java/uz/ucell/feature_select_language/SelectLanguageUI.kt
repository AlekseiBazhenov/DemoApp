package uz.ucell.feature_select_language

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import uz.ucell.core.models.Language
import uz.ucell.feature_select_language.contract.SelectLanguageState
import uz.ucell.ui_kit.components.Texts
import uz.ucell.ui_kit.components.UcellLogo
import uz.ucell.ui_kit.extentions.ucellShadow
import uz.ucell.utils.ResourcesUtils

@Composable
fun SelectLanguageScreen(
    state: SelectLanguageState,
    onSelectLanguage: (Language) -> Unit
) {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .padding(32.dp)
    ) {
        UcellLogo(
            modifier = Modifier.padding(top = 58.dp)
        )
        Texts.H2(
            modifier = Modifier.padding(top = 16.dp),
            title = ResourcesUtils.getStringForLanguage(
                LocalContext.current,
                state.primaryLanguage,
                R.string.select_language
            )
        )
        Texts.ParagraphText(
            modifier = Modifier.padding(top = 4.dp),
            title = ResourcesUtils.getStringForLanguage(
                LocalContext.current,
                state.secondaryLanguage,
                R.string.select_language
            ),
            color = colorResource(R.color.sky3)
        )
        LanguagesList(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            state.languages,
            onSelectLanguage
        )
    }
}

@Composable
fun LanguagesList(
    modifier: Modifier,
    languages: List<Language>,
    onSelectLanguage: (Language) -> Unit
) {
    Card(
        modifier = modifier.ucellShadow(),
        shape = RoundedCornerShape(12.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp)
        ) {
            items(languages) {
                LanguageRow(
                    modifier = Modifier.fillMaxWidth(),
                    language = it,
                    onSelectLanguage = onSelectLanguage
                )
            }
        }
    }
}

@Composable
fun LanguageRow(
    modifier: Modifier,
    language: Language,
    onSelectLanguage: (Language) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable {
            onSelectLanguage(language)
        }
    ) {
        Image(
            painter = painterResource(language.flag),
            contentDescription = ""
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp),
            text = stringResource(language.title),
            style = MaterialTheme.typography.body1
        )
        Image(
            modifier = Modifier
                .padding(end = 12.dp),
            painter = painterResource(R.drawable.ic_arrow_right_18),
            contentDescription = ""
        )
    }
}
