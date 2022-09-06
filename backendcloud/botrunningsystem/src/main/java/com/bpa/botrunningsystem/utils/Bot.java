package com.bpa.botrunningsystem.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bot implements java.util.function.Supplier<Integer> {
    public Integer nextStep(String input) {
        String[] split = input.split("#");
        int[][] g = new int[13][14];
        for (int i = 0, k = 0; i < 13; i++)
            for (int j = 0; j < 14; j++, k++)
                if (split[0].charAt(k) == '1')
                    g[i][j] = 1;
        int meSx = Integer.parseInt(split[1]), meSy = Integer.parseInt(split[2]);
        int youSx = Integer.parseInt(split[4]), youSy = Integer.parseInt(split[5]);

        List<Cell> meCells = getCells(meSx, meSy, split[3]);
        List<Cell> youCells = getCells(youSx, youSy, split[6]);

        for (Cell c : meCells) g[c.x][c.y] = 1;
        for (Cell c : youCells) g[c.x][c.y] = 1;

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for (int i = 0; i < 4; i++) {
//            拿到a蛇的头
            int x = meCells.get(meCells.size() - 1).x + dx[i];
            int y = meCells.get(meCells.size() - 1).y + dy[i];
            if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0)
                return i;
        }
        return 0;
    }

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
    public List<Cell> getCells(int sx, int sy, String steps) {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int i = 1; i < steps.length() - 1; i++) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if (!checkTailIncreasing(++step)) {
                res.remove(0);
            }
        }

        return res;
    }

    @Override
    public Integer get() {
        File file = new File("input.txt");
        try {
            Scanner sc = new Scanner(file);
            return nextStep(sc.next());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    static class Cell {
        public int x, y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
