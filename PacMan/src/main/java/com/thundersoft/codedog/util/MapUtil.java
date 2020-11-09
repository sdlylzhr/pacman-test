package com.thundersoft.codedog.util;

import com.thundersoft.codedog.bean.MapData;
import com.thundersoft.codedog.bean.Point;
import com.thundersoft.codedog.constants.Constant;
import com.thundersoft.codedog.game.DefenseSystem;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtil {

    private MapData mapData = new MapData();

    public MapData getMapData() {
        return mapData;
    }

    // 单例
    private static class SingletonClassInstance {
        private static final MapUtil instance = new MapUtil();
    }

    private MapUtil() {
    }

    public static MapUtil getInstance() {
        return SingletonClassInstance.instance;
    }

    private static final String REG = "\\[MAP (.{2}) (.*)]\\[LOCATION .{2} (.*)]\\[SCORE .{2} (.*)]";

    public void setupData(String serverData, int size, int position) {
        mapData = new MapData();
        Pattern pattern = Pattern.compile(REG);
        Matcher matcher = pattern.matcher(serverData);
        String mapStr = "";
        String locationStr = "";
        String scoreStr = "";
        if (matcher.find()) {
            mapData.setToken(matcher.group(1));
            mapStr = matcher.group(2);
            locationStr = matcher.group(3);
            scoreStr = matcher.group(4);
        }
        System.out.println(mapStr);
        System.out.println(locationStr);
        int selfLocation = getSelfLocation(locationStr, position);
        if (selfLocation != -1) {
            mapExec(mapStr, size, selfLocation);
        } else {
            // todo:全部异常处理
        }
    }

    private int getSelfLocation(String locationStr, int position) {
        System.out.println(locationStr);
        String[] locations = locationStr.split(" ");

        for (String location : locations) {
            System.out.println(location);
        }
        return Integer.parseInt(locations[position]);
    }


//    public List<String> recycleViewDataSource(String mapStr) {
//        return Arrays.asList(mapStr.split(""));
//    }

//    public void resetMapData(){
//        mapData.getFruits().clear();
//        mapData.getWalls().clear();
//        mapData.getGhosts().clear();
//        mapData.getBullets().clear();
//        mapData.getEnemies().clear();
//    }

    private void mapExec(String mapStr, int size, int selfLocation) {
        String[][] map = new String[size][];
        byte[][] wallMap = new byte[size][];
        String[] singleWords = mapStr.split("");
        for (int i = 0; i < size; i++) {
            String[] temp = new String[size];
            byte[] wallRow = new byte[size];
            for (int k = 0; k < temp.length; k++) {
                String singleWord = singleWords[i * size + k];
                // 分类
                computePointData(singleWord, size, i * size + k, selfLocation);
                temp[k] = singleWord;
                wallRow[k] = isWallOrFruit(singleWord);
            }
            map[i] = temp;
            wallMap[i] = wallRow;
        }
        for (int i = 0; i < map.length; i++) {
            String[] temp = map[i];
            for (int k = 0; k < temp.length; k++) {
                System.out.print(temp[k] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < wallMap.length; i++) {
            byte[] temp = wallMap[i];
            for (int k = 0; k < temp.length; k++) {
                System.out.print(temp[k] + " ");
            }
            System.out.println();
        }
        mapData.setMap(map);
        mapData.setWallMap(wallMap);
    }

    private byte isWallOrFruit(String singleWord) {
        List<String> fruitWords = Arrays.asList("1", "2", "3", "4", "5");
        if ("9".equals(singleWord)) {
            return 1;
        } else if (fruitWords.contains(singleWord)) {
            return 2;
        } else {
            return 0;
        }
    }

    private void computePointData(String singleWord, int size, int index, int selfLocation) {

        int x = index % size;
        int y = index / size;

        List<String> fruitWords = Arrays.asList("1", "2", "3", "4", "5");
        List<String> playerWords = Arrays.asList("w", "s", "a", "d");
        List<String> bulletWords = Arrays.asList("^", "v", "<", ">");

        if (selfLocation == index) {
            Point point = new Point(x, y, playerWords.indexOf(singleWord) + 1, Constant.TYPE.PLAYER, 0);
            mapData.setSelf(point);
        } else if (fruitWords.contains(singleWord)) {
            Point point = new Point(x, y, Constant.Direction.DIRECTION_NONE, Constant.TYPE.FRUIT, Integer.parseInt(singleWord));
            mapData.getFruits().add(point);
        } else if (playerWords.contains(singleWord)) {
            int direction = playerWords.indexOf(singleWord) + 1;
            Point point = new Point(x, y, direction, Constant.TYPE.PLAYER, 0);
            mapData.getEnemies().add(point);
        } else if (bulletWords.contains(singleWord)) {
            int direction = bulletWords.indexOf(singleWord) + 1;
            Point point = new Point(x, y, direction, Constant.TYPE.BULLET, 0);
            mapData.getBullets().add(point);
        } else if ("G".equals(singleWord)) {
            Point point = new Point(x, y, Constant.Direction.DIRECTION_NONE, Constant.TYPE.GHOST, 0);
            mapData.getGhosts().add(point);
        } else if ("9".equals(singleWord)) {
            Point point = new Point(x, y, Constant.Direction.DIRECTION_NONE, Constant.TYPE.WALL, 0);
            mapData.getWalls().add(point);
        }
    }

    public static void main(String[] args) {

//        String mapStr = "[MAP A@ 52545542341325s3429311912493145519349943323355499w914211d594559332243243434421232434953194519145235532>13s991511G954129s314913^3191925424935123554592342259415154211142534295919a5955232135>5412251425221215199421d4323131415113w][LOCATION A@ 105 119 14 176 224 49 210 56][SCORE A@ 0 18 22 44 0 0 0 0]";

        String mapStr = "[MAP CV a4534414423352a311432412519351491323449191931449434195391454451154111425413349122324322934144513153433422439591931411295131434949125539G5213221934411411353552913351532393394245443129511225414313954233553531245434454931595131249>299159541422554111255123225195493333239193359154>95a1>^<21>4532141431195321223129544323252499559159125241434455543992351532992354s44199415121212515344114425293134332925352545959135292542434353113225231512119359413351533542429524994399442422411525523254495533911223359342933149432243259344429229921554954351533925312549599452449949142295529252331255212253521392222251393a452113912151555134131345122][LOCATION CV 14 279 357 0 597][SCORE CV 0 0 0 0 0]";
        MapUtil.getInstance().setupData(mapStr, 25, 1);

        MapData mapData = MapUtil.getInstance().getMapData();

        System.out.println(mapData);

        DefenseSystem ds = new DefenseSystem();
        System.out.println(ds.getDefenseActions(mapData));


    }

}
