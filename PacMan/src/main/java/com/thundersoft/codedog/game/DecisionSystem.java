package com.thundersoft.codedog.game;

import com.thundersoft.codedog.bean.MapData;
import com.thundersoft.codedog.bean.SummaryAction;

/**
 * 行为决策系统
 */
public class DecisionSystem {

    private DefenseSystem mDefenseSystem;
    private ScoreSystem mScoreSystem;
    private AttackSystem mAttackSystem;

    public DecisionSystem() {
        mDefenseSystem = new DefenseSystem();
        mScoreSystem = new ScoreSystem();
        mAttackSystem = new AttackSystem();
    }

    public SummaryAction getAction(MapData mapData) {
        return null;
    }
}
