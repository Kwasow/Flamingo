package pl.kwasow.ui.screens.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.kwasow.R
import pl.kwasow.ui.screens.modules.whishlist.WishlistModuleInfo

// ====== Public composables
@Composable
fun ModuleListItem(
    moduleInfo: ModuleInfo,
    navigateToModule: () -> Unit,
) {
    val color = MaterialTheme.colorScheme.primaryContainer
    val onColor = MaterialTheme.colorScheme.background

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 1f),
        onClick = { navigateToModule() },
        colors = CardDefaults.cardColors(containerColor = color),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
        ) {
            ModuleName(
                moduleInfo = moduleInfo,
                color = onColor,
                modifier = Modifier.weight(1f),
            )

            VerticalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = onColor,
            )

            ModuleIcon(
                moduleInfo = moduleInfo,
                color = color,
                onColor = onColor,
            )
        }
    }
}

// ====== Private composables
@Composable
private fun ModuleName(
    moduleInfo: ModuleInfo,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = moduleInfo.nameId),
        style = MaterialTheme.typography.titleLarge,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier,
    )
}

@Composable
private fun ModuleIcon(
    moduleInfo: ModuleInfo,
    color: Color,
    onColor: Color,
) {
    Icon(
        painter = painterResource(id = moduleInfo.iconId),
        contentDescription =
            stringResource(
                id = R.string.contentDescription_module_icon,
                stringResource(id = moduleInfo.nameId),
            ),
        tint = color,
        modifier =
            Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .padding(15.dp)
                .background(
                    color = onColor,
                    shape = RoundedCornerShape(100f),
                )
                .padding(10.dp),
    )
}

// ====== Previews
@Preview
@Composable
private fun ModuleListItemPreview() {
    ModuleListItem(
        moduleInfo = WishlistModuleInfo,
        navigateToModule = {},
    )
}
