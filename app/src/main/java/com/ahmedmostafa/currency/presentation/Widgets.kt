package com.ahmedmostafa.currency.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ahmedmostafa.currency.ui.theme.Green

@Composable
fun LoadingIndicator(isVisible: Boolean, size: Dp = 90.dp) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            contentAlignment = Alignment.Center,

            ) {
            CircularProgressIndicator(
                modifier = Modifier.size(size),
                strokeWidth = 4.dp, color = Green
            )
        }
    }
}