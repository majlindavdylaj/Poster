package com.koddev.poster;

import android.widget.RelativeLayout;

public enum Position {

    CENTER(RelativeLayout.CENTER_IN_PARENT),
    TOP(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL),
    TOP_LEFT(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT),
    TOP_RIGHT(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_RIGHT),
    BOTTOM(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.CENTER_HORIZONTAL),
    BOTTOM_LEFT(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.ALIGN_PARENT_LEFT),
    BOTTOM_RIGHT(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.ALIGN_PARENT_RIGHT),
    LEFT(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.CENTER_VERTICAL),
    RIGHT(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL);

    private int pos1;
    private int pos2 = Integer.MIN_VALUE;

    Position(int pos1) {
        this.pos1 = pos1;
    }

    Position(int pos1, int pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public int getPos1() {
        return pos1;
    }

    public int getPos2() {
        return pos2;
    }
}
