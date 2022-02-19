package com.lm.ball

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("UnrememberedMutableState")
@Composable
fun Ddd() {
    var isStarted by remember { mutableStateOf(false) }
    var isRotate by remember { mutableStateOf(false) }
    val listCards = remember { mutableStateListOf(Red, Gray, Magenta, Yellow, Green) }
    var isSmall by remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()

    val rotation by animateFloatAsState(
        targetValue = if (isRotate) 0f else 6f,
        animationSpec = tween(700),
        finishedListener = { if (it == 0f) isSmall = true })

    val bottomCardY by animateFloatAsState(
        targetValue = if (isStarted) -600f else 0f,
        animationSpec = tween(700),
        finishedListener = {
            if (it == 0f) {
                coroutine.launch {
                    isSmall = false
                    listCards.replaceElements
                }
            }
            isStarted = false
        }
    )
    val size by animateFloatAsState(
        targetValue = if (isSmall) 0f else 1f,
        finishedListener = {
            if (it == 1f) {
                isRotate = false
            }
        })

    val scale by animateFloatAsState(
        targetValue = if (isStarted) 1.5f else 1f,
        animationSpec = tween(700)
    )

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        listCards.forEachIndexed { index, color ->
            when (index) {
                0 -> BoxCard(
                    listSize = listCards.size,
                    index = index,
                    rotation = rotation,
                    color = color,
                    scale = scale,
                    offsetY = bottomCardY
                )

                listCards.lastIndex -> BoxCard(
                    listSize = listCards.size,
                    index = index,
                    rotation = rotation,
                    color = color,
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures {
                                isStarted = !isStarted
                                isRotate = true
                            }
                        },
                    scale = size
                )

                else -> BoxCard(
                    listSize = listCards.size,
                    index = index,
                    rotation = rotation,
                    color = color,
                    scale = size
                )
            }
        }
    }
}

@Composable
fun BoxCard(
    listSize: Int,
    index: Int,
    rotation: Float,
    color: Color,
    scale: Float,
    modifier: Modifier = Modifier,
    offsetY: Float = 0f,
) {
    LocalConfiguration.current.apply {
        Box(
            modifier = modifier
                .width(width = screenWidthDp.dp / 2)
                .offset { IntOffset(0f.roundToInt(), offsetY.roundToInt()) }
                .height(screenHeightDp.dp / 6)
                .rotate((listSize - 1 - index) * rotation)
                .scale(scale)
                .background(color, RoundedCornerShape(20.dp)), contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (color) {
                    Red -> "5"
                    Gray -> "4"
                    Magenta -> "3"
                    Yellow -> "2"
                    Green -> "1"
                    else -> "1"
                }
            )
        }
    }
}

private val SnapshotStateList<Color>.replaceElements: SnapshotStateList<Color>
    get() = this.also { list ->
        takeLast(size - 2).also { tempList ->
            tempList.take(tempList.size).also { newList ->
                add(list[1])
                addAll(newList)
                add(list[0])
                removeRange(0, newList.size + 2)
            }
        }
    }