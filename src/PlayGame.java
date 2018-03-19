import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static javax.swing.JOptionPane.showConfirmDialog;

/**
 * Created by Tzvi on 1/25/2018.
 */
public class PlayGame extends JFrame {
    private MinesweeperBoard game = new MinesweeperBoard();
    private JButton[][] buttons = new JButton[game.getBOARD_LENGTH()][game.getBOARD_WIDTH()];
    private int buttonsPressed;
    private boolean gameActive = true;
    private int nearbyMarks = 0;
    private int correctMarks = 0;
    private Color unclickedColor = new Color(155, 155, 155), clickedColor = new Color(164, 164, 164);
    private Color[] colors = {clickedColor,Color.blue,new Color(32, 135, 37),
            Color.red, Color.black, Color.orange, Color.cyan, Color.magenta, Color.black, Color.white};

    public PlayGame() {
        setTitle("Minesweeper");
        setSize(45 * game.getBOARD_WIDTH(), 45 * game.getBOARD_LENGTH());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(game.getBOARD_LENGTH(), game.getBOARD_WIDTH()));
        for (int row = 0; row < game.getBOARD_LENGTH(); row++) {
            for (int col = 0; col < game.getBOARD_WIDTH(); col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].addMouseListener(new MyMouseListener());
                buttons[row][col].setForeground(Color.white);
                buttons[row][col].setBackground(unclickedColor);
                add(buttons[row][col]);
            }
        }
        setVisible(true);
    }

    private class MyMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            for (int r = 0; r < game.getBOARD_LENGTH(); r++) {
                for (int c = 0; c < game.getBOARD_WIDTH(); c++) {
                    if (SwingUtilities.isRightMouseButton(e) && SwingUtilities.isLeftMouseButton(e)) {
                        if ((JButton)e.getSource() == buttons[r][c]) {
                            getMarkedMines(r, c);
                            if (!buttons[r][c].getText().equals("X") && !buttons[r][c].getText().equals("")) {
                                if (game.getNearbyMineNumber(r, c) == nearbyMarks) {
                                    //if mines are correct, expand
                                    if (game.getNearbyMineNumber(r, c) == correctMarks) {
                                        expand(r, c);
                                    } else {
                                        blowUp();
                                    }
                                }
                            }
                        }
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if ((JButton)e.getSource() == buttons[r][c]) {
                            showClicked(r, c);
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if ((JButton)e.getSource() == buttons[r][c]) {
                            if (buttons[r][c].getText().equals("X")) {
                                buttons[r][c].setText("");
                                buttons[r][c].setForeground(Color.white);
                            } else if (buttons[r][c].getText().equals("")) {
                                buttons[r][c].setText("X");
                                buttons[r][c].setForeground(Color.red);
                            }
                        }
                    }
                }
            }
        }
    }

    private void getMarkedMines(int r, int c) {

        correctMarks = 0;
        nearbyMarks = 0;
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && j >= 0 && i < game.getBOARD_LENGTH() && j < game.getBOARD_WIDTH()) {
                    if (!(i == r && j == c)) {
                        if (buttons[i][j].getText().equals("X")) {
                            if (game.getNearbyMineNumber(i, j) == 9) {
                                correctMarks++;
                            }
                            nearbyMarks++;
                        }
                    }
                }
            }
        }
    }

    private void showClicked(int r, int c) {
        if (buttonsPressed == 0 && !(game.getNearbyMineNumber(r, c) == 0)) {
            if (game.isZeroMineSquare()) {
                resetGame();
                showClicked(r, c);
            } else if (game.getNearbyMineNumber(r, c) == 9) {
                resetGame();
                showClicked(r, c);
            }
        }

        if (gameActive && !buttons[r][c].getText().equals("X")) {
            if (game.getNearbyMineNumber(r, c) == 9) {
                blowUp();
            } else {
                if (!buttons[r][c].getText().equals(Integer.toString(game.getNearbyMineNumber(r, c)))) {
                    buttons[r][c].setText(Integer.toString(game.getNearbyMineNumber(r, c)));
                    setNumberColor(r, c);
                    buttons[r][c].setBackground(colors[0]);
                    buttonsPressed++;
                    if (game.getNearbyMineNumber(r, c) == 0) {
                        expand(r, c);
                    }
                    if (buttonsPressed == ((game.getBOARD_LENGTH() * game.getBOARD_WIDTH()) - game.getNUMBER_OF_MINES())) {
                        int o = JOptionPane.showConfirmDialog(null, "You win! Play again?");
                        playAgainYesNo(o);
                    }
                }
            }
        }
    }

    private void blowUp() {
        for (int row = 0; row < game.getBOARD_LENGTH(); row++) {
            for (int col = 0; col < game.getBOARD_WIDTH(); col++) {
                if (game.getNearbyMineNumber(row, col) == 9) {
                    buttons[row][col].setForeground(Color.white);
                    buttons[row][col].setText("*");
                }
            }
        }
        int o = showConfirmDialog(null, "Game over. Play again?");
        playAgainYesNo(o);
    }

    private void playAgainYesNo(int o) {
        if(o== JOptionPane.YES_OPTION) {
            gameActive = false;
            resetGame();
        }else if (o==JOptionPane.CANCEL_OPTION){
            gameActive=false;
        } else {
            System.exit(0);
        }
    }

    private void setNumberColor(int r, int c) {
        buttons[r][c].setForeground(colors[Integer.parseInt(buttons[r][c].getText())]);
    }

    private void expand(int r, int c) {
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && j >= 0 && i < game.getBOARD_LENGTH() && j < game.getBOARD_WIDTH()) {
                    if (!(i == r && j == c)) {
                        showClicked(i, j);
                    }
                }
            }
        }
    }

    private void resetGame() {
        game.newGame();
        buttonsPressed = 0;
        for (int row = 0; row < game.getBOARD_LENGTH(); row++) {
            for (int col = 0; col < game.getBOARD_WIDTH(); col++) {
                buttons[row][col].setText("");
                buttons[row][col].setBackground(unclickedColor);
            }
        }
        gameActive = true;
    }

    public static void main(String[] args) {
        new PlayGame();
    }
}