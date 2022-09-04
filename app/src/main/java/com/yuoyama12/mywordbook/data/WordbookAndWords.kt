package com.yuoyama12.mywordbook.data

import androidx.room.Embedded
import androidx.room.Relation

data class WordbookAndWords (
    @Embedded
    val wordbook: Wordbook,

    @Relation(
        parentColumn = "id",
        entityColumn = "wordbook_id"
    )
    val word: List<Word>
)