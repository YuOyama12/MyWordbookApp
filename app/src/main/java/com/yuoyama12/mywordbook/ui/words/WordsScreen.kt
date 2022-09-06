package com.yuoyama12.mywordbook.ui.words

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.data.Wordbook

@Composable
fun WordsScreen(
    wordbook: Wordbook,
    onNavigationIconClicked: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(Modifier.fillMaxWidth()) {
                        Text(
                            text = wordbook.name,
                            modifier = Modifier.weight(1f, false),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = stringResource(R.string.words_screen_app_bar_header),
                            modifier = Modifier.padding(end = 15.dp)
                        )

                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigationIconClicked() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            var openAddDialog by rememberSaveable { mutableStateOf(false) }

            FloatingActionButton(
                onClick = { openAddDialog = true },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.fab_add_desc),
                )

                if (openAddDialog) {
                    InsertWordDialog(
                        title = stringResource(R.string.dialog_add_word_title),
                        message = stringResource(R.string.dialog_add_word_message),
                        wordbookId = wordbook.id,
                        positiveButtonText = stringResource(R.string.dialog_add_workbook_positive_button),
                        onDismissRequest = { openAddDialog = false }
                    )
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