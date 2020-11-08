package com.thundersoft.codedog.constants;

public class Constant {

    public class Move {
        public static final int MOVE_FORWARD = 1; //前
        public static final int MOVE_BACKWARD = 2; //后
        public static final int MOVE_LEFT = 3; //左
        public static final int MOVE_RIGHT = 4; //右
        public static final int NO_MOVE = 5; //不动
    }

    public class Direction {
        public static final int DIRECTION_UP = 1; //上
        public static final int DIRECTION_DOWN = 2; //下
        public static final int DIRECTION_LEFT = 3; //左
        public static final int DIRECTION_RIGHT = 4; //右
        public static final int DIRECTION_NONE = 100; //没有方向
    }

    public class HIT {
        public static final int MUST_HIT = 1; //必中
        public static final int CAN_BE_OFFSET = 2; //可被对方攻击抵消
        public static final int CAN_BE_AVOIDED = 3; //可被躲避
    }

    public class Danger {
        public static final int EXTREMELY_DANGER = 1; //极度危险
        public static final int SEVERE_DANGER = 2; //比较危险
        public static final int NORMAL_DANGER = 3; //一般危险
        public static final int SAFTY = 4; //安全
    }

    public class TYPE {
        public static final int GHOST = 1; //鬼
        public static final int BULLET = 2; //炮弹
        public static final int PLAYER = 3; //玩家
        public static final int FRUIT = 4; //水果
        public static final int WALL = 5; //墙
    }

}
