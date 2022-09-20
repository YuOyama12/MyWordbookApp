package com.yuoyama12.mywordbook.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.yuoyama12.mywordbook.R

@Composable
fun ConfirmationDialog(
    title: String,
    message: String? = null,
    positiveButtonText: String = stringResource(android.R.string.ok),
    onDismissRequest: () -> Unit,
    onPositiveClicked: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = title) },
        text = { Text(text = message ?: "") },
        confirmButton = {
            TextButton(
                onClick = {
                    onPositiveClicked()
                    onDismissRequest()
                }
            ) {
                Text(text = positiveButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismissRequest() }
            ) {
                Text(text = stringResource(R.string.dialog_cancel))
            }
        }

    )
}