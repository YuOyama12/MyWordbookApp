package com.yuoyama12.mywordbook.ui.words

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.data.Word
import com.yuoyama12.mywordbook.datastore.DataStoreManager
import kotlinx.coroutines.runBlocking

private val Spacer = Modifier.padding(vertical = 4.dp)

@Composable
fun InsertWordDialog(
    title: String = "",
    message: String = "",
    wordbookId: Long,
    word: Word? = null,
    hasConsecutivelyAddButton: Boolean = true,
    positiveButtonText: String,
    onDialogClosed: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val wordViewModel: WordsViewModel = hiltViewModel()
    val dialogViewModel: WordDialogViewModel = hiltViewModel()

    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)

    val preInputtedWord = word?.word ?: ""
    val preInputtedMeaning = word?.meaning ?: ""

    var wordInfo by rememberSaveable {
        dialogViewModel.setPreInputtedWord(preInputtedWord)
        dialogViewModel.setInputtedWord(preInputtedWord)

        mutableStateOf(preInputtedWord)
    }

    //意味テキストの状態のみ画面回転時等に破棄されないようにするため
    //別に定義しておく。
    var meaningText by rememberSaveable {
        dialogViewModel.setPreInputtedMeaning(preInputtedMeaning)
        dialogViewModel.setInputtedMeaning(preInputtedMeaning)

        mutableStateOf(preInputtedMeaning)
    }
    var meaningInfo by remember {
        mutableStateOf(TextFieldValue( text = meaningText ))
    }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        val focusManager = LocalFocusManager.current

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface,
        ) {
            Box(
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(title)

                    Spacer(Spacer)

                    if (message != "") {
                        Text(
                            text = message,
                            modifier = Modifier
                                .padding(bottom = 14.dp)
                                .alpha(ContentAlpha.medium),
                            style = MaterialTheme.typography.body2
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = wordInfo,
                        onValueChange = {
                            wordInfo = it
                            dialogViewModel.setInputtedWord(it)
                        },
                        label = { Text(stringResource(R.string.dialog_add_word_label_word)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    )

                    Spacer(Spacer)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colors.surface, shape = CircleShape)
                            .border(width = 0.8.dp, color = Color.Gray, shape = CircleShape)
                    ) {
                        val tags = runBlocking { dataStoreManager.getWordTags() }

                        LazyRowToSelectTag(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color = MaterialTheme.colors.surface,
                                    shape = CircleShape),
                            tags = tags,
                        ) { tag ->
                            insertTagTextAfterCursor(
                                tag,
                                meaningInfo
                            ) { textWithTag, selection ->
                                meaningInfo = TextFieldValue(
                                    text = textWithTag,
                                    selection = selection
                                )

                                meaningText = textWithTag
                            }
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f)
                            .onFocusChanged {
                                if (!it.hasFocus) {
                                    val text = meaningInfo.text
                                    meaningInfo =
                                        TextFieldValue(
                                            text = text,
                                            selection = TextRange(text.length)
                                        )
                                }
                            },
                        value = meaningInfo,
                        onValueChange = {
                            meaningInfo = it
                            meaningText = it.text
                            dialogViewModel.setInputtedMeaning(it.text)
                        },
                        label = { Text(stringResource(R.string.dialog_add_word_label_meaning)) }
                    )

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        if (hasConsecutivelyAddButton) {
                            AddButton(
                                buttonText = stringResource(R.string.dialog_add_word_consecutively_add_button),
                                wordInfo = wordInfo,
                                meaningInfo = meaningInfo.text,
                                word = word,
                                onDismissRequest = onDialogClosed
                            ) { wordText, meaningText ->

                                insertWord(
                                    wordViewModel,
                                    word,
                                    wordbookId,
                                    wordText,
                                    meaningText
                                )

                                wordInfo = ""
                                meaningInfo = TextFieldValue()
                            }
                        }

                        TextButton(
                            onClick = { onDismissRequest() }
                        ) {
                            Text(stringResource(R.string.dialog_cancel))
                        }

                        AddButton(
                            buttonText = positiveButtonText,
                            wordInfo = wordInfo,
                            meaningInfo = meaningInfo.text,
                            word = word,
                            onDismissRequest = onDialogClosed
                        ) { wordText, meaningText ->

                            insertWord(
                                wordViewModel,
                                word,
                                wordbookId,
                                wordText,
                                meaningText
                            )

                            onDialogClosed()
                        }
                    }
                }
            }
        }


    }
}

@Composable
private fun LazyRowToSelectTag(
    modifier: Modifier = Modifier,
    tags: List<String>,
    onTagClicked: (String) -> Unit
) {
    LazyRow(
        modifier = modifier.padding(horizontal = 18.dp)
    ) {
        items(tags) { tag ->
            TextButton(
                modifier = Modifier
                    .padding(horizontal = 2.dp),
                shape = CircleShape,
                border = BorderStroke(0.8.dp, Color.Gray),
                onClick = {
                    onTagClicked(tag)
                }
            ) {
                Text(tag)
            }
        }
    }
}

@Composable
private fun AddButton(
    buttonText: String,
    wordInfo: String,
    meaningInfo: String,
    word: Word?,
    onDismissRequest: () -> Unit,
    onClicked: (word: String, meaning: String) -> Unit
) {
    TextButton(
        onClick = {
            if (wordInfo.trim() == (word?.word ?: "") &&
                meaningInfo.trim() == (word?.meaning ?: "")) {
                onDismissRequest()
                return@TextButton
            }

            onClicked(wordInfo, meaningInfo)
        }
    ) {
        Text(buttonText)
    }
}

private fun insertTagTextAfterCursor(
    tagText: String,
    textFieldValue: TextFieldValue,
    onTextFieldValueChange: (
        text: String,
        selection: TextRange
    ) -> Unit
) {
    val currentText = textFieldValue.text
    val currentCursorPosition = textFieldValue.selection.max

    val firstSplitText = currentText.substring(0, currentCursorPosition)
    val secondSplitText = currentText.substring(currentCursorPosition)

    val textWithTag = firstSplitText + tagText + secondSplitText
    val selection = TextRange((firstSplitText + tagText).length)

    onTextFieldValueChange(textWithTag, selection)
}


private fun insertWord(
    viewModel: WordsViewModel,
    originalWord: Word?,
    wordbookId: Long,
    wordText: String,
    meaningText: String
) {
    //更新するためのWordデータがなければ、新しくWordbookIdを用いて作成する
    val newWord =
        originalWord?.copy(
            word = wordText, meaning = meaningText, modifiedDate = viewModel.currentDate
        )
        ?: Word(
            wordbookId = wordbookId, word = wordText, meaning = meaningText, modifiedDate = viewModel.currentDate
        )

    viewModel.insertWord(newWord)
}