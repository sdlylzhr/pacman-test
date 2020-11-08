package com.thundersoft.codedog.game;

import com.thundersoft.codedog.bean.MapData;
import com.thundersoft.codedog.bean.SummaryAction;
import com.thundersoft.codedog.net.SocketManager;
import com.thundersoft.codedog.util.MapUtil;

/**
 * 游戏管理器
 */
public class GameManager {

    private SocketManager mSocketManager;
    private GameListener mGameListener;
    private int playerNumer;
    private boolean isGameEnd = false;

    public GameManager(SocketManager socketManager) {
        mSocketManager = socketManager;
    }

    public void setGameListener(GameListener gameListener) {
        mGameListener = gameListener;
    }

    public void startRead() {
        while (!isGameEnd) {
            String result = mSocketManager.read();
            System.out.println("map data : " + result);

//            MapUtil.getInstance().getMapData()
        }
    }

    public void setPlayerNumer(int playerNumer) {
        this.playerNumer = playerNumer;
    }

    public void sendAction(SummaryAction action) {

    }

    public interface GameListener {
        public void onMapDataChanged(MapData mapData);
        public void onGameEnd();
    }
}
