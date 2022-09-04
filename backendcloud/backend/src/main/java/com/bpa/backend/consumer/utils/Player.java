package com.bpa.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;
    private String botCode;
    // 起始位置
    private Integer sx;
    private Integer sy;
    private List<Integer> steps; // 每一步走的方向

    /**
     * 检查当前回合蛇身长度是否会增加
     *
     * @param steps
     * @return
     */
    private boolean checkTailIncreasing(int steps) {
        if (steps <= 10) return true;
        return steps % 3 == 1;
    }

    /**
     * 每次都计算一遍蛇身
     *
     * @return
     */
    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!checkTailIncreasing(++step)) {
                res.remove(0);
            }
        }

        return res;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for (int step : steps) {
            res.append(step);
        }
        return res.toString();
    }
}
