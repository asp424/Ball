package com.lm.ball

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircleProgress(
    type: ProgressCircleType,
    minSize: Float = 0f,
    mazSize: Float = 1f
) {
    val map = remember { mutableStateListOf<Boolean>()
        .apply { (-1 until 12).map { add(false) } } }
    var scale by remember { mutableStateOf(false) }
    var tick by remember { mutableStateOf(0) }
    var rotation by remember { mutableStateOf(0f) }
    val workState by rememberUpdatedState(true)
    var jobSize: Job? by remember { mutableStateOf(null) }
    val coroutine = rememberCoroutineScope()
    LaunchedEffect(workState) {
        if (workState) {
            jobSize?.cancel()
            jobSize = coroutine.launch(Dispatchers.IO) {
                oneSideWaveTimer(speed = type.speed, countItems = 11) { t, s ->
                    tick = t
                    scale = s
                    rotation += 1f
                }
            }
        } else jobSize?.cancel()
    }
    LaunchedEffect(tick, scale) { map[tick] = scale }
    repeat(12) {
        val float by animateFloatAsState(
            if (map[it]) minSize else mazSize, animationSpec = tween(type.speedResize))
        Canvas(modifier = Modifier
            .graphicsLayer {
                scaleX = float
                scaleY = float
                rotationZ = rotation
            }, onDraw = {
            drawCircle(
                color = Color.Black,
                center = Offset(
                    60 * cos(30 * it * 0.0174444444).toFloat(),
                    60 * sin(30 * it * 0.0174444444).toFloat()
                ), radius = 10f
            )
        })
    }
    DisposableEffect(true) { onDispose { jobSize?.cancel() } }
}

suspend fun oneSideWaveTimer(
    speed: Long,
    countItems: Int,
    call: (Int, Boolean) -> Unit
) {
    var tick = 0
    var side = 0

    while (true) {
        if (tick in 0..countItems)
            call(tick, when (side) {
                    0 -> true
                    1 -> false
                    2 -> true
                    3 -> false
                    else -> true
                }
            )
        delay(speed)
        when (side) {
            1 -> tick--
            2 -> tick--
        }
        when {
            tick == countItems && side == 0 -> { side = 2 }
            tick == -1 && side == 2 -> { side = 3 }

            tick == countItems && side == 3 -> {
                tick = -1
                side = 0
            }
        }

        when (side) {
            0 -> tick++
            3 -> tick++
        }
    }
}

sealed interface ProgressCircleType {
    val speed: Long
    val speedResize: Int

    object Atom : ProgressCircleType {
        override val speed: Long get() = 3
        override val speedResize: Int get() = 100
    }

    object Slow : ProgressCircleType {
        override val speed: Long get() = 100
        override val speedResize: Int get() = 500
    }

    object Middle : ProgressCircleType {
        override val speed: Long get() = 50
        override val speedResize: Int get() = 500
    }

    object Fast : ProgressCircleType {
        override val speed: Long get() = 30
        override val speedResize: Int get() = 500
    }

    object Random : ProgressCircleType {
        override val speed: Long get() = 10
        override val speedResize: Int get() = 1000
    }
}
