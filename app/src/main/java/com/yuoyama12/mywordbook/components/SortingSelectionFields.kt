package com.yuoyama12.mywordbook.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yuoyama12.mywordbook.R
import com.yuoyama12.mywordbook.getString

private val fontSize = 16.sp

@Composable
fun <E: Enum<E>> SortingSelectionFields(
    sortingList: Array<E>,
    defaultSorting: Enum<E>,
    defaultIsDescOrder: Boolean,
    onOrderButtonClicked: (isDescOrder: Boolean) -> Unit,
    onSortingApplyClicked: (Enum<E>) -> Unit,
) {
    var selectedSortType by rememberSaveable { mutableStateOf(defaultSorting) }
    var isDescOrder by rememberSaveable { mutableStateOf(defaultIsDescOrder) }
    var openSortTypeSelectDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 30.dp)
            .shadow(1.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.sorting_selection_header),
            fontSize = fontSize
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    openSortTypeSelectDialog = true
                }
        ) {
            Text(
                text = selectedSortType.getString(),
                fontSize = fontSize,
                modifier = Modifier
                    .width(100.dp)
                    .padding(horizontal = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

        TextButton(
            onClick = {
                isDescOrder = !isDescOrder

                onOrderButtonClicked(isDescOrder)
            },
            shape = RectangleShape,
            border = BorderStroke(3.dp, MaterialTheme.colors.onPrimary)
        ) {
            Text(
                text = getOrderButtonText(
                    textWhenAsc = stringResource(R.string.sorting_selection_asc_button_text),
                    textWhenDesc = stringResource(R.string.sorting_selection_desc_button_text),
                    isDescOrder = isDescOrder
                ),
                fontSize = fontSize
            )
        }

        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
    }

    if (openSortTypeSelectDialog) {
        SingleSelectDialog(
            title = stringResource(R.string.dialog_sorting_selection_title),
            optionsList = sortingList.map { it.getString() },
            defaultPosition = sortingList.indexOf(selectedSortType),
            onDismissRequest = {
                openSortTypeSelectDialog = false
            },
            submitButtonText = stringResource(R.string.dialog_sorting_selection_submit_button)
        ) { position ->
            selectedSortType = sortingList[position]

            onSortingApplyClicked(selectedSortType)
        }

    }

}

private fun getOrderButtonText(
    textWhenAsc: String,
    textWhenDesc: String,
    isDescOrder: Boolean
) =
    if (isDescOrder) textWhenDesc else textWhenAsc