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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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

private val Spacer = Modifier.padding(vertical = 4.dp)

@Composable
fun InsertWordDialog(
    title: String = "",
    message: String = "",
    wordbookId: Long,
    word: Word? = null,
    hasConsecutivelyAddButton: Boolean = true,
    positiveButtonText: String,
    onDismissRequest: () -> Unit
) {
    val viewModel: WordsViewModel = hiltViewModel()

    val preInputtedWord = word?.word ?:""
    val preInputtedMeaning = word?.meaning ?: ""

    var wordInfo by rememberSaveable { mutableStateOf(preInputtedWord) }

    //意味テキストの状態のみ画面回転時等に破棄されないようにするため
    //別に定義しておく。
    var meaningText by rememberSaveable { mutableStateOf(preInputtedMeaning) }
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
                    vertical = 16.dp,
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
                        onValueChange = { wordInfo = it },
                        label = { Text("単語") },
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
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(shape = CircleShape, color = MaterialTheme.colors.surface)
                            .border(width = 0.8.dp, color = Color.Gray, shape = CircleShape)
                    ) {
                        val tags = listOf("[名]", "[動]", "[形]", "[副]", "[前]", "[助動]")
                        LazyRowToSelectTag(
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
                            .fillMaxHeight(0.65f)
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
                        },
                        label = { Text("意味") }
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
                                preInputtedWord = preInputtedWord,
                                preInputtedMeaning = preInputtedMeaning,
                                onDismissRequest = onDismissRequest
                            ) { wordText, meaningText ->

                                insertWord(
                                    viewModel,
                                    word,
                                    wordbookId,
                                    wordText,
                                    meaningText
                                )

                                wordInfo = ""
                                meaningInfo = TextFieldValue()
                            }
                        }

                        TextButton(onClick = { onDismissRequest() }) {
                            Text(stringResource(R.string.dialog_cancel))
                        }

                        AddButton(buttonText = positiveButtonText,
                            wordInfo = wordInfo,
                            meaningInfo = meaningInfo.text,
                            preInputtedWord = preInputtedWord,
                            preInputtedMeaning = preInputtedMeaning,
                            onDismissRequest = onDismissRequest
                        ) { wordText, meaningText ->

                            insertWord(
                                viewModel,
                                word,
                                wordbookId,
                                wordText,
                                meaningText
                            )

                            onDismissRequest()
                        }

                    }

                }
            }
        }
    }
}

@Composable
private fun LazyRowToSelectTag(
    tags: List<String>,
    onTagClicked: (String) -> Unit
) {

    LazyRow(modifier = Modifier.padding(horizontal = 18.dp)) {
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
    preInputtedWord: String,
    preInputtedMeaning: String,
    onDismissRequest: () -> Unit,
    onClicked: (word: String, meaning: String) -> Unit
) {
    TextButton(
        onClick = {
            if (wordInfo.trim() == preInputtedWord &&
                meaningInfo.trim() == preInputtedMeaning) {
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