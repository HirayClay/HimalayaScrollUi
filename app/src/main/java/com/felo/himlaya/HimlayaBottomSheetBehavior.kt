package com.felo.himlaya

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HimlayaBottomSheetBehavior : BottomSheetBehavior<View> {
    private val TAG = "HimlayaBottomSheetBehavior"
    private lateinit var headerView: View

    constructor() : super()
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        addBottomSheetCallback(StateChangeCallback())
    }

    /**
     * Header滚动的过程中，没有消耗的距离可以用来滚动 BottomSheet，这样就可以达到 Header滚动到底部时滚动 BottomSheet
     */
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        Log.i(
            TAG,
            "onNestedScroll:  ${child.javaClass.simpleName} ${target.javaClass.simpleName} $dyConsumed $dyUnconsumed ==${child.top}===${target.bottom}"
        )

        if (::headerView.isInitialized.not()) {
            headerView = target
        }

        if (dyUnconsumed >= 0) {
            // 有未消费的上滑距离,表明 Header内部已经滑到底了。
            // 此时如果 两者存在重叠，则应该向上平移 HeaderView

            if (headerView.bottom - dyUnconsumed >= child.top) {
                headerView.offsetTopAndBottom(-dyUnconsumed)
            } else {
                val destBottom =target.bottom - dyConsumed
                target.offsetTopAndBottom(-dyConsumed)
                child.offsetTopAndBottom(child.top - destBottom+1)
            }
        }
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        // 下滑的时候禁止内部滚动
        if (target is NestedScrollView) {
            // 往下滑，此时需要使用全部的距离,同时配合 preScroll消费掉全部的距离禁止内部滚动
            if (dy < 0) {
                if (target.top - dy < 0) {
                    headerView.offsetTopAndBottom(-dy)
                    consumed[1] = dy
                } else {
                    consumed[1] = -target.top
                    headerView.offsetTopAndBottom(-target.top)
                }
                // 往下滑动，如果没有达到 peek位置 ，bottomSheet 可以继续下滑
                val peekStateTop = coordinatorLayout.height - peekHeight
                if (child.top - dy > peekStateTop) {
                    val clampOffset = peekStateTop - child.top
                    child.offsetTopAndBottom(clampOffset)
                } else {
                    child.offsetTopAndBottom(-dy)
                }
            } else {
                // 上滑，如果处于连接处，可以跟着一起上滑,此时
                if (child.top >= target.bottom) {
                    child.offsetTopAndBottom(-dy)
                    consumed[1] = 0
                }
            }
        } else {
            Log.i(TAG, "IIIIIIIIIIIIIIIII ")
            if (::headerView.isInitialized) {
                // bottom sheet 已经下滑到 Header的底部，此时可以一起下滑
                if (dy < 0 && child.top >= headerView.bottom) {
                    if (headerView.top - dy < 0) {
                        headerView.offsetTopAndBottom(-dy)
                    } else {
                        headerView.offsetTopAndBottom(-headerView.top)
                    }
                }
            }
            // 如果是 bottom sheet部分自己的滚动，此时如果 header可以下滑，也必须跟着一起下滑
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }

        Log.i(
            TAG,
            "onNestedPreScroll: ${child.javaClass.simpleName} ${target.javaClass.simpleName} $dy(${consumed[1]})  "
        )

    }

    /**
     * 处理 BottomSheet自身的 fling
     */
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.i(TAG, "onNestedPreFling: ")
        return true
    }

    /**
     *  HeaderView fling 时候，消耗剩余的 fling
     */
    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        Log.i(TAG, "onNestedFling: ===" + consumed)
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
    }

    inner class StateChangeCallback : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
//            if (newState == BottomSheetBehavior.STATE_SETTLING) {
//                if (::headerView.isInitialized)
//                    headerView.offsetTopAndBottom(-headerView.top)
//            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

    }

}