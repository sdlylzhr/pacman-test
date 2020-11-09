package com.thundersoft.codedog.game;

import com.thundersoft.codedog.bean.DefenseAction;
import com.thundersoft.codedog.bean.MapData;
import com.thundersoft.codedog.bean.Point;
import com.thundersoft.codedog.constants.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 防御系统
 */
public class DefenseSystem {

    private byte[][] mWallMap;
    private Point mSelf;

    public List<DefenseAction> getDefenseActions(MapData mapData) {
        mSelf = mapData.getSelf();
        List<Point> mBullets = mapData.getBullets();
        List<Point> mGhosts = mapData.getGhosts();
        List<Point> mEnemies = mapData.getEnemies();
        mWallMap = mapData.getWallMap();
        List<DefenseAction> actions = new ArrayList<>();
//      1. 遍历所有的敌人和ghost, 保留警戒内point
        List<Point> all = new ArrayList<>();
        all.addAll(mGhosts);
        all.addAll(mEnemies);
        all.addAll(mBullets);
        List<Point> dangerList = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            Point enemy = all.get(i);
            if (isInDangerRange(mSelf, enemy)) {
                dangerList.add(enemy);
            }
        }
        System.out.println("all=====" + dangerList);
//      2. 按方向分类敌人
        List<Point> leftEnemies = new ArrayList<>();
        List<Point> topEnemies = new ArrayList<>();
        List<Point> rightEnemies = new ArrayList<>();
        List<Point> bottomEnemies = new ArrayList<>();
        Map<Integer, List<Point>> dangerDirectionPointsMap = new HashMap<>();
        for (Point enemy : dangerList) {
            int dx = enemy.getX() - mSelf.getX();
            int dy = enemy.getY() - mSelf.getY();
            double slope = Math.abs(dy * 1.0 / dx);
            if (slope >= 1 && dy < 0) {
                // 上方
                topEnemies.add(enemy);
            } else if (slope <= 1 && dx < 0) {
                // 左方
                leftEnemies.add(enemy);
            } else if (slope >= 1 && dy > 0) {
                // 下方
                bottomEnemies.add(enemy);
            } else if (slope <= 1 && dx > 0) {
                // 右方
                rightEnemies.add(enemy);
            }
        }
        dangerDirectionPointsMap.put(Constant.Direction.DIRECTION_UP, topEnemies);
        dangerDirectionPointsMap.put(Constant.Direction.DIRECTION_LEFT, leftEnemies);
        dangerDirectionPointsMap.put(Constant.Direction.DIRECTION_DOWN, bottomEnemies);
        dangerDirectionPointsMap.put(Constant.Direction.DIRECTION_RIGHT, rightEnemies);

        // 策略顺序: 前，后，左，右，不动
        for (int i = 0; i < 4; i++) {
            List<Point> dangerPoints = dangerDirectionPointsMap.get((mSelf.getDirection() - 1 + i) % 4 + 1);
            DefenseAction defenseAction = defaultStrategy(dangerPoints, i + 1);
            actions.add(defenseAction);
        }

