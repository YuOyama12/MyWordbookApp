package com.yuoyama12.mywordbook.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SingleSelectDialog(
    title: String,
    optionsList: List<String>,
    defaultPosition: Int = 0,
    onDismissRequest: () -> Unit,
    submitButtonText: String,
    onSubmitButtonClick: (Int) -> Unit
) {
    var selectedItem by remember { mutableStateOf(defaultPosition) }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = title)

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f, false)
                ) {
                    items(optionsList) { option ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedItem == optionsList.indexOf(option),
                                onClick = {
                                    selectedItem = optionsList.indexOf(option)
                                }
                            )

                            Text(
                                text = option,
                                fontSize = 20.sp
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = {
                        onSubmitButtonClick.invoke(selectedItem)
                        onDismissRequest.invoke()
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = submitButtonText)
                }
            }

        }
    }
}