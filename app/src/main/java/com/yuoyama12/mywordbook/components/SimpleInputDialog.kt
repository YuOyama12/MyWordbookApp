package com.yuoyama12.mywordbook.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yuoyama12.mywordbook.R

private val Spacer = Modifier.padding(vertical = 3.dp)
private val MessagePadding = Modifier.padding(bottom = 28.dp)

@Composable
fun SimpleInputDialog(
    title: String,
    message: String = "",
    preInputtedText:String = "",
    textFieldHint: String,
    positiveButtonText: String,
    onDismissRequest: () -> Unit,
    onPositiveButtonClicked: (String) -> Unit
) {
    var inputtedText by rememberSaveable { mutableStateOf(preInputtedText) }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Box (
                modifier = Modifier.padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Spacer(Modifier.padding(vertical = 8.dp))

                    Text(
                        text = title,
                        modifier = Modifier.alpha(ContentAlpha.high),
                        style = MaterialTheme.typography.subtitle1
                    )

                    Spacer(Spacer)

                    if (message != "") {
                        Text(
                            text = message,
                            modifier = MessagePadding.alpha(ContentAlpha.medium),
                            style = MaterialTheme.typography.body2
                        )

                        Spacer(Spacer)
                    }

                    OutlinedTextField(
                        value = inputtedText,
                        onValueChange = { inputtedText = it },
                        placeholder = { Text(textFieldHint) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (inputtedText.trim() != preInputtedText) {
                                    onPositiveButtonClicked(inputtedText)
                                }
                                onDismissRequest()
                            }
                        )
                    )

                    Spacer(Spacer)

                    Row(
                        modifier = Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TextButton(onClick = { onDismissRequest() }) {
                            Text(stringResource(R.string.dialog_cancel))
                        }
                        TextButton(
                            onClick = {
                                if (inputtedText.trim() != preInputtedText) {
                                    onPositiveButtonClicked(inputtedText)
                                }
                                onDismissRequest()
                            }
                        ) {
                            Text(positiveButtonText)
                        }
                    }
                }
            }
        }
    }
}