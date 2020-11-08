package com.thundersoft.codedog.heart;

import com.thundersoft.codedog.net.SocketManager;

/**
 * 心跳管理器
 */
public class HeartManager {

    private static final String INIT_COMMAND = "(0210c307997948cf81c37a496529d7eb)";
    private static final String HEART_COMMAND = "(H)";
    private static final String READY_COMMAND = "(READY)";
    private static final String INIT_RESULT = "[OK]";
    private static final String HEART_RESULT = "[OK]";
    private static final String GAME_START_HEAD = "[START";
    private static final String READY_RESULT = "[OK]";


    private SocketManager mSocketManager;
    private HeartListener mHeartListener;

    private boolean isGameStarted = false;

    public HeartManager(SocketManager socketManager) {
        mSocketManager = socketManager;
    }

    public void setHeartListener(HeartListener heartListener) {
        mHeartListener = heartListener;
    }

    public void startHeart() {

        String initResult = mSocketManager.callForResult(INIT_COMMAND);
        System.out.println("Init result : " + initResult);

        if (initResult != null && initResult.equals(INIT_RESULT)) {
            System.out.println("init result ok");
            new Thread() {
                @Override
                public void run() {
                    while (!isGameStarted) {
                        mSocketManager.send(HEART_COMMAND);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            while (true) {
                String result = mSocketManager.read();
                System.out.println("heart data : " + result);
                if (result != null) {
                    if (isGameStart(result)) {
                        System.out.println("game start");
                        isGameStarted = true;
                        int playerNum = getPlayerNum(result);
                        System.out.println("playerNum : "+ playerNum);
                        String readyResult = mSocketManager.callForResult(READY_COMMAND);
                        System.out.println("readyResult : "+ readyResult);
                        if (readyResult != null && readyResult.equals(READY_RESULT)) {
                            if (mHeartListener != null && playerNum >= 0) {
                                mHeartListener.gameStarted(playerNum);
                            }
                        }
                        break;
                    } else if (result.equals(HEART_RESULT)) {

                    } else {
                        isGameStarted = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    private boolean isGameStart(String result) {
        if (result.startsWith(GAME_START_HEAD)) {
            return true;
        }
        return false;
    }

    private int getPlayerNum(String result) {
        String[] subs = result.split(" ");
        for (String sub : subs) {
            System.out.println("sub : " + sub);
        }
        if (subs != null && subs.length >= 2) {
            try {
                return Integer.parseInt(subs[1]);
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
        }
        return -1;
    }

    public interface HeartListener {
        public void gameStarted(int playerNumer);
    }
}
