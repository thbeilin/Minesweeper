import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Tzvi on 1/25/2018.
 */
public class MinesweeperBoard {
    private final int BOARD_LENGTH = 9, BOARD_WIDTH = 9, NUMBER_OF_MINES = 9;
    private int[][] board = new int[BOARD_LENGTH][BOARD_WIDTH];
    private ArrayList<Integer> minePlacement = new ArrayList<>(NUMBER_OF_MINES);

    public boolean isZeroMineSquare() {
        for (int r = 0; r < BOARD_LENGTH; r++) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                if (board[r][c] == 0)
                    return true;
            }
        }
        return false;
    }

    private void findMines() {
        Random random = new Random();
        int rand;
        for (int i = 0; i < NUMBER_OF_MINES; i++) {
            do {
                rand = random.nextInt(BOARD_LENGTH * BOARD_WIDTH);
            }
            while (minePlacement.contains(rand));
            minePlacement.add(rand);
        }
    }

    MinesweeperBoard() {
        findMines();
        placeMines(NUMBER_OF_MINES);
    }

    private void placeMines(int mines) {
        int placeInArray = 0, minesPlaced = 0;

        Collections.sort(minePlacement);
        for (int row = 0; row < BOARD_LENGTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (minePlacement.get(minesPlaced) == placeInArray) {
                    board[row][col] = 9;
                    placeWarnings(row, col);
                    if (minesPlaced < mines - 1) {
                        minesPlaced++;
                    }
                }
                placeInArray++;
            }
        }
    }

    public int getBOARD_WIDTH() {
        return BOARD_WIDTH;
    }

    public int getBOARD_LENGTH() {
        return BOARD_LENGTH;
    }

    public int getNUMBER_OF_MINES() {
        return NUMBER_OF_MINES;
    }

    private void placeWarnings(int r, int c) {
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && j >= 0 && i < BOARD_LENGTH && j < BOARD_WIDTH) {
                    if (!(i == r && j == c)) {
                        if (!(board[i][j] == 9))
                            board[i][j]++;
                    }
                }
            }
        }
    }

    public int getNearbyMineNumber(int row, int col) {
        return board[row][col];
    }

    public void newGame() {
        for (int r = 0; r < BOARD_LENGTH; r++) {
            for (int c = 0; c < BOARD_WIDTH; c++) {
                board[r][c] = 0;
            }
        }
        minePlacement.clear();
        findMines();
        placeMines(NUMBER_OF_MINES);
    }
}
