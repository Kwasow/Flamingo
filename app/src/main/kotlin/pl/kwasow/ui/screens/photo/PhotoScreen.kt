package pl.kwasow.ui.screens.photo

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import pl.kwasow.R
import pl.kwasow.ui.components.PhotoView
import pl.kwasow.ui.composition.LocalFlamingoNavigation

// ====== Public composables
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PhotoPreviewScreen(uri: String) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black),
    ) {
        TopAppBar()
        PhotoView(
            modifier = Modifier.fillMaxSize(),
            uri = uri,
            clickable = false,
        )
    }
}

// ====== Private composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar() {
    val navigation = LocalFlamingoNavigation.current

    CenterAlignedTopAppBar(
        title = {},
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = Color.White,
            ),
        navigationIcon = {
            IconButton(
                onClick = navigation.navigateBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription =
                        stringResource(
                            id = R.string.contentDescription_navigate_back,
                        ),
                )
            }
        },
    )
}
