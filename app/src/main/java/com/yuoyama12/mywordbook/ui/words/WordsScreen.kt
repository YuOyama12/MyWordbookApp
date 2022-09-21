package com.yuoyama12.mywordbook.ui.words

import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.components.ConfirmationDialog
import com.yuoyama12.mywordbook.components.NoItemsNotification
import com.yuoyama12.mywordbook.components.SimplePopupMenu
import com.yuoyama12.mywordbook.components.SingleSelectDialog
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.ui.theme.wordCardBackgroundColor
import com.yuoyama12.mywordbook.ui.theme.wordbookBorderColor
import com.yuoyama12.mywordbook.ui.wordbooks.WordbooksViewModel
import com.yuoyama12.mywordbook.ui.wordbooks.showRippleEffect

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
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (viewModel.isWordsListEmpty) {
                    NoItemsNotification(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        image = painterResource(R.drawable.ic_no_items_in_list),
                        message = stringResource(R.string.no_word_items_in_list_text)
                    )
                } else {
                    WordsList(
                        viewModel = viewModel
                    )
                }
            }

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
            items = viewModel.words,
            key = { it.id }
        ) { word ->

            val context = LocalContext.current
            val interactionSource = remember { MutableInteractionSource() }
            var expandPopupMenu by remember { mutableStateOf(false) }

            var openWordDisplayDialog by rememberSaveable { mutableStateOf(false) }
            var openEditDialog by rememberSaveable { mutableStateOf(false) }
            var openSelectWordbookDialog by rememberSaveable { mutableStateOf(false) }
            var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

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
                    .indication(interactionSource, LocalIndication.current)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { showRippleEffect(interactionSource, it) },
                            onLongPress = { expandPopupMenu = true },
                            onTap = { openWordDisplayDialog = true }
                        )
                    }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = word.word,
                        fontSize = fontSize,
                        modifier = Modifier
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

                if (openWordDisplayDialog) {
                    val wordlist = viewModel.words

                    WordDisplayDialog(
                        wordList = wordlist,
                        defaultIndex = wordlist.indexOf(word),
                        onDismissRequest = { openWordDisplayDialog = false }
                    )
                }

                if (expandPopupMenu) {
                    SimplePopupMenu(
                        modifier = Modifier
                            .wrapContentSize(Alignment.BottomEnd)
                            .offset(y = (-20).dp),
                        clickedItemContent = word,
                        menuItems = stringArrayResource(R.array.word_list_popup_menu),
                        onDismissRequest = { expandPopupMenu = false }
                    ) { index, _ ->
                           when(index) {
                               0 -> { openEditDialog = true }
                               1 -> { openSelectWordbookDialog = true }
                               2 -> { openDeleteDialog = true }
                           }
                    }
                }

                if (openEditDialog) {
                    InsertWordDialog(
                        title = stringResource(R.string.dialog_edit_word_title),
                        wordbookId = word.wordbookId,
                        word = word,
                        hasConsecutivelyAddButton = false,
                        positiveButtonText = stringResource(R.string.dialog_edit_word_positive_button),
                        onDismissRequest = { openEditDialog = false }
                    )
                }

                if (openSelectWordbookDialog) {
                    val wordbookViewModel: WordbooksViewModel = hiltViewModel()
                    val wordbookList = wordbookViewModel.fetchWordbooksAndWords().mapNotNull {
                        if (it.wordbook.id != word.wordbookId) it else null
                    }

                    if (wordbookList.isEmpty()) {
                        openSelectWordbookDialog = false
                        Toast.makeText(
                            context,
                            stringResource(R.string.no_other_wordbooks_to_move_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        SingleSelectDialog(
                            title = stringResource(R.string.dialog_select_other_wordbook_title),
                            optionsList = wordbookList.map { it.wordbook.name },
                            onDismissRequest = { openSelectWordbookDialog = false },
                            submitButtonText = stringResource(R.string.dialog_select_other_wordbook_submit_button)
                        ) { position ->
                            val selectedWordbook = wordbookList[position].wordbook

                            viewModel.moveWordToOtherWordbook(word, selectedWordbook)
                        }
                    }
                }

                if (openDeleteDialog) {
                    val deleteCompleteMsg =
                        stringResource(R.string.delete_complete_message)

                    ConfirmationDialog(
                        title = stringResource(R.string.dialog_delete_confirmation_title),
                        message = stringResource(R.string.dialog_delete_confirmation_message_no_name),
                        positiveButtonText = stringResource(R.string.dialog_delete_confirmation_positive_button),
                        onDismissRequest = { openDeleteDialog = false }
                    ) {
                        viewModel.deleteWord(word)

                        Toast.makeText(context, deleteCompleteMsg, Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }
}