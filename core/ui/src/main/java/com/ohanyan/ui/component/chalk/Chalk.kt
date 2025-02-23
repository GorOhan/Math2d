package com.ohanyan.ui.component.chalk

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ohanyan.mathgame.ui.R

@Composable
fun Chalk(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier
            .padding(32.dp),
        painter = painterResource(R.drawable.ic_chalk),
        contentScale = ContentScale.FillWidth,
        contentDescription = null,
    )
}