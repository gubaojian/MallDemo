package com.zhongpin.mvvm_android.common.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView间距Decoration
 */
class LinearItemDecoration(divider: Float) : RecyclerView.ItemDecoration() {

    private val divider = divider.toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = divider
        outRect.right = divider
        outRect.top = divider
        outRect.bottom = divider/2
    }
}