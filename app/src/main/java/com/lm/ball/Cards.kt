package com.lm.ball

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun Cards() {
    val width = LocalConfiguration.current.screenWidthDp.dp
    val height = LocalConfiguration.current.screenHeightDp.dp
    val colorBack by remember {
        mutableStateOf(Red)
    }
    var colorTop by remember {
        mutableStateOf(Black)
    }
    var colorMid by remember {
        mutableStateOf(Blue)
    }
    var isStarted by remember {
        mutableStateOf(false)
    }
    var isRotate by remember {
        mutableStateOf(false)
    }

    val coroutine = rememberCoroutineScope()
    val rotation2 by animateFloatAsState(
        targetValue = if (isRotate) 0f else 12f,
        animationSpec = tween(700)
    )
    var isSmall by remember {
        mutableStateOf(true)
    }
    var rot by remember {
        mutableStateOf(true)
    }
    val rotation1 by animateFloatAsState(
        targetValue = if (rot) 6f else 0f,
        animationSpec = tween(500)
    )
    var alphaC by remember {
        mutableStateOf(true)
    }
    val bottomCardY by animateFloatAsState(
        targetValue = if (isStarted) -height.value / 16 - 400f else if (isSmall) -height.value / 16 else 0f,
        animationSpec = tween(500)
    )
    val bottomCardX by animateFloatAsState(
        targetValue = if (isSmall) -16f else 0f,
        animationSpec = tween(500)
    )

    val bottomCardSize by animateFloatAsState(
        targetValue = if (isStarted) 1.3f else 1f,
        animationSpec = tween(500),
        finishedListener = {
            if (it == 1f) {
                rot = true
                alphaC = true
            }

            coroutine.launch {
                isStarted = false
                isSmall = false
                colorTop = colorBack
            }
        }
    )
    val alpha by animateFloatAsState(
        targetValue =
        if (alphaC) 1f else 0f,
        animationSpec = tween(500)

    )
    Box(
        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier
            .width(width = width / 2)
            .offset { IntOffset(bottomCardX.roundToInt(), bottomCardY.roundToInt()) }
            .rotate(rotation2)
            .height(height / 8)
            .graphicsLayer {
                scaleY = bottomCardSize
                scaleX = bottomCardSize
            }
            .background(colorBack, RoundedCornerShape(20.dp))) {}
        Box(modifier = Modifier
            .width(width = width / 2)
            .rotate(rotation1)
            .offset { IntOffset((-10f).roundToInt(), (-height.value / 30).roundToInt()) }
            .height(height / 8)
            .alpha(alpha)
            .background(colorMid, RoundedCornerShape(20.dp))) {}
        Box(modifier = Modifier
            .width(width = width / 2)
            .alpha(alpha)
            .offset { IntOffset(0f.roundToInt(), 0f.roundToInt()) }
            .height(height / 8)
            .pointerInput(Unit) {
                detectTapGestures {
                    isStarted = true
                    isRotate = true
                    alphaC = false
                    rot = false
                }
            }
            .background(colorTop, RoundedCornerShape(20.dp))) {}
    }
}

