package pl.kwasow.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kwasow.R

// ====== Public composables
@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    FilledTonalButton(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_google),
            contentDescription = stringResource(id = R.string.contentDescription_google_icon),
            modifier = Modifier.size(30.dp),
        )
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = stringResource(id = R.string.login_google),
        )
    }
}

// ====== Previews
@Preview
@Composable
fun GoogleSignInButtonPreview() {
    GoogleSignInButton(onClick = {})
}
