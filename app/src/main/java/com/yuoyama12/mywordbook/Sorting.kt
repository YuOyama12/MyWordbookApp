package com.yuoyama12.mywordbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

enum class Sorting {
    CreatedDate, ModifiedDate, Name
}

@Composable
fun <E: Enum<E>> Enum<E>.getString(): String {
    return when(this) {
        Sorting.CreatedDate -> stringResource(R.string.sorting_selection_field_created_date)
        Sorting.ModifiedDate -> stringResource(R.string.sorting_selection_field_modified_date)
        Sorting.Name -> stringResource(R.string.sorting_selection_field_name)
        else -> this.toString()
    }
}