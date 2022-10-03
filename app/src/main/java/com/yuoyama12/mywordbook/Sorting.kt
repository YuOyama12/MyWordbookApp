package com.yuoyama12.mywordbook

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

enum class WordbookSorting {
    CreatedDate, ModifiedDate, Name
}

enum class WordSorting {
    CreatedDate, ModifiedDate, Word, Meaning
}

@Composable
fun <E: Enum<E>> Enum<E>.getString(): String {
    return when(this) {
        WordbookSorting.CreatedDate -> stringResource(R.string.sorting_selection_field_created_date)
        WordbookSorting.ModifiedDate -> stringResource(R.string.sorting_selection_field_modified_date)
        WordbookSorting.Name -> stringResource(R.string.sorting_selection_field_name)
        WordSorting.CreatedDate -> stringResource(R.string.sorting_selection_field_created_date)
        WordSorting.ModifiedDate -> stringResource(R.string.sorting_selection_field_modified_date)
        WordSorting.Word -> stringResource(R.string.sorting_selection_field_word)
        WordSorting.Meaning -> stringResource(R.string.sorting_selection_field_meaning)
        else -> throw Exception("An Enum class has one or more enum constants that have any strings!")
    }
}