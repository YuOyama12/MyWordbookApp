package com.yuoyama12.mywordbook.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoItemsNotification(
    modifier: Modifier = Modifier,
    image: Painter? = null,
    imageDesc: String? = null,
    color: Color = Color.Gray,
    message: String
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (image != null){
                Image(
                    painter = image,
                    modifier = Modifier.size(100.dp),
                    alignment = Alignment.Center,
                    contentDescription = imageDesc,
                    colorFilter = ColorFilter.tint(color)
                )
            }

            Text(
                text = message,
                color = color,
                fontSize = 25.sp
            )
        }
    }
}