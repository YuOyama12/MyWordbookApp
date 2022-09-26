package com.yuoyama12.mywordbook.menu

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.yuoyama12.mywordbook.components.SimpleFlowRow

private val spacer = Modifier.padding(vertical = 4.dp)

@Composable
fun EditTagsDialog(
    title: String,
    message: String? = null,
    onDismissRequest: () -> Unit
) {
    var tagText by rememberSaveable { mutableStateOf("") }

    //TODO: 後程、InsertWordDialog(128行目)のタグリストと共に、dataStoreより引き出す処理を実装
    val tagsList  = mutableListOf("[名]", "[動]", "[形]", "[副]", "[前]", "[助動]")

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colors.surface
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

                    Spacer(spacer)

                    if (message != null) {
                        Text(
                            text = message,
                            modifier = Modifier
                                .padding(bottom = 14.dp)
                                .alpha(ContentAlpha.medium),
                            style = MaterialTheme.typography.body2
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .wrapContentHeight()
                            .height(IntrinsicSize.Max),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = tagText,
                            onValueChange = { tagText = it },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                //TODO: 追加処理と保存処理を入れる
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(1.dp, MaterialTheme.colors.primaryVariant)
                            .verticalScroll(ScrollState(0))
                    ) {
                        SimpleFlowRow(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            horizontalGap = 5.dp,
                            verticalGap = 5.dp
                        ) {
                            for (tag in tagsList) {
                                TagButton(
                                    tag = tag,
                                    onClearClicked = {
                                        tagsList.remove(it)
                                        //TODO: 削除処理と保存処理を入れる
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .weight(0.7f, false)
                            .align(Alignment.End),
                    ) {
                        TextButton(
                            onClick = {
                                //TODO: 追加処理と保存処理を入れる
                                onDismissRequest()
                            }
                        ) {
                            Text(stringResource(android.R.string.ok))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagButton(
    tag: String,
    onClearClicked: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .shadow(5.dp, CircleShape)
            .wrapContentSize()
            .background(shape = CircleShape, color = MaterialTheme.colors.surface)
            .border(width = 0.8.dp, color = Color.Gray, shape = CircleShape)
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tag,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f, false)
                    .padding(horizontal = 9.dp, vertical = 12.dp),
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                modifier = Modifier
                    .shadow(5.dp, CircleShape)
                    .size(24.dp)
                    .background(MaterialTheme.colors.primary, CircleShape)
                    .clickable { onClearClicked(tag) },
                imageVector = Icons.Rounded.Clear,
                contentDescription = null,
                tint = MaterialTheme.colors.surface
            )
        }
    }
}
