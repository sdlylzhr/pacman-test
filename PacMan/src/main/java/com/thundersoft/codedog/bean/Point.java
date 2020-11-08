package com.thundersoft.codedog.bean;

import com.thundersoft.codedog.constants.Constant;

public class Point {

    private int x;
    private int y;
    private int direction;
    private int type;
    private int score;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = Constant.Direction.DIRECTION_NONE;
        this.type = 0;
        this.score = 0;
    }


    public Point(int x, int y, int direction, int type, int score) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.type = type;
        this.score = score;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                ", type=" + type +
                ", score=" + score +
                '}';
    }
}
