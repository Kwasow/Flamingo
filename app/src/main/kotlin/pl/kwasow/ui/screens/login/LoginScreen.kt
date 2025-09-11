package pl.kwasow.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import pl.kwasow.R
import pl.kwasow.ui.components.ErrorDialog
import pl.kwasow.ui.components.FlamingoBackgroundLight
import pl.kwasow.ui.components.LoadingView
import pl.kwasow.ui.composition.LocalFlamingoNavigation

// ====== Public composables
@Composable
fun LoginScreen() {
    val viewModel = koinViewModel<LoginScreenViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        FlamingoBackgroundLight()

        MainView()

        if (viewModel.isLoading) {
            // We use the clickable modifier so that the loading indicator intercepts
            // any clicks and prevents the user from interacting with buttons below
            // the loading indicator layer

            LoadingView(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color(0x80000000))
                        .clickable(enabled = false) {},
            )
        }

        ErrorDialog(
            title = stringResource(id = R.string.login_error),
            error = viewModel.error,
            isShowing = viewModel.error.isNotEmpty(),
            clearError = { viewModel.clearError() },
        )
    }
}

// ====== Private composables
@Composable
private fun MainView() {
    val viewModel = koinViewModel<LoginScreenViewModel>()
    val navigation = LocalFlamingoNavigation.current

    Column(
        modifier = Modifier.systemBarsPadding(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(horizontal = 32.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.contentDescription_karonia_logo),
            contentScale = ContentScale.FillWidth,
        )

        GoogleSignInButton(onClick = { viewModel.launchLogin(navigation.navigateToHome) })
    }
}

// ====== Previews
// TODO: Broken preview
@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}
