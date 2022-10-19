package com.yuoyama12.mywordbook.ui.wordbooks

import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.PressGestureScope
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.WordbookSorting
import com.yuoyama12.mywordbook.components.*
import com.yuoyama12.mywordbook.data.Wordbook
import com.yuoyama12.mywordbook.datastore.DataStoreManager
import com.yuoyama12.mywordbook.menu.MultiOptionMenu
import com.yuoyama12.mywordbook.ui.theme.wordbookBackgroundColor
import com.yuoyama12.mywordbook.ui.theme.wordbookBorderColor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

private val wordbookNameFontSize = 30.sp
val visibilityToggleButtonFromEndOfParent = 8.dp

@Composable
fun WordbooksScreen(
    viewModel: WordbooksViewModel = hiltViewModel(),
    onWordbookClicked: (Wordbook) -> Unit
) {
    val context = LocalContext.current
    val dataStoreManager = DataStoreManager(context)
    val composableScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadWordbookAndWords()
    }

    var openOptionMenu by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    MultiOptionMenu(
                        expand = openOptionMenu,
                        iconClicked = { openOptionMenu = !openOptionMenu },
                        onDismissRequest = { openOptionMenu = false }
                    )
                }
            )
        },
        floatingActionButton = {
            var openAddDialog by remember { mutableStateOf(false) }

            FloatingActionButton(
                onClick = { openAddDialog = true },
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.fab_add_desc),
                )

                if (openAddDialog) {
                    SimpleInputDialog(
                        title = stringResource(R.string.dialog_add_wordbook_title),
                        textFieldHint = stringResource(R.string.dialog_add_wordbook_hint),
                        positiveButtonText = stringResource(R.string.dialog_add_wordbook_positive_button),
                        onDismissRequest = { openAddDialog = false }
                    ) { name ->
                        viewModel.addNewWordbook(name)
                    }
                }

            }
        }
    ) { padding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            var isSortingFieldsVisible by remember {
                mutableStateOf(
                    runBlocking { dataStoreManager.getWhetherShowSortingSelectionFields().first() }
                )
            }

            val (list, toggleButton, sortingFields) = createRefs()

            Box(
                modifier = Modifier.constrainAs(list) {
                    top.linkTo(toggleButton.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
            ) {
                if (viewModel.isWordbookEmpty) {
                    NoItemsNotification(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center),
                        image = painterResource(R.drawable.ic_no_items_in_list),
                        message = stringResource(R.string.no_wordbook_items_in_list_text)
                    )
                } else {
                    WordbookList(
                        viewModel = viewModel,
                        onWordbookClicked = onWordbookClicked
                    )
                }
            }

            VisibilityToggleButton(
                modifier = Modifier.constrainAs(toggleButton) {
                    top.linkTo(sortingFields.bottom)
                    end.linkTo(parent.end, visibilityToggleButtonFromEndOfParent)
                },
                isVisibilityOn = isSortingFieldsVisible,
                onClick = {
                    isSortingFieldsVisible = !isSortingFieldsVisible

                    composableScope.launch {
                        dataStoreManager.storeWhetherShowSortingSelectionFields(isSortingFieldsVisible)
                    }
                }
            )

            SortingSelectionFields(
                modifier = Modifier.constrainAs(sortingFields){},
                isVisible = isSortingFieldsVisible,
                sortingList = WordbookSorting.values(),
                defaultSorting = runBlocking {
                    WordbookSorting.valueOf(viewModel.wordbookSortingFlow.first())
                },
                defaultIsDescOrder = runBlocking {
                    viewModel.wordbookSortingOrderFlow.first()
                },
                onOrderButtonClicked = { isDescOrder ->
                    composableScope.launch {
                        dataStoreManager.storeWordbookSortingOrder(isDescOrder)
                    }
                },
                onSortingApplyClicked = { wordbookSorting ->
                    composableScope.launch {
                        dataStoreManager.storeWordbookSorting(wordbookSorting)
                    }
                }
            )

        }
    }
}

@Composable
private fun WordbookList(
    modifier: Modifier = Modifier,
    viewModel: WordbooksViewModel,
    onWordbookClicked: (Wordbook) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = viewModel.wordbookAndWordsList,
            key = { it.wordbook.id }
        ) { wordbookAndWords ->

            val context = LocalContext.current
            val interactionSource = remember { MutableInteractionSource() }
            var expandPopupMenu by remember { mutableStateOf(false) }

            var openRenameDialog by rememberSaveable { mutableStateOf(false) }
            var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

            Card (
                elevation = 8.dp,
                backgroundColor = wordbookBackgroundColor(),
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        top = 10.dp,
                        start = 30.dp,
                        end = 60.dp,
                        bottom = 10.dp
                    )
                    .border(
                        width = 1.2.dp,
                        color = wordbookBorderColor(),
                        shape = RectangleShape
                    )
                    .indication(interactionSource, LocalIndication.current)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                showRippleEffect(interactionSource, it)
                            },
                            onLongPress = {
                                expandPopupMenu = true
                            },
                            onTap = {
                                //名前を変更した場合、古い名前を含むデータを受け取るため。
                                val latestWordbook =
                                    viewModel.getLatestWordbookBy(wordbookAndWords.wordbook.id)

                                onWordbookClicked(latestWordbook)
                            }
                        )
                    }
            ) {
                Text(
                    text = wordbookAndWords.wordbook.name,
                    fontSize = wordbookNameFontSize,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                    fontWeight = FontWeight.SemiBold
                )

                if (expandPopupMenu) {
                    SimplePopupMenu(
                        modifier = Modifier
                            .wrapContentSize(Alignment.BottomEnd)
                            .offset(y = 20.dp),
                        clickedItemContent = wordbookAndWords,
                        menuItems = stringArrayResource(R.array.wordbook_list_popup_menu),
                        onDismissRequest = { expandPopupMenu = false }
                    ) { index, _ ->
                        when(index) {
                            0 -> { openRenameDialog = true }
                            1 -> { openDeleteDialog = true }
                        }
                    }
                }

                if (openRenameDialog) {
                    SimpleInputDialog(
                        title = stringResource(R.string.dialog_rename_title),
                        message = stringResource(R.string.dialog_rename_message),
                        preInputtedText = wordbookAndWords.wordbook.name,
                        textFieldHint = stringResource(R.string.dialog_rename_hint),
                        positiveButtonText = stringResource(R.string.dialog_rename_positive_button),
                        onDismissRequest = { openRenameDialog = false }
                    ) { newName ->
                        if (newName.trim() != "")
                            viewModel.renameWordbook(wordbookAndWords.wordbook, newName)
                    }
                }

                if (openDeleteDialog) {
                    val deleteCompleteMsg =
                        stringResource(R.string.delete_complete_message)

                    ConfirmationDialog (
                        title = stringResource(R.string.dialog_delete_confirmation_title),
                        message = stringResource(
                            R.string.dialog_delete_confirmation_message,
                            wordbookAndWords.wordbook.name
                        ),
                        positiveButtonText = stringResource(R.string.dialog_delete_confirmation_positive_button),
                        onDismissRequest = { openDeleteDialog = false }
                    ) {
                        viewModel.deleteWordbook(wordbookAndWords.wordbook)

                        Toast.makeText(context, deleteCompleteMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
}


suspend fun PressGestureScope.showRippleEffect(
    interactionSource: MutableInteractionSource,
    pressedOffset: Offset
) {
    val pressed = PressInteraction.Press(pressedOffset)
    interactionSource.emit(pressed)
    tryAwaitRelease()
    interactionSource.emit(PressInteraction.Release(pressed))

}