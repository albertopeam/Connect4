package game;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Al on 18/01/2016.
 */
public class Connect4 {

    public static final String NO_WINNER = "-";
    public static final String RED_WINNER = "R";
    public static final String GREEN_WINNER = "G";
    public static final int BOARD_ROWS = 6;
    public static final int BOARD_COLUMNS = 7;
    private static final int DISCS_TO_WIN = 4;
    private Color[][] board;
    private Color turn;
    private ByteArrayOutputStream outputStream;


    public Connect4(ByteArrayOutputStream outputStream){
        this.outputStream = outputStream;
        this.turn = Color.redColor();
        this.board = new Color[BOARD_ROWS][BOARD_COLUMNS];
        for (Color[] row:board){
            Arrays.fill(row, Color.emptyColor());
        }
    }


    public boolean isFinished(){
        return getNumberOfDiscs() == BOARD_ROWS * BOARD_COLUMNS;
    }


    public String winner(){
        for (int i=0;i<BOARD_COLUMNS;i++){
            StringBuilder columnBuilder = new StringBuilder();
            for (int j=0;j<BOARD_ROWS;j++){
                columnBuilder.append(board[j][i].toString());
            }
            String column = columnBuilder.toString();
            String result = checkLine(column);
            if (!result.equals(NO_WINNER)){
                return result;
            }
        }
        for (int i=0;i<BOARD_ROWS;i++){
            StringBuilder rowBuilder = new StringBuilder();
            for (int j=0;j<BOARD_COLUMNS;j++){
                rowBuilder.append(board[i][j].toString());
            }
            String row = rowBuilder.toString();
            String result = checkLine(row);
            if (!result.equals(NO_WINNER)){
                return result;
            }
        }
        for (int i=0;i<=BOARD_ROWS-DISCS_TO_WIN;i++){
            for(int k=0;k < BOARD_COLUMNS - DISCS_TO_WIN;k++) {
                StringBuilder diagonalBuilder = new StringBuilder();
                for (int j = 0; j <= BOARD_COLUMNS - DISCS_TO_WIN; j++) {
                    diagonalBuilder.append(board[i+j][k+j].toString());
                }
                String diagonal = diagonalBuilder.toString();
                String result = checkLine(diagonal);
                if (!result.equals(NO_WINNER)) {
                    return result;
                }
            }
        }
        for (int i=0;i<=BOARD_ROWS-DISCS_TO_WIN;i++){
            for(int k = DISCS_TO_WIN-1;k < BOARD_COLUMNS;k++) {
                StringBuilder diagonalBuilder = new StringBuilder();
                for (int j = 0; j <= BOARD_COLUMNS - DISCS_TO_WIN; j++) {
                    diagonalBuilder.append(board[i+j][k-j].toString());
                }
                String diagonal = diagonalBuilder.toString();
                String result = checkLine(diagonal);
                if (!result.equals(NO_WINNER)) {
                    return result;
                }
            }
        }
        return NO_WINNER;
    }


    private String checkLine(String line){
        //line = line.replaceAll(" ", "");
        if (line.length() < DISCS_TO_WIN){
            return NO_WINNER;
        }else if(line.contains("RRRR")){
            return RED_WINNER;
        }else if(line.contains("GGGG")){
            return GREEN_WINNER;
        }
        return NO_WINNER;
    }


    public int getNumberOfDiscs(){
        int numberOfDiscs = 0;
        for (int i=0;i<BOARD_ROWS;i++){
            for (int j=0;j<BOARD_COLUMNS;j++ ){
                if (!board[i][j].equals(Color.emptyColor())){
                    numberOfDiscs++;
                }
            }
        }
        return numberOfDiscs;
    }


    public int insertDisc(int column) throws OutOfBoardException, ColumnFullOfDIscsException{
        if (column < 1 || column >BOARD_COLUMNS) {
            throw new OutOfBoardException();
        }

        int insertRow = 0;
        column = fixedColumn(column);
        if(isColumnFull(column)){
            throw new ColumnFullOfDIscsException();
        }else{
            insertRow = rowForDisc(column);
            board[insertRow][column] = turn;
            switchPlayer();
        }
        String boardOutput = buildBoardOutput();
        tryPrintOutput(boardOutput);
        return insertRow;
    }


    private String buildBoardOutput(){
        StringBuilder boardBuilder = new StringBuilder();
        for (int i=BOARD_ROWS-1;i>=0;i--){
            String row = buildRowOutput(i);
            boardBuilder.append(row);
            boardBuilder.append("\n");
        }
        return boardBuilder.toString();
    }


    private String buildRowOutput(int i){
        StringBuilder rowBuilder = new StringBuilder();
        for (int j=0;j<BOARD_COLUMNS;j++){
            rowBuilder.append("|");
            rowBuilder.append(board[i][j]);
        }
        rowBuilder.append("|");
        return rowBuilder.toString();
    }


    public boolean isColumnFull(int column){
        if (!board[fixedRow(BOARD_ROWS)][column].equals(Color.emptyColor())){
            return true;
        }
        return false;
    }


    public Color getNextTurn(){
        String message = "R turn";
        tryPrintOutput(message);
        return turn;
    }


    private void tryPrintOutput(String message){
        try {
            outputStream.reset();
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void switchPlayer(){
        if (turn.equals(Color.greenColor())){
            turn = Color.redColor();
        }else {
            turn = Color.greenColor();
        }
    }

    private int rowForDisc(int column){
        int row = 0;
        for(int i=0;i<BOARD_ROWS;i++){
            if (board[i][column].equals(Color.emptyColor())){
                row = i;
                break;
            }
        }
        return row;
    }


    private int fixedColumn(int column){
        return column - 1;
    }

    private int fixedRow(int row){
        return row - 1;
    }
}
