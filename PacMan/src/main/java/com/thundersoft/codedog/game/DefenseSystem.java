package com.thundersoft.codedog.game;

import com.thundersoft.codedog.bean.DefenseAction;
import com.thundersoft.codedog.bean.MapData;
import com.thundersoft.codedog.bean.Point;
import com.thundersoft.codedog.constants.Constant;

import java.util.ArrayList;
import java.util.List;


/**
 * 防御系统
 */
public class DefenseSystem {

    public List<DefenseAction> getDefenseActions(MapData mapData) {

        Point self = mapData.getSelf();
        List<Point> bullets = mapData.getBullets();
        List<Point> ghosts = mapData.getGhosts();
        List<Point> enemies = mapData.getEnemies();
        // 1. 遍历所有的敌人和ghost, 保留警戒内point
        List<Point> all = new ArrayList<>();
        all.addAll(ghosts);
        all.addAll(enemies);
        all.addAll(bullets);
        List<Point> dangerList = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            Point enemy = all.get(i);
            if (isInDangerRange(self, enemy)) {
                dangerList.add(enemy);
            }
        }
        System.out.println("all=====" + dangerList);
        // 2. 判断范围内的敌人方向
        List<Point> dList = new ArrayList<>();
        for (int i = 0; i < dangerList.size(); i++) {
            Point enemy = dangerList.get(i);
            if (isInDangerDirection(self, enemy)) {
                dList.add(enemy);
            }
        }
        System.out.println("danger==" + dList);
        // 3. 遍历危险目标，创建action
        List<DefenseAction> actions = new ArrayList<>();
        for (Point point : dList) {

            if (point.getType() == Constant.TYPE.GHOST) {
                DefenseAction defenseAction = ghostStrategy(self, point);
                actions.add(defenseAction);
            } else if (isPositiveDirection(self, point) && isInSameLine(self, point)) {
                DefenseAction defenseAction = new DefenseAction();
                defenseAction.setAttack(false);
                defenseAction.setDangerLevel(Constant.Danger.EXTREMELY_DANGER);
                defenseAction.setDirection(self.getDirection());
                actions.add(defenseAction);
            }
        }
        return actions;
    }

    private boolean isInDangerRange(Point self, Point enemy) {
        int dx = enemy.getX() - self.getX();
        int dy = enemy.getY() - self.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        System.out.println(distance);
        return distance <= 3;
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
        } else {
            defenseAction.setDangerLevel(Constant.Danger.NORMAL_DANGER);
        }
        defenseAction.setDirection(ghostDirection);
        defenseAction.setAttack(false);
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
