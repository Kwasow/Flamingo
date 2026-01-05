package pl.kwasow.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import pl.kwasow.R

// ====== Public composables
@Composable
fun EditDeleteActions(
    onDeleteRequest: () -> Unit,
    onEditRequest: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription =
                    stringResource(
                        id = R.string.contentDescription_three_dot_menu,
                    ),
                tint = Color.Gray,
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            Column {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.edit)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription =
                                stringResource(
                                    id = R.string.contentDescription_edit_icon,
                                ),
                        )
                    },
                    onClick = {
                        expanded = false
                        onEditRequest()
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.delete)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription =
                                stringResource(
                                    id = R.string.contentDescription_delete_icon,
                                ),
                        )
                    },
                    onClick = {
                        expanded = false
                        onDeleteRequest()
                    },
                )
            }
        }
    }
}
