package com.thundersoft.codedog;

import com.thundersoft.codedog.bean.MapData;
import com.thundersoft.codedog.bean.SummaryAction;
import com.thundersoft.codedog.game.DecisionSystem;
import com.thundersoft.codedog.game.GameManager;
import com.thundersoft.codedog.heart.HeartManager;
import com.thundersoft.codedog.net.SocketManager;

public class MainProcess {

    private SocketManager mSocketManager;
    private HeartManager mHeartManager;
    private GameManager mGameManager;
    private DecisionSystem mDecisionSystem;

    public void start(String ip,int port) {
        //建立socket连接
        mSocketManager = new SocketManager();
        mSocketManager.connect(ip,port);


        //初始化心跳管理器
        mHeartManager = new HeartManager(mSocketManager);
        mHeartManager.setHeartListener(mHeartListener);

        //初始化游戏管理器
        mGameManager = new GameManager(mSocketManager);
        mGameManager.setGameListener(mGameListener);

        //初始化决策模块
        mDecisionSystem = new DecisionSystem();

        //开始发送心跳
        mHeartManager.startHeart();
    }

    private HeartManager.HeartListener mHeartListener = new HeartManager.HeartListener() {
        @Override
        public void gameStarted(int playerNumer) {
            //游戏已开始,开始读取地图数据
            mGameManager.setPlayerNumer(playerNumer);
            mGameManager.startRead();
        }
    };

    private GameManager.GameListener mGameListener = new GameManager.GameListener() {
        @Override
        public void onMapDataChanged(MapData mapData) {
            //获取到地图数据,将地图数据传给决策模块并获取下一步动作
            SummaryAction action = mDecisionSystem.getAction(mapData);
            //发送下一步动作
            mGameManager.sendAction(action);
        }

        @Override
        public void onGameEnd() {
            //游戏已结束,关闭Socket
            mSocketManager.close();
        }
    };
}
