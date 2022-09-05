package com.yuoyama12.mywordbook.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <E> SimplePopupMenu(
    modifier: Modifier = Modifier,
    clickedItemContent: E,
    menuItems: Array<String>,
    onDismissRequest: () -> Unit,
    onMenuItemClicked: (
        index: Int,
        clickedItem: E
    ) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = { onDismissRequest() }
        ) {
            menuItems.forEachIndexed { index, menuItem ->
                DropdownMenuItem(
                    onClick = {
                        onMenuItemClicked(index, clickedItemContent)
                        onDismissRequest()
                    }
                ) {
                    Text(text = menuItem)
                }
            }
        }
    }

}