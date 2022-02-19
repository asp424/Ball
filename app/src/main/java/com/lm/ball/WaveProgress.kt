package com.lm.ball

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LinearProgress(
    type: ProgressLinearType,
    countItems: Int = 4,
    work: Boolean,
    minSize: Float = 0f,
    mazSize: Float = 1f,
    content: @Composable (BoxScope) -> Unit
) {
    val map =
        remember { mutableStateListOf<Boolean>().apply { (-1 until countItems).map { add(false) } } }
    var scale by remember { mutableStateOf(false) }
    var tick by remember { mutableStateOf(0) }
    val workState by rememberUpdatedState(newValue = work)
    var job: Job? by remember { mutableStateOf(null) }
    val coroutine = rememberCoroutineScope()

    LaunchedEffect(workState) {
        if (workState) {
            job?.cancel()
            job =
                coroutine.launch(Dispatchers.IO) {
                    waveTimer(speed = type.speed, countItems = countItems - 1) { t, s ->
                        tick = t
                        scale = s
                    }
                }
        } else {
            job?.cancel()
        }
    }
    LaunchedEffect(tick, scale) { map[tick] = scale }
    Column(
        Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center, verticalAlignment = CenterVertically
        ) {
            repeat(countItems) {
                val float by animateFloatAsState(
                    if (map[it]) minSize else mazSize,
                    animationSpec = tween(type.speedResize)
                )
                Box(
                    Modifier
                        .scale(float)
                        .padding(end = 20.dp),
                    contentAlignment = Alignment.Center) { content(this) }
            }
        }
    }
    DisposableEffect(true) {
        onDispose {
            job?.cancel()
        }
    }
}


suspend fun waveTimer(
    speed: Long,
    countItems: Int,
    call: (Int, Boolean) -> Unit
) {
    var tick = 0
    var side = 0
    while (true) {
        if (tick in 0..countItems)
            call(
                tick, when (side) {
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
            tick == countItems && side == 0 -> {
                delay(speed * 4)
                side = 1
            }
            tick == -1 && side == 1 -> {
                delay(speed * 2)
                tick = countItems
                side = 2
            }
            tick == -1 && side == 2 -> {
                delay(speed * 4)
                side = 3
            }
            tick == countItems && side == 3 -> {
                delay(speed * 2)
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

sealed interface ProgressLinearType {
    val speed: Long
    val speedResize: Int

    object Atom : ProgressLinearType {
        override val speed: Long
            get() = 3
        override val speedResize: Int
            get() = 100

    }

    object Slow : ProgressLinearType {
        override val speed: Long
            get() = 100
        override val speedResize: Int
            get() = 500
    }

    object Middle : ProgressLinearType {
        override val speed: Long
            get() = 50
        override val speedResize: Int
            get() = 500
    }

    object Fast : ProgressLinearType {
        override val speed: Long
            get() = 30
        override val speedResize: Int
            get() = 500
    }

    object Random : ProgressLinearType {
        override val speed: Long
            get() = 10
        override val speedResize: Int
            get() = 1000
    }
}














