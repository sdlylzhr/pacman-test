package com.thundersoft.codedog.bean;

import java.util.ArrayList;
import java.util.List;

public class MapData {

    private String token;
    private Point self;
    private List<Point> enemies = new ArrayList<>();
    private List<Point> bullets = new ArrayList<>();
    private List<Point> fruits = new ArrayList<>();
    private List<Point> ghosts = new ArrayList<>();
    private List<Point> walls = new ArrayList<>();

    public void setMap(String[][] map) {
        this.map = map;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSelf(Point self) {
        this.self = self;
    }

    private String[][] map;

    public String getToken() {
        return token;
    }

    public List<Point> getGhosts() {
        return ghosts;
    }

    public List<Point> getWalls() {
        return walls;
    }

    public String[][] getMap() {
        return map;
    }

    public Point getSelf() {
        return self;
    }

    public List<Point> getEnemies() {
        return enemies;
    }

    public List<Point> getBullets() {
        return bullets;
    }

    public List<Point> getFruits() {
        return fruits;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "token='" + token + '\'' +
                ", self=" + self + "\n" +
                ", enemies=" + enemies + "\n" +
                ", bullets=" + bullets + "\n" +
//                ", fruits=" + fruits +
                ", ghosts=" + ghosts + "\n" +
                ", walls=" + walls +
//                ", map=" + Arrays.toString(map) +
                '}';
    }
}
