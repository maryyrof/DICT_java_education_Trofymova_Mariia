package TicTacToe;

import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}

class Game {
    private final char[][] board = {
            {'_', '_', '_'},
            {'_', '_', '_'},
            {'_', '_', '_'}
    };
    private boolean xTurn = true; // Хто зараз ходить
    private final Scanner scanner = new Scanner(System.in);

    // --- Запуск гри ---
    public void start() {
        printBoard();

        while (true) {
            makeMove();  // Запит координат і оновлення поля
            printBoard();

            String state = checkGameState();
            if (!state.equals("Game not finished")) {
                System.out.println(state);
                break;
            }
            xTurn = !xTurn; // черга іншого гравця
        }
    }

    // --- Вивід ігрового поля ---
    private void printBoard() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print((board[i][j] == '_' ? ' ' : board[i][j]) + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    // --- Обробка введення користувача ---
    private void makeMove() {
        while (true) {
            System.out.print("Enter the coordinates: ");
            String input = scanner.nextLine();
            String[] parts = input.trim().split(" ");

            if (parts.length != 2) {
                System.out.println("You should enter numbers!");
                continue;
            }

            int row, col;
            try {
                row = Integer.parseInt(parts[0]);
                col = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("You should enter numbers!");
                continue;
            }

            if (row < 1 || row > 3 || col < 1 || col > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }

            int i = row - 1;
            int j = col - 1;

            if (board[i][j] != '_') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            board[i][j] = xTurn ? 'X' : 'O';
            break;
        }
    }

    // --- Перевірка стану гри ---
    private String checkGameState() {
        boolean xWins = checkWin('X');
        boolean oWins = checkWin('O');
        boolean empty = hasEmptyCells();

        int xCount = countChar('X');
        int oCount = countChar('O');

        if ((xWins && oWins) || Math.abs(xCount - oCount) > 1) {
            return "Impossible";
        } else if (xWins) {
            return "X wins";
        } else if (oWins) {
            return "O wins";
        } else if (empty) {
            return "Game not finished";
        } else {
            return "Draw";
        }
    }

    // --- Перевірка переможця ---
    private boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    // --- Перевірка наявності порожніх клітинок ---
    private boolean hasEmptyCells() {
        for (char[] row : board) {
            for (char c : row) {
                if (c == '_') return true;
            }
        }
        return false;
    }

    // --- Підрахунок символів ---
    private int countChar(char ch) {
        int count = 0;
        for (char[] row : board) {
            for (char c : row) {
                if (c == ch) count++;
            }
        }
        return count;
    }
}
