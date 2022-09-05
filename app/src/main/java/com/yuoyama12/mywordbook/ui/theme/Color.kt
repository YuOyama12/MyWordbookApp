package com.yuoyama12.mywordbook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.yuoyama12.mywordbook.R

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

@Composable
fun wordbookBackgroundColor() =
    if (isSystemInDarkTheme()) colorResource(R.color.gray_900)
    else colorResource(R.color.light_gray)

@Composable
fun wordbookBorderColor() =
    if (isSystemInDarkTheme()) colorResource(R.color.light_gray)
    else Color.DarkGray

