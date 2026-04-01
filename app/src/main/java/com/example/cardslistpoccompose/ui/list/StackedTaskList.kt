package com.example.cardslistpoccompose.ui.list

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.cardslistpoccompose.model.OnboardingTask
import kotlin.math.abs

private val CardExpandedHeight = 280.dp
private val CardCollapsedHeight = 88.dp

private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f
private const val MAX_TRANSLATE_FACTOR = 0.55f
private val NextPeek = 22.dp
private const val FocusLineFactor = 0.35f
private const val MIN_ALPHA_NEXT = 0.8f

@Composable
fun StackedTaskList(
    tasks: List<OnboardingTask>,
    modifier: Modifier = Modifier,
    onTaskClick: (OnboardingTask) -> Unit,
) {
    if (tasks.isEmpty()) return

    val listState = rememberLazyListState()
    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        val verticalPadding = ((maxHeight - CardExpandedHeight) / 2).coerceAtLeast(0.dp)
        val cardHeightPx = with(density) { CardExpandedHeight.toPx() }
        val maxTranslatePx = cardHeightPx * MAX_TRANSLATE_FACTOR
        val nextPeekPx = with(density) { NextPeek.toPx() }

        val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = verticalPadding),
            flingBehavior = snapFlingBehavior,
        ) {
            itemsIndexed(tasks, key = { _, task -> task.id }) { index, task ->
                val position by remember {
                    derivedStateOf {
                        val info = listState.layoutInfo
                        val viewportHeight =
                            (info.viewportEndOffset - info.viewportStartOffset).toFloat()
                        val focusLinePx =
                            info.viewportStartOffset + (viewportHeight * FocusLineFactor)
                        val itemInfo =
                            info.visibleItemsInfo.firstOrNull { it.index == index }
                        if (itemInfo != null && itemInfo.size > 0) {
                            val itemCenter = itemInfo.offset + itemInfo.size / 2f
                            (itemCenter - focusLinePx) / itemInfo.size.toFloat()
                        } else {
                            2f
                        }
                    }
                }

                val absPos = abs(position).coerceAtMost(1f)

                val scale = (MIN_SCALE + (1f - MIN_SCALE) * (1f - absPos))
                    .coerceIn(MIN_SCALE, 1f)

                val scaleNorm = ((scale - MIN_SCALE) / (1f - MIN_SCALE)).coerceIn(0f, 1f)
                val alphaBase = (MIN_ALPHA + (1f - MIN_ALPHA) * scaleNorm)
                    .coerceIn(MIN_ALPHA, 1f)
                val alpha =
                    if (position > 0f) alphaBase.coerceAtLeast(MIN_ALPHA_NEXT) else alphaBase

                // Vertical stack:
                // - previous cards (position < 0) are pulled DOWN behind the focused card
                //   (equivalent to translationY = -cardHeight * position)
                // - next card (position > 0) only peeks slightly (depth cue)
                val translationYPx =
                    when {
                        // previous: stack behind focused (downwards)
                        position < 0f -> (-cardHeightPx * position).coerceIn(0f, maxTranslatePx)
                        // next: pull UP a bit to create a consistent peek
                        position > 0f -> (-position * nextPeekPx).coerceIn(-nextPeekPx, 0f)
                        else -> 0f
                    }

                // Focused on top; previous cards just behind focus; next card below everything.
                val zIdx =
                    when {
                        absPos < 0.0001f -> 10f
                        position < 0f -> 5f - absPos
                        else -> -1f - absPos
                    }

                // Layout slot is fixed to keep \"1 card in focus\" behavior stable.
                // (Completed cards keep their internal UI collapsed, but the slot stays fixed.)
                val height = CardExpandedHeight

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height)
                        .zIndex(zIdx)
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            this.translationY = translationYPx
                        },
                ) {
                    TaskCard(
                        task = task,
                        height = height,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onTaskClick(task) },
                    )
                }
            }
        }
    }
}
