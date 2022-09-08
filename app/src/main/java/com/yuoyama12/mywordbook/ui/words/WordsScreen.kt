package com.yuoyama12.mywordbook.ui.words

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.ui.theme.wordCardBackgroundColor
import com.yuoyama12.mywordbook.ui.theme.wordbookBorderColor

private val fontSize = 16.sp
private val cardMinHeight = 85.dp

@Composable
fun WordsScreen(
    wordbook: Wordbook,
    onNavigationIconClicked: () -> Unit = {}
) {
    val viewModel: WordsViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadWordsBy(wordbook.id)
    }

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
            WordsList(
                viewModel = viewModel
            )
        }

    }
}


@Composable
fun WordsList(
    modifier: Modifier = Modifier,
    viewModel: WordsViewModel
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = viewModel.words
        ) { word ->

            Card(
                elevation = 8.dp,
                backgroundColor = wordCardBackgroundColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = cardMinHeight)
                    .height(intrinsicSize = IntrinsicSize.Min)
                    .padding(
                        top = 10.dp,
                        start = 16.dp,
                        end = 32.dp,
                        bottom = 10.dp
                    )
                    .border(
                        width = 0.8.dp,
                        color = wordbookBorderColor(),
                        shape = RectangleShape
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = word.word,
                        fontSize = fontSize,
                        modifier =
                        Modifier
                            .weight(0.4f)
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                    )

                    Divider(
                        modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight(),
                        color = wordbookBorderColor()
                    )

                    Text(
                        text = word.meaning,
                        fontSize = fontSize,
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                    )

                }
            }
        }
    }
}