package com.example.cardslistpoccompose.ui.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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

/** Fraction of list viewport height; slightly reduced so more room shows below the focused card. */
private const val CardHeightFraction = 0.51f

private val CardHeightMin = 240.dp
private val CardHeightMax = 392.dp

/** Vertical gutter between card slots (XML: clear gap, next card header not tucked under focus). */
private val ItemSpacing = 16.dp

private const val MIN_ALPHA = 0.5f
private const val MAX_TRANSLATE_FACTOR = 0.58f
private const val FocusLineFactor = 0.34f
private const val MIN_ALPHA_NEXT = 0.85f

/** XML: cards above the focus peek slightly narrower (wallet stack); focus and next stay full width. */
private const val BackCardScaleMin = 0.94f

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StackedTaskList(
    tasks: List<OnboardingTask>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onTaskClick: (OnboardingTask) -> Unit,
) {
    if (tasks.isEmpty()) return

    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier) {
        val cardHeight = (maxHeight * CardHeightFraction).coerceIn(CardHeightMin, CardHeightMax)

        val viewportHeightPx = with(density) { maxHeight.toPx() }
        val cardHeightPx = with(density) { cardHeight.toPx() }
        val maxTranslatePx = cardHeightPx * MAX_TRANSLATE_FACTOR

        val topPadding = with(density) {
            (viewportHeightPx * FocusLineFactor - cardHeightPx / 2f).coerceAtLeast(0f).toDp()
        }
        val bottomPadding = with(density) {
            (viewportHeightPx * (1f - FocusLineFactor) - cardHeightPx / 2f).coerceAtLeast(0f).toDp()
        }

        val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = topPadding, bottom = bottomPadding),
            verticalArrangement = Arrangement.spacedBy(ItemSpacing),
            flingBehavior = snapFlingBehavior,
        ) {
            itemsIndexed(
                items = tasks,
                key = { _, task -> task.id },
            ) { index, task ->
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

                val alphaBase =
                    (MIN_ALPHA + (1f - MIN_ALPHA) * (1f - absPos)).coerceIn(MIN_ALPHA, 1f)
                val alpha =
                    if (position > 0f) alphaBase.coerceAtLeast(MIN_ALPHA_NEXT) else alphaBase

                // Previous items: pull down behind focus. Next item: no upward translation —
                // avoids sliding the next card under the focus (looked "cut off").
                val translationYPx =
                    when {
                        position < 0f -> (-cardHeightPx * position).coerceIn(0f, maxTranslatePx)
                        else -> 0f
                    }

                val zIdx = (1f - absPos) * 10f

                val height = cardHeight

                val scaleX =
                    if (position < 0f) {
                        BackCardScaleMin +
                            (1f - BackCardScaleMin) * (1f - absPos).coerceIn(0f, 1f)
                    } else {
                        1f
                    }

                Box(
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .height(height)
                        .zIndex(zIdx)
                        .graphicsLayer {
                            this.scaleX = scaleX
                            scaleY = 1f
                            clip = false
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
