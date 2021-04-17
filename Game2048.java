package com.codegym.games.game2048;

import com.codegym.engine.cell.*;

import java.util.HashMap;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;

    @Override
    public void initialize() {

        setScreenSize(SIDE, SIDE);
        createGame();

        drawScene();

    }


    private void createGame() {


        gameField = new int[SIDE][SIDE];


        createNewNumber();
        createNewNumber();

    }

    private void drawScene() {

        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {

                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }

    }

    private void createNewNumber() {

        boolean isZero = true;
        while (isZero) {
            int x = getRandomNumber(SIDE);
            int y = getRandomNumber(SIDE);
            if (gameField[y][x] == 0) {
                int randomValue = getRandomNumber(10);

                if (randomValue == 9) {
                    gameField[y][x] = 4;
                } else {
                    gameField[y][x] = 2;
                }

                isZero = false;
            }

            int max = getMaxTileValue();
            if (max == 2048) {
                win();
            }

        }

    }

    private void setCellColoredNumber(int x, int y, int value) {

        Color setedColor = getColorBy(value);
        String number = String.valueOf(value);

        if (value > 0) {
            setCellValueEx(x, y, setedColor, number);
        } else {
            setCellValueEx(x, y, setedColor, "");
        }

    }

    private Color getColorBy(int value) {

        HashMap<Integer, Color> colorHashMap = new HashMap<>();
        colorHashMap.put(2, Color.PINK);
        colorHashMap.put(4, Color.PURPLE);
        colorHashMap.put(8, Color.BLUE);
        colorHashMap.put(16, Color.AQUA);
        colorHashMap.put(32, Color.DARKGREEN);
        colorHashMap.put(64, Color.LIGHTGREEN);
        colorHashMap.put(128, Color.ORANGE);
        colorHashMap.put(256, Color.CRIMSON);
        colorHashMap.put(512, Color.RED);
        colorHashMap.put(1024, Color.GOLD);
        colorHashMap.put(2048, Color.DARKRED);
        colorHashMap.put(0, Color.WHITE);

        Color newColor = Color.NONE;

        switch (value) {

            case 0:
                return colorHashMap.get(0);

            case 2:
                return colorHashMap.get(2);

            case 4:
                return colorHashMap.get(4);

            case 8:
                return colorHashMap.get(8);

            case 16:
                return colorHashMap.get(16);

            case 32:
                return colorHashMap.get(32);

            case 64:
                return colorHashMap.get(64);

            case 128:
                return colorHashMap.get(128);

            case 256:
                return colorHashMap.get(256);

            case 512:
                return colorHashMap.get(512);

            case 1024:
                return colorHashMap.get(1024);

            case 2048:
                return colorHashMap.get(2048);
        }

        return newColor;


    }


    private boolean compressRow(int[] row) {
        int holder;
        boolean compress = false;
        for (int l = 0; l < row.length - 1; l++)
            for (int m = 0; m < row.length - 1; m++) {
                if (row[m] == 0 && row[m + 1] != 0) {
                    compress = true;
                    for (int n = m; n < row.length - 1; n++) {
                        holder = row[n + 1];
                        row[n + 1] = row[n];
                        row[n] = holder;
                    }
                }
            }
        return compress;
    }


    private boolean mergeRow(int[] row) {
        boolean merge = false;

        for (int i = 0; i < row.length - 1; i++) {
            if ((row[i] != 0) && (row[i] == row[i + 1])) {
                merge = true;
                score += row[i]*2;
                setScore(score);
                for (int j = i; j < i + 2; j++) {
                    row[i] += row[i + 1];
                    row[i + 1] = 0;
                }
            }
        }

        return merge;

    }

    @Override
    public void onKeyPress(Key key) {

        if (isGameStopped) {
            if (key == Key.SPACE) {
                createGame();
                drawScene();
                isGameStopped = false;
                score = 0;
                setScore(0);
            }
        } else {

            boolean possibleMove = canUserMove();
            if (!possibleMove) {
                gameOver();
            } else {

                if (key == Key.LEFT) {
                    moveLeft();
                    drawScene();
                } else if (key == Key.RIGHT) {
                    moveRight();
                    drawScene();
                } else if (key == Key.UP) {
                    moveUp();
                    drawScene();
                } else if (key == Key.DOWN) {
                    moveDown();
                    drawScene();
                }
            }
        }


    }

    private void moveLeft() {

        boolean compress = false;
        boolean merge = false;
        int count = 0;
        for (int i = 0; i < SIDE; i++) {
            merge = mergeRow(gameField[i]);

            compress = compressRow(gameField[i]);
            if (merge || compress) {
                if (count == 0) {
                    createNewNumber();
                    count++;
                }
            }
        }

    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int n = SIDE;
        int x = (n / 2);
        int y = n - 1;
        for (int i = 0; i < x; i++) {
            for (int j = i; j < y - i; j++) {
                int k = gameField[i][j];
                gameField[i][j] = gameField[y - j][i];
                gameField[y - j][i] = gameField[y - i][y - j];
                gameField[y - i][y - j] = gameField[j][y - i];
                gameField[j][y - i] = k;
            }
        }

    }

    private int getMaxTileValue() {

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                int temp = gameField[j][i];
                if (temp > max) {
                    max = temp;
                }
            }
        }

        return max;
    }

    private void win() {

        isGameStopped = true;
        showMessageDialog(Color.GOLD, "YOU HAVE WON!", Color.WHITE, 50);
    }

    private boolean canUserMove() {

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[j][i] == 0) {
                    return true;
                }
            }
        }

        for (int r = 0; r < SIDE - 1; r++) {
            for (int c = 0; c < SIDE; c++) {
                if (gameField[r][c] == gameField[r + 1][c]) {
                    return true;
                }
            }
        }

        for (int r = 0; r < SIDE; r++) {
            for (int c = 0; c < SIDE - 1; c++) {
                if (gameField[r][c] == gameField[r][c + 1]) {
                    return true;
                }
            }
        }

        return false;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BROWN, "NO MORE MOVES, YOU LOST!", Color.RED, 35);
    }

}
