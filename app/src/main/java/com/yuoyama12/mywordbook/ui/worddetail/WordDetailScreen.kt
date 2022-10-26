package com.yuoyama12.mywordbook.ui.worddetail

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.components.ConfirmationDialog
import com.yuoyama12.mywordbook.data.Word
import com.yuoyama12.mywordbook.ui.words.Header
import com.yuoyama12.mywordbook.ui.words.WordsViewModel

private val textFontSize = 20.sp

@Composable
fun WordDetailScreen(
    word: Word,
    onNavigationIconClicked: () -> Unit
) {
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current

    val isEditNoteValid = rememberSaveable { mutableStateOf(false) }
    val miscellaneousNote = rememberSaveable { mutableStateOf(word.miscellaneousNote) }

    var isEditNoteContentChanged by rememberSaveable { mutableStateOf(false) }
    var openDiscardConfirmationDialog by rememberSaveable{ mutableStateOf(false) }

    LaunchedEffect(configuration) {
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.word_detail_screen_app_bar_header)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isEditNoteContentChanged) {
                                openDiscardConfirmationDialog = true
                            } else {
                                onNavigationIconClicked()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            PortraitScreen(
                word = word,
                isEditNoteValid = isEditNoteValid,
                miscellaneousNote = miscellaneousNote,
                onEditNoteContentChanged = { isChanged ->
                    isEditNoteContentChanged = isChanged
                },
                onConfirmClicked = { isEditNoteContentChanged = false }
            )
        } else {
            LandscapeScreen(
                word = word,
                isEditNoteValid = isEditNoteValid,
                miscellaneousNote = miscellaneousNote,
                onEditNoteContentChanged = { isChanged ->
                    isEditNoteContentChanged = isChanged
                },
                onConfirmClicked = { isEditNoteContentChanged = false }
            )
        }

        //TODO: チェック
        BackHandler(enabled = true) {
            if (isEditNoteContentChanged) {
                openDiscardConfirmationDialog = true
            } else {
                onNavigationIconClicked()
            }
        }

        if (openDiscardConfirmationDialog) {
            ConfirmationDialog(
                title = stringResource(R.string.dialog_discard_changed_content_confirmation_title),
                message = stringResource(R.string.dialog_discard_changed_content_confirmation_message),
                positiveButtonText = stringResource(R.string.dialog_discard_content_confirmation_positive_button),
                onDismissRequest = { openDiscardConfirmationDialog = false },
                onPositiveClicked = { onNavigationIconClicked() }
            )
        }
    }
}

@Composable
private fun PortraitScreen(
    word: Word,
    isEditNoteValid: MutableState<Boolean>,
    miscellaneousNote: MutableState<String>,
    onEditNoteContentChanged: (isChanged: Boolean) -> Unit,
    onConfirmClicked: () -> Unit
) {
    val viewModel: WordsViewModel = hiltViewModel()
    var restoredMiscellaneousNote by rememberSaveable { mutableStateOf(word.miscellaneousNote) }

    Column(Modifier.fillMaxSize()) {

        Header(title = stringResource(R.string.word_display_word_header))

        SelectionContainer(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(ScrollState(0))
        ) {
            Text(
                text = word.word,
                modifier = Modifier
                    .height(55.dp)
                    .padding(15.dp),
                fontSize = textFontSize
            )
        }

        Header(title = stringResource(R.string.word_display_meaning_header))

        SelectionContainer(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 70.dp, max = 105.dp)
                .verticalScroll(ScrollState(0))
        ) {
            Text(
                text = word.meaning,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxHeight(),
                fontSize = textFontSize
            )
        }

        MiscellaneousNoteHeader(isEditNoteValid) {
            if (isEditNoteValid.value) {
                val note = miscellaneousNote.value
                val newWord =
                    word.copy(miscellaneousNote = note)

                viewModel.insertWord(newWord)
                restoredMiscellaneousNote = note

                onConfirmClicked()
            }

            isEditNoteValid.value = !(isEditNoteValid.value)
        }

        SelectionContainer(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(ScrollState(0))
                .weight(1f, true)
        ) {
            MiscellaneousNoteTextField (
                text = {
                    if (isEditNoteValid.value) {
                        OutlinedTextField(
                            value = miscellaneousNote.value,
                            onValueChange = {
                                miscellaneousNote.value = it

                                if (it != restoredMiscellaneousNote) {
                                    onEditNoteContentChanged(true)
                                } else {
                                    onEditNoteContentChanged(false)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = miscellaneousNote.value,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            fontSize = textFontSize
                        )
                    }
                }
            )
        }

    }
}

@Composable
private fun LandscapeScreen(
    word: Word,
    isEditNoteValid: MutableState<Boolean>,
    miscellaneousNote: MutableState<String>,
    onEditNoteContentChanged: (isChanged: Boolean) -> Unit,
    onConfirmClicked: () -> Unit
) {
    val viewModel: WordsViewModel = hiltViewModel()
    var restoredMiscellaneousNote by rememberSaveable { mutableStateOf(word.miscellaneousNote) }

    Row(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Header(title = stringResource(R.string.word_display_word_header))

            SelectionContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(ScrollState(0))
            ) {
                Text(
                    text = word.word,
                    modifier = Modifier
                        .height(80.dp)
                        .padding(15.dp),
                    fontSize = textFontSize
                )
            }

            Header(title = stringResource(R.string.word_display_meaning_header))

            SelectionContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
                    .verticalScroll(ScrollState(0))
            ) {
                Text(
                    text = word.meaning,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(15.dp),
                    fontSize = textFontSize
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
        )

        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            MiscellaneousNoteHeader(isEditNoteValid) {
                if (isEditNoteValid.value) {
                    val note = miscellaneousNote.value
                    val newWord =
                        word.copy(miscellaneousNote = note)

                    viewModel.insertWord(newWord)
                    restoredMiscellaneousNote = note

                    onConfirmClicked()
                }

                isEditNoteValid.value = !(isEditNoteValid.value)
            }

            SelectionContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(ScrollState(0))
            ) {
                MiscellaneousNoteTextField(
                    text = {
                        if (isEditNoteValid.value) {
                            OutlinedTextField(
                                value = miscellaneousNote.value,
                                onValueChange = {
                                    miscellaneousNote.value = it

                                    if (it != restoredMiscellaneousNote) {
                                        onEditNoteContentChanged(true)
                                    } else {
                                        onEditNoteContentChanged(false)
                                    }
                                },
                            )
                        } else {
                            Text(
                                text = miscellaneousNote.value,
                                modifier = Modifier.padding(15.dp),
                                fontSize = textFontSize
                            )
                        }
                    }
                )
            }

        }
    }
}

@Composable
private fun MiscellaneousNoteHeader(
    isEditNoteValid: MutableState<Boolean>,
    onButtonClick: () ->Unit
) {
    HeaderWithButton(
        title = stringResource(R.string.word_detail_miscellaneous_note_header),
        buttonText = getButtonText(isEditNoteValid)
    ) {
        onButtonClick()
    }
}

@Composable
fun getButtonText(isEditNoteValid: MutableState<Boolean>): String {
    return if (isEditNoteValid.value) {
        stringResource(R.string.word_detail_miscellaneous_note_confirm_button)
    } else {
        stringResource(R.string.word_detail_miscellaneous_note_edit_button)
    }
}

@Composable
private fun MiscellaneousNoteTextField(
    text: @Composable () -> Unit
) {
    text()
}


@Composable
private fun HeaderWithButton(
    title: String,
    titleColor: Color = MaterialTheme.colors.onPrimary,
    backgroundColor: Color = MaterialTheme.colors.primary,
    buttonText: String,
    buttonColor: Color = MaterialTheme.colors.primaryVariant,
    onButtonClicked: () -> Unit
) {
    Box {
        Header(
            title = title,
            titleTextColor = titleColor,
            backgroundColor = backgroundColor
        )

        TextButton(
            onClick = { onButtonClicked() },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Text(
                text = buttonText,
                modifier = Modifier
                    .background(buttonColor)
                    .padding(horizontal = 10.dp),
                color = titleColor
            )
        }
    }

}
