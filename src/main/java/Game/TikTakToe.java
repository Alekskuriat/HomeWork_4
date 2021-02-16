package Game;

import java.util.Random;
import java.util.Scanner;

public class TikTakToe {
    // Указать размер игрового поля от 3 до 20
    static final int MAP_SIZE = 5;

    static int winSize;

    static final char DOT_EMPTY = '•';
    static final char DOT_HUMAN = 'X';
    static final char DOT_AI = 'O';
    static final char HEADER_FIRST_SYMBOL = '♥';

    static final Scanner in = new Scanner(System.in);
    static final Random random = new Random();

    static final char[][] MAP = new char[MAP_SIZE][MAP_SIZE];

    static int turnsCount;
    static int rowNumber;
    static int columnNumber;
    static int count;
    static int startMain_i;
    static int startMain_j;
    static int startSide_i;
    static int startSide_j;

    public static void main(String[] args) {

        turnGame();

    }

    private static void turnGame() {

        winningStreak();

        initMap();

        printMap();

        playGame();

    }

    private static void winningStreak() {
        if (MAP_SIZE >= 3 && MAP_SIZE <= 4){
            winSize = 3;
        }
        if (MAP_SIZE >= 5 && MAP_SIZE <= 7){
            winSize = 4;
        }
        if (MAP_SIZE >= 8 && MAP_SIZE <= 20){
            winSize = 5;
        }
    }


    private static void initMap() {
        for (int i = 0; i < MAP_SIZE ; i++) {
            for (int j = 0; j < MAP_SIZE ; j++) {
                MAP[i][j] = DOT_EMPTY;
            }
        }
    }

    private static void printMap() {
        for (int i = 0; i <= MAP_SIZE; i++) {
            for (int j = 0; j <= MAP_SIZE; j++) {
                if (i == 0 && j == 0) {
                    System.out.printf("%3c", HEADER_FIRST_SYMBOL);
                    continue;
                }
                if (i == 0) {
                    System.out.printf("%3d",j);
                    continue;
                }
                if (j == 0) {
                    System.out.printf("%3d",i);
                    continue;
                }
            System.out.printf("%3c",MAP[i-1][j-1]);
            }
        System.out.println();
        }
    }

    private static void playGame() {
        turnsCount = 0;

        while (true) {
            humanTurn();
            printMap();
            startChecking(DOT_HUMAN);

            turnAI();
            printMap();
            startChecking(DOT_AI);
        }
    }

    private static void humanTurn() {
        boolean isInputValid = true;

        System.out.println("\nХод человека! Введите номера строки и столбца");

        do{
            rowNumber = -1;
            columnNumber = -1;
            isInputValid = true;

            System.out.print("Строка: ");
            if (in.hasNextInt()) {
                rowNumber = in.nextInt() - 1;
            } else {
                processingIncorrectInput();
                isInputValid = false;
                continue;
            }

            System.out.print("Столбик: ");
            if (in.hasNextInt()) {
                columnNumber = in.nextInt() - 1;
            } else {
                processingIncorrectInput();
                isInputValid = false;
            }

        }  while (!(isInputValid && isHumanTurnValid(rowNumber, columnNumber)));

        MAP[rowNumber][columnNumber] = DOT_HUMAN;
        turnsCount++;
    }

    private static void turnAI() {
        System.out.println("\nХод компьютера!");

        do {
            rowNumber = random.nextInt(MAP_SIZE);
            columnNumber = random.nextInt(MAP_SIZE);
        } while (!isCellOccupancy(rowNumber, columnNumber));

        MAP[rowNumber][columnNumber] = DOT_AI;
        turnsCount++;
    }

    private static void startChecking(char DOT) {
        if (turnsCount >= (2 * winSize - 1)){
            checkEnd(DOT);
        }
    }

    private static void processingIncorrectInput() {
            System.out.println("Ошибка ввода! Введите число в диапазоне размера игрового поля");
            in.nextLine();
    }

    private static boolean isHumanTurnValid(int rowNumber, int columnNumber) {
        if (isNumbersValid(rowNumber, columnNumber)) {
            System.out.println("\nПроверьте значения строки и столбца");
            return false;
        } else if (!isCellOccupancy(rowNumber, columnNumber)) {
            System.out.println("\nВы выбрали занятую ячейку");
            return false;
        }
        return true;
    }

    private static boolean isNumbersValid(int rowNumber, int columnNumber) {
        return (rowNumber >= MAP_SIZE || rowNumber < 0 || columnNumber >= MAP_SIZE || columnNumber < 0);
    }

    private static boolean isCellOccupancy(int rowNumber, int columnNumber) {
        return MAP[rowNumber][columnNumber] == DOT_EMPTY;
    }

    private static void checkEnd(char symbol) {
        if (checkWin(symbol, rowNumber, columnNumber)) {
            if (symbol == DOT_HUMAN) {
                System.out.println("Ура! Вы победили!");
            } else {
                System.out.println("Победил компьютер!");
            }
            System.exit(0);
        } else if (isMapFull()) {
            System.out.println("Ничья!");
            System.exit(0);
        }
    }

    private static boolean isMapFull() {
        return turnsCount == MAP_SIZE * MAP_SIZE;
    }

    private static boolean checkWin(char symbol, int row, int column) {
        return (checkingRows(row, symbol) ||
                    checkingColumns(column, symbol) ||
                        checkingDiagonals(row, column, symbol));
    }

    private static boolean checkingRows(int row, char symbol) {
        for (int j = 0; j < MAP_SIZE; j++) {
            if (MAP[row][j] == symbol) {
                count++;
                if (count == winSize) return true;
            } else count = 0;
        }
        count = 0;

        return false;
    }

    private static boolean checkingColumns(int column, char symbol) {
        for (int i = 0; i < MAP_SIZE; i++) {
            if (MAP[i][column] == symbol){
                count++;
                if (count == winSize) return true;
            } else count = 0;
        }
        count = 0;

        return false;
    }

    private static boolean checkingDiagonals(int row, int column, char symbol) {
        mainDiagonalStart(row, column);
        sideDiagonalStart(row, column);

        for (int i = startMain_i,  j = startMain_j; (i < MAP_SIZE  && j < MAP_SIZE ); i++, j++) {
            if (MAP[i][j] == symbol){
                count++;
                if (count == winSize) return true;
            } else count = 0;
        }
        count = 0;

        for (int i = startSide_i,  j = startSide_j; (i >= 0 && j < MAP_SIZE ); i--, j++) {
            if (MAP[i][j] == symbol){
                count++;
                if (count == winSize) return true;
            } else count = 0;
        }
        count = 0;

        return false;
    }

    private static void mainDiagonalStart(int row, int column) {
        startMain_i = row;
        startMain_j = column;
        while ( startMain_i > 0 && startMain_j > 0){
            startMain_i--;
            startMain_j--;
        }
    }

    private static void sideDiagonalStart(int row, int column) {
        startSide_i = row;
        startSide_j = column;
        while ((startSide_i < MAP_SIZE - 1  &&  startSide_j > 0)){
            startSide_i++;
            startSide_j--;
        }
    }
}