        // 不动的策略（计步）
        DefenseAction defenseAction = notMoveStrategy(dangerList);
        actions.add(defenseAction);
//        List<Point> dList = new ArrayList<>();
//        for (int i = 0; i < dangerList.size(); i++) {
//            Point enemy = dangerList.get(i);
//            if (isInDangerDirection(mSelf, enemy)) {
//                dList.add(enemy);
//            }
//        }
//        System.out.println("danger==" + dList);
        return actions;
    }

    private DefenseAction notMoveStrategy(List<Point> dangerList) {
        DefenseAction defenseAction = new DefenseAction();
        defenseAction.setMove(Constant.Move.NO_MOVE);
        defenseAction.setAttack(false);
        Point nearestPoint = null;
        int minDistance = 10000;
        for (Point point : dangerList) {
            int dx = point.getX() - mSelf.getX();
            int dy = point.getY() - mSelf.getY();
            int distance = Math.abs(dx) + Math.abs(dy);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = point;
            }
        }
        if (minDistance > 3) {
            defenseAction.setDangerLevel(Constant.Danger.SAFTY);
        } else if (minDistance == 3) {
            defenseAction.setDangerLevel(Constant.Danger.NORMAL_DANGER);
        } else if (minDistance == 2) {
            defenseAction.setDangerLevel(Constant.Danger.SEVERE_DANGER);
        } else {
            defenseAction.setDangerLevel(Constant.Danger.EXTREMELY_DANGER);
            defenseAction.setAttack(true);
        }
        return defenseAction;
    }

    private DefenseAction defaultStrategy(List<Point> dangerList, int moveDirection) {
        DefenseAction defenseAction = new DefenseAction();
        defenseAction.setAttack(false);
        defenseAction.setDangerLevel(Constant.Danger.SAFTY);
        defenseAction.setMove(moveDirection);
        for (Point point : dangerList) {
            if (isInSameLine(mSelf, point)) {
                if (hasWallBetweenSameLineEnemy(point)) {
                    defenseAction.setDangerLevel(Constant.Danger.SAFTY);
                    break;
                }
                defenseAction.setDangerLevel(Constant.Danger.EXTREMELY_DANGER);
                break;
            } else {
                defenseAction.setDangerLevel(Constant.Danger.SEVERE_DANGER);
            }
        }
        return defenseAction;
    }

    private boolean isInDangerRange(Point self, Point enemy) {
        int dx = enemy.getX() - self.getX();
        int dy = enemy.getY() - self.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        System.out.println(distance);
        return distance <= 3;
    }

    private boolean hasWallBetweenSameLineEnemy(Point enemy) {
        if (mSelf.getX() == enemy.getX()) {
            int maxY = mSelf.getY() > enemy.getY() ? mSelf.getY() : enemy.getY();
            int minY = mSelf.getY() < enemy.getY() ? mSelf.getY() : enemy.getY();
            for (int i = minY; i <= maxY; i++) {
                if (mWallMap[mSelf.getX()][i] == 1) {
                    return true;
                }
            }
        } else if (mSelf.getY() == enemy.getY()) {
            int maxX = mSelf.getX() > enemy.getX() ? mSelf.getX() : enemy.getX();
            int minX = mSelf.getX() < enemy.getX() ? mSelf.getX() : enemy.getX();
            for (int i = minX; i <= maxX; i++) {
                if (mWallMap[i][mSelf.getY()] == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInDangerDirection(Point self, Point enemy) {
        int dx = enemy.getX() - self.getX();
        int dy = enemy.getY() - self.getY();
        double slope = Math.abs(dy * 1.0 / dx);
        if (slope > 1 && dy < 0) {
            // 上方
            if (enemy.getDirection() == Constant.Direction.DIRECTION_DOWN) {
                return true;
            }
        } else if (slope < 1 && dx < 0) {
            // 左方
            if (enemy.getDirection() == Constant.Direction.DIRECTION_RIGHT) {
                return true;
            }
        } else if (slope > 1 && dy > 0) {
            // 下方
            if (enemy.getDirection() == Constant.Direction.DIRECTION_UP) {
                return true;
            }
        } else if (slope < 1 && dx > 0) {
            // 右方
            if (enemy.getDirection() == Constant.Direction.DIRECTION_LEFT) {
                return true;
            }
        }
        return false;
    }

    private boolean isPositiveDirection(Point self, Point enemy) {
        int i = self.getDirection() + enemy.getDirection();
        return i == 3 || i == 7;
    }

    // 在同一个横行，竖行，正斜行
    private boolean isInSameLine(Point self, Point enemy) {
        int dx = enemy.getX() - self.getX();
        int dy = enemy.getY() - self.getY();
        return dx == 0 || dy == 0 || dx == dy;
    }

    // 鬼策略:鬼在哪个方向，就把哪个方向的危险等级提高；如果自己面向鬼，那么就极度危险
    private DefenseAction ghostStrategy(Point self, Point ghost) {
        DefenseAction defenseAction = new DefenseAction();
        int dx = ghost.getX() - self.getX();
        int dy = ghost.getY() - self.getY();
        int ghostDirection = directionOfEnemy(self, ghost);
        if (self.getDirection() == ghostDirection) {
            defenseAction.setDangerLevel(Constant.Danger.EXTREMELY_DANGER);
            defenseAction.setAttack(true);
        } else {
            defenseAction.setDangerLevel(Constant.Danger.NORMAL_DANGER);
            defenseAction.setAttack(false);
        }
        defenseAction.setMove(ghostDirection);
        return defenseAction;
    }

    // 敌人在自己的哪个方向
    private int directionOfEnemy(Point self, Point enemy) {
        int dx = enemy.getX() - self.getX();
        int dy = enemy.getY() - self.getY();
        double slope = Math.abs(dy * 1.0 / dx);
        if (slope >= 1 && dy < 0) {
            // 上方
            return Constant.Direction.DIRECTION_UP;
        } else if (slope < 1 && dx < 0) {
            // 左方
            return Constant.Direction.DIRECTION_LEFT;
        } else if (slope >= 1 && dy > 0) {
            // 下方
            return Constant.Direction.DIRECTION_DOWN;
        } else {
            // 右方
            return Constant.Direction.DIRECTION_RIGHT;
        }
    }

}
