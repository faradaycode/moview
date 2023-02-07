package com.movie.mandiri.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalLinearDecoration(var space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = space
//        outRect.bottom = space
        outRect.left = space
        outRect.right = space

        val itemCount = state.itemCount
        val itemPosition = parent.getChildAdapterPosition(view)

        if (itemPosition == 0) {
            //first
        }

        if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.bottom = space
        }
    }
}