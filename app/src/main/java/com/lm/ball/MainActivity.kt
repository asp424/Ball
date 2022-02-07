package com.lm.ball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlin.math.abs


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Ball() }
    }

    @Composable
    fun Ball() {
        val rectSide by remember { mutableStateOf(500f) }
        val ballRadius by remember { mutableStateOf(20f) }
        val startLocBallX by remember { mutableStateOf(rectSide / 2 - ballRadius) }
        var locBallX by remember { mutableStateOf(0f) }
        var locBallY by remember { mutableStateOf(0f) }
        var stepX by remember { mutableStateOf(5f) }
        var stepY by remember { mutableStateOf(5f) }
        LaunchedEffect(true) {
            while (true) {
                delay(10L)
                locBallX += stepX
                locBallY += stepY
                when (locBallX) {
                    0f -> {
                        locBallX = 0f
                        locBallY = 0f
                        stepY = abs(stepY)
                        stepX = abs(stepX)
                    }
                    startLocBallX -> stepY = -stepY
                    startLocBallX * 2 -> stepX = -stepX
                }

                if (locBallY == -startLocBallX) stepY = abs(stepY)
            }
        }

        Column(
            Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(modifier = Modifier, onDraw = {
                drawRect(
                    topLeft = Offset(-rectSide / 2, -rectSide / 2),
                    size = Size(rectSide, rectSide), color = Red
                )
            })
        }
        Column(
            Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(modifier = Modifier.graphicsLayer {
                translationX = locBallX
                translationY = locBallY
            }, onDraw = {
                drawCircle(
                    color = White, radius = ballRadius, center = Offset(-startLocBallX, 0f)
                )
            })
        }
    }
}