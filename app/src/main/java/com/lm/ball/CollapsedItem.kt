package com.lm.ball

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun CollapsedItem() {
    val itemHeight = LocalConfiguration.current.screenHeightDp.dp/3
    val state = rememberScrollState()

    var height by remember { mutableStateOf(itemHeight) }
    LaunchedEffect(state.value) {
        if (height <= itemHeight)
            height = itemHeight - state.value.dp / 3
    }
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(state), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Image(
            painter = painterResource(id = R.drawable.b), contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(
                LocalConfiguration.current.screenWidthDp.dp,
                height = height
            )
        )
        repeat(6) {
            Image(
                painter = painterResource(id = R.drawable.a), contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(
                    LocalConfiguration.current.screenWidthDp.dp,
                    height = itemHeight
                )
            )
        }
    }
}