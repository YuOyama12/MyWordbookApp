package com.yuoyama12.mywordbook.ui.wordbooks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.components.SimpleInputDialog

@Composable
fun WordbooksScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        floatingActionButton = {
            var openAddDialog by remember { mutableStateOf(false) }

            FloatingActionButton(
                onClick = { openAddDialog = true },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.fab_add_desc),
                )

                if (openAddDialog) {
                    SimpleInputDialog(
                        title = stringResource(R.string.dialog_add_wordbook_title),
                        textFieldHint = stringResource(R.string.dialog_add_wordbook_hint),
                        positiveButtonText = stringResource(R.string.dialog_add_workbook_positive_button),
                        onDismissRequest = { openAddDialog = false }
                    ) { inputtedText ->


                    }
                }

            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {

        }
    }
}