package com.bpa.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.bpa.backend.consumer.WebSocketServer;
import com.bpa.backend.pojo.Bot;
import com.bpa.backend.pojo.Record;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private static final String addBotUrl = "http://127.0.0.1:8092/bot/add";
    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = {0, 1, 0, -1};
    private final Integer rows;
    private final Integer cols;
    private final Integer innerWallsCount;
    private final int[][] g;

    private final Player playerA;
    private final Player playerB;
    private final ReentrantLock lock = new ReentrantLock();
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private String status = "playing"; // playing 正在游戏中， finished 对局结束
    private String loser = "";  // all 平局， A a输, B b输

    public Game(Integer rows, Integer cols, Integer innerWallsCount, Integer idA, Bot botA, Integer idB, Bot botB) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.g = new int[rows][cols];

        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
        if (botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if (botB != null) {
            botIdB = botB.getId();
            botCodeB = botB.getContent();
        }

        this.playerA = new Player(idA, botIdA, botCodeA, this.rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(idB, botIdB, botCodeB, 1, this.cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public int[][] getG() {
        return this.g;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }


    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;


        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i],
                    y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (checkConnectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }
        g[sx][sy] = 0;
        return false;
    }

    /**
     * 画地图
     */
    private boolean draw() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                g[r][c] = 0;
            }
        }
        // 四周围墙
        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        // 随机生成图内障碍物
        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount / 2; i++) {
            // 放置随机数冲突，随机1000次
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                // 中心对称
                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2))
                    continue;
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw())
                break;
        }
    }

    private String getInput(Player player) {
//        将信息编码成字符串
//        地图#我的起始x#我的起始y#(我的操作序列)#队友的起始x#对手的起始y#(对手的操作序列)
        Player me, you;
        if (playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")";

    }

    private void sendBotCode(Player player) {
        if (player.getBotId().equals(-1)) return; // 本人操作

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));

        String resp = WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
        System.out.println(resp);
    }

    /**
     * 等待两名玩家下一步操作
     * 等待五秒钟，每100毫秒判断一次，如果超过五秒仍有一方未执行操作则判负
     *
     * @return 是否都操作了, 如果超时未操作则判负
     */
    public boolean nextStep() {
        // 前端是1秒走5格，一个格子需要200ms，如果走的步过快，前端渲染只会渲染最后一步，可能会遗漏步数
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendBotCode(playerA);
        sendBotCode(playerB);
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private boolean checkValid(List<Cell> cellsA, List<Cell> cellsB) {
        int sizeA = cellsA.size();
        Cell headA = cellsA.get(sizeA - 1);
        // 头不能撞墙
        if (g[headA.getX()][headA.getY()] == 1)
            return false;
        // 头不能撞自身
        for (int i = 0; i < sizeA - 1; i++) {
            if (cellsA.get(i).getX() == headA.getX() && cellsA.get(i).getY() == headA.getY())
                return false;
        }

        // 头不能撞另一条蛇
        for (Cell cellB : cellsB) {
            if (cellB.getX() == headA.getX() && cellB.getY() == headA.getY())
                return false;
        }

        return true;
    }

    /**
     * 判断两名玩家的下一步操作是否合法
     */
    private void judge() {
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = checkValid(cellsA, cellsB);
        boolean validB = checkValid(cellsB, cellsA);

        if (!validA || !validB) {
            this.status = "finished";
            if (!validA && !validB) {
                this.loser = "all";
            } else if (!validA) {
                this.loser = "A";
            } else {
                this.loser = "B";
            }
        }
    }

    /**
     * 向两名玩家传递移动信息
     */
    private void sendMove() {
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            //当前回合完成，清空a和b的操作
            nextStepA = nextStepB = null;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }

    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                res.append(g[i][j]);
        return res.toString();
    }

    private void save2database() {
        Record record = new Record(null, playerA.getId(), playerA.getSx(), playerA.getSy(), playerB.getId(),
                playerB.getSx(),
                playerB.getSy(), playerA.getStepsString(), playerB.getStepsString(), getMapString(), loser, new Date());
        WebSocketServer.recordMapper.insert(record);
    }

    /**
     * 向两名玩家公布结果
     */
    private void sendResult() {
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        save2database();
        sendAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {
                judge();
                if ("playing".equals(status)) { // 如果judge操作合法，则表明对局仍在进行
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
