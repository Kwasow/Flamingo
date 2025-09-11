package pl.kwasow.ui.screens.modules.missingyou

import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.data.enums.InteractionSource
import pl.kwasow.ui.components.FlamingoBackgroundLight
import pl.kwasow.ui.components.FlamingoTopAppBar
import pl.kwasow.ui.composition.LocalFlamingoNavigation
import pl.kwasow.utils.FlamingoLogger

// ====== Public composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissingYouModuleScreen(interactionSource: InteractionSource) {
    val viewModel = koinViewModel<MissingYouModuleViewModel>()
    val navigation = LocalFlamingoNavigation.current

    LaunchedEffect(true) {
        FlamingoLogger.d("MissingYouModuleScreen, source: $interactionSource")
    }
    Scaffold(
        topBar = {
            FlamingoTopAppBar(
                title = stringResource(MissingYouModuleInfo.nameId),
                onBackPressed = navigation.navigateBack,
            )
        },
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) },
    ) { paddingValues ->
        FlamingoBackgroundLight(modifier = Modifier.padding(paddingValues))

        MainView(paddingValues = paddingValues)
    }
}

// ====== Private composables
@Composable
private fun MainView(paddingValues: PaddingValues) {
    val context = LocalContext.current
    val viewModel = koinViewModel<MissingYouModuleViewModel>()

    val mediaPlayer = MediaPlayer.create(context, R.raw.magic)

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        NecklaceIcon(
            hueVisible = viewModel.hueVisible,
            onClick = { viewModel.sendMissingYou(mediaPlayer) },
        )

        Text(
            text = stringResource(id = R.string.module_missingyou_touch_to_send),
            color = Color.Gray,
            fontWeight = FontWeight.Light,
            fontSize = 20.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
        )
    }
}

// ====== Private composables
@Composable
private fun NecklaceIcon(
    hueVisible: Boolean,
    onClick: () -> Unit,
) {
    val hueAlpha by animateFloatAsState(
        if (hueVisible) 0.5f else 0f,
        label = "hueAlpha",
    )

    Box(
        modifier =
            Modifier
                .fillMaxWidth(0.5f)
                .aspectRatio(1.0f)
                .padding(bottom = 36.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_necklace),
            contentDescription = stringResource(id = R.string.contentDescription_necklace_icon),
            tint = Color.Yellow,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .offset(x = (-1).dp, y = 1.dp)
                    .blur(10.dp)
                    .alpha(hueAlpha),
        )
        Image(
            painter = painterResource(id = R.drawable.ic_necklace),
            contentDescription = stringResource(id = R.string.contentDescription_necklace_icon),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable(
                        onClick = onClick,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false),
                    ),
        )
    }
}

// ====== Previews
// TODO: Broken preview
@Preview
@Composable
private fun MissingYouModuleScreenPreview() {
    MissingYouModuleScreen(
        interactionSource = InteractionSource.User,
    )
}
