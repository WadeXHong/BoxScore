package com.wadexhong.boxscore.gameboxscore

import android.view.ViewConfiguration
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import kotlin.math.abs

enum class SwipeDirection { UP, DOWN, LEFT, RIGHT }

/**
 * Compose replacement for legacy `BSLinearLayout`.
 *
 * Once a second finger lands, every pointer change is consumed in the Initial pass so the
 * pager and buttons underneath ignore the gesture. Direction is decided from the first
 * finger's travel using the same thresholds as the legacy view, and [onSwipe] fires when all
 * fingers are lifted, with the maximum number of fingers seen during the gesture.
 * Single-finger touches pass through untouched.
 */
fun Modifier.multiFingerSwipe(
    onSwipe: (fingerCount: Int, direction: SwipeDirection) -> Unit
): Modifier = composed {
    val pagingSlop = ViewConfiguration.get(LocalContext.current).scaledPagingTouchSlop.toFloat()
    val currentOnSwipe by rememberUpdatedState(onSwipe)

    pointerInput(pagingSlop) {
        awaitEachGesture {
            val firstDown = awaitFirstDown(requireUnconsumed = false, pass = PointerEventPass.Initial)
            val start = firstDown.position
            var last = start
            var travelX = 0f
            var travelY = 0f
            var maxPointers = 1
            var direction: SwipeDirection? = null

            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                val pressedCount = event.changes.count { it.pressed }
                if (pressedCount > maxPointers) maxPointers = pressedCount
                if (maxPointers >= 2) event.changes.forEach { it.consume() }

                // Like the legacy view, only the original first finger decides the direction.
                event.changes.firstOrNull { it.id == firstDown.id && it.pressed }?.let { change ->
                    val position = change.position
                    travelX += abs(position.x - last.x)
                    travelY += abs(position.y - last.y)
                    last = position

                    val dx = position.x - start.x
                    val dy = position.y - start.y
                    if (travelX + travelY > 3f * pagingSlop && abs(dx) + abs(dy) > pagingSlop) {
                        if (travelX > 2f * travelY) {
                            if (dx > 0) direction = SwipeDirection.RIGHT
                            else if (dx < 0) direction = SwipeDirection.LEFT
                        } else if (travelX < 0.5f * travelY) {
                            if (dy < 0) direction = SwipeDirection.UP
                            else if (dy > 0) direction = SwipeDirection.DOWN
                        }
                    }
                }

                if (event.changes.none { it.pressed }) break
            }

            val swipe = direction
            if (maxPointers >= 2 && swipe != null) currentOnSwipe(maxPointers, swipe)
        }
    }
}
