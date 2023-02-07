package com.movie.mandiri.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizonSpaceDecorator extends RecyclerView.ItemDecoration {
    private int space;

    public HorizonSpaceDecorator(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;

        int childCount = parent.getAdapter().getItemCount();
        if (parent.getChildAdapterPosition(view) == (childCount - 1)) {
            outRect.right = 0;
        }
    }
}
