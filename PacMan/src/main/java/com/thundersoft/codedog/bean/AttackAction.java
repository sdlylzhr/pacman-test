package com.thundersoft.codedog.bean;

public class AttackAction {

    private int direction;//开枪方向

    private boolean attack;//是否开枪

    private int hitProbability;//命中概率

    private float hitSorce;//命中后得分

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isAttack() {
        return attack;
    }

    public void setAttack(boolean attack) {
        this.attack = attack;
    }

    public int getHitProbability() {
        return hitProbability;
    }

    public void setHitProbability(int hitProbability) {
        this.hitProbability = hitProbability;
    }

    public float getHitSorce() {
        return hitSorce;
    }

    public void setHitSorce(float hitSorce) {
        this.hitSorce = hitSorce;
    }
}
