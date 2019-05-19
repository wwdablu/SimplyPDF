package com.wwdablu.soumya.simplypdf.composers.models.cell;

public abstract class Cell {

    public int width;
    public int horizontalPadding;
    public int verticalPadding;

    Cell() {
        horizontalPadding = 10;
        verticalPadding = 10;
    }

    public int getHorizontalPadding() {
        return horizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        this.horizontalPadding = horizontalPadding;
    }

    public int getVerticalPadding() {
        return verticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        this.verticalPadding = verticalPadding;
    }
}
