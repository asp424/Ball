package com.lm.ball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent { Ddd() }
    }

    @Composable
    fun Ball() {
        val rectSide by remember { mutableStateOf(500f) }
        val ballRadius by remember { mutableStateOf(20f) }
        val startLocBallX by remember { mutableStateOf(rectSide / 2 - ballRadius) }
        var locBallX by remember { mutableStateOf(0f) }
        var locBallY by remember { mutableStateOf(0f) }
        var stepX by remember { mutableStateOf(1f) }
        var stepY by remember { mutableStateOf(1f) }

        LaunchedEffect(ballRadius) {
            while (true) {
                delay(3L)
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


