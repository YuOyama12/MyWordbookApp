package com.yuoyama12.mywordbook.menu

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.yuoyama12.mywordbook.R

@Composable
fun MultiOptionMenu(
    expand: Boolean,
    menuIcon: ImageVector = Icons.Default.MoreVert,
    iconClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    var openEditTagsDialog by rememberSaveable { mutableStateOf(false) }

    IconButton(
        onClick = { iconClicked() }
    ) {
        Icon(
            imageVector = menuIcon,
            contentDescription = null
        )
    }

    DropdownMenu(
        expanded = expand,
        onDismissRequest = { onDismissRequest() }
    ) {
        DropdownMenuItem(
            onClick = {
                openEditTagsDialog = true
                onDismissRequest()
            }
        ) {
            Text(text = stringResource(R.string.option_menu_word_tags_title))
        }
    }

    if (openEditTagsDialog) {
        EditTagsDialog(
            title = stringResource(R.string.dialog_edit_word_tags_title),
            onDismissRequest = { openEditTagsDialog = false }
        )
    }
}