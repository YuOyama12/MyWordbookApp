@file:OptIn(ExperimentalPagerApi::class)

package com.yuoyama12.mywordbook.ui.words

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.data.Word
import com.yuoyama12.mywordbook.datastore.DataStoreManager
import kotlinx.coroutines.launch

private val textFontSize = 20.sp
private val iconButtonColor = Color.White

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WordDisplayDialog(
    wordList: List<Word>,
    defaultIndex: Int = 0,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)
    val composableScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(initialPage = defaultIndex)

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                modifier = Modifier
                    .align(Alignment.End),
                onClick = { onDismissRequest() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = iconButtonColor
                )
            }

            HorizontalPager(
                count = wordList.size,
                state = pagerState
            ) { index ->
                val showMeaning = dataStoreManager.getWhetherShowMeaning().collectAsState(initial = true)

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(color = MaterialTheme.colors.surface)
                ) {
                    Header(title = stringResource(R.string.word_display_word_header))

                    SelectionContainer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(ScrollState(0))
                    ) {
                        Text(
                            text = wordList[index].word,
                            modifier = Modifier
                                .height(55.dp)
                                .padding(15.dp),
                            fontSize = textFontSize
                        )
                    }

                    Box {
                        Header(title = stringResource(R.string.word_display_meaning_header))

                        Icon(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(horizontal = 12.dp)
                                .clickable {
                                    composableScope.launch {
                                        dataStoreManager.storeWhetherShowMeaning(!showMeaning.value)
                                }
                            },
                            painter = getVisibilityIcon(showMeaning.value),
                            contentDescription = null,
                            tint = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primarySurface)
                        )
                    }

                    SelectionContainer(
                        modifier = Modifier
                            .fillMaxHeight(0.8f)
                            .fillMaxWidth()
                            .verticalScroll(ScrollState(0))
                    ) {
                        Text(
                            text = wordList[index].meaning,
                            color = getMeaningTextColor(showMeaning.value),
                            modifier = Modifier.padding(15.dp),
                            fontSize = textFontSize,
                        )
                    }

                }
            }

            Row(horizontalArrangement = Arrangement.Center) {
                IconButton(
                    onClick = {
                        composableScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_first_page),
                        contentDescription = null,
                        tint = iconButtonColor
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 18.dp))

                IconButton(
                    onClick = {
                        composableScope.launch {
                            goPreviousPage(pagerState)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_left),
                        contentDescription = null,
                        tint = iconButtonColor
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                IconButton(
                    onClick = {
                        composableScope.launch {
                            goNextPage(pagerState)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chevron_right),
                        contentDescription = null,
                        tint = iconButtonColor
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 18.dp))

                IconButton(
                    onClick = {
                        composableScope.launch {
                            pagerState.animateScrollToPage(wordList.lastIndex)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_last_page),
                        contentDescription = null,
                        tint = iconButtonColor
                    )
                }

            }
        }
    }
}

@Composable
fun Header(
    title: String,
    titleTextColor: Color = MaterialTheme.colors.onPrimary,
    backgroundColor: Color = MaterialTheme.colors.primary
) {
    Box(modifier = Modifier
        .background(backgroundColor)
        .fillMaxWidth()
        .wrapContentHeight()
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Center),
            color = titleTextColor
        )
    }
}

@Composable
private fun getMeaningTextColor(show: Boolean): Color {
    return if (show) {
        Color.Unspecified
    } else {
        MaterialTheme.colors.surface
    }
}

@Composable
private fun getVisibilityIcon(show: Boolean): Painter {
    return if (show) {
        painterResource(R.drawable.ic_visibility_off)
    } else {
        painterResource(R.drawable.ic_visibility)
    }
}

private suspend fun goPreviousPage(state: PagerState) {
    try {
        state.animateScrollToPage(state.currentPage - 1)
    } catch (e: IllegalArgumentException) { return }
}

private suspend fun goNextPage(state: PagerState) {
    state.animateScrollToPage(state.currentPage + 1)
}
