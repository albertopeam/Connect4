package game;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Al on 18/01/2016.
 */
public class Connect4Spec {


    private Connect4 connect4;
    private ByteArrayOutputStream outputStream;


    @Before
    public void beforeEachTest() {
        outputStream = new ByteArrayOutputStream();
        connect4 = new Connect4(outputStream);
    }


    @Test
    public void testWhenConnect4IsInstantiatedThenBoardIsEmpty() {
        int discs = connect4.getNumberOfDiscs();
        assertThat(discs, is(0));
    }


    @Test(expected = OutOfBoardException.class)
    public void testWhenDiscIsInsertedIntoAnInvalidPosition() throws OutOfBoardException, ColumnFullOfDIscsException {
        connect4.insertDisc(-1);
    }


    @Test
    public void testWhenDiscIsInsertedIntoAnEmptyColumn() throws OutOfBoardException, ColumnFullOfDIscsException {
        int column = 1;
        int row = connect4.insertDisc(column);
        assertThat(row, is(0));
    }


    @Test
    public void testWhenDiscIsInsertedIntoAnColumnWIthOneDisc() throws OutOfBoardException, ColumnFullOfDIscsException {
        int column = 1;
        connect4.insertDisc(column);
        int row = connect4.insertDisc(column);
        assertThat(row, is(1));
    }


    @Test
    public void testWhenDiscIsInsertedThenTotalDiscsIncreases() throws OutOfBoardException, ColumnFullOfDIscsException {
        int column = 1;
        int numberOfInsertedDiscs = 2;
        connect4.insertDisc(column);
        connect4.insertDisc(column);
        assertThat(numberOfInsertedDiscs, is(connect4.getNumberOfDiscs()));
    }


    @Test(expected = ColumnFullOfDIscsException.class)
    public void testWhenAColumnIsFullOfDiscs() throws OutOfBoardException, ColumnFullOfDIscsException {
        int column = 1;
        for (int i = 0; i <= Connect4.BOARD_ROWS; i++) {
            connect4.insertDisc(column);
        }
    }

    @Test
    public void testWhenBeforeStartGameThenIsRedColorTun() throws OutOfBoardException, ColumnFullOfDIscsException {
        Color color = connect4.getNextTurn();
        Color expectedColor = Color.redColor();
        assertThat(color, equalTo(expectedColor));
    }


    @Test
    public void testWhenAfterFirstInsertThenIsGreenColorTun() throws OutOfBoardException, ColumnFullOfDIscsException {
        int column = 1;
        connect4.insertDisc(column);
        Color color = connect4.getNextTurn();
        Color expectedColor = Color.greenColor();
        assertThat(color, equalTo(expectedColor));
    }


    @Test
    public void whenAskedForRedPlayerThenOutputNotice(){
        connect4.getNextTurn();
        String output = outputStream.toString();
        assertThat(output, containsString("R turn"));
    }


    @Test
    public void whenDiscInsertedThenShowFullBoardInOutput() throws ColumnFullOfDIscsException, OutOfBoardException {
        connect4.insertDisc(2);
        connect4.insertDisc(2);
        String boardOutput = outputStream.toString();
        assertThat(boardOutput, equalTo("| | | | | | | |\n" +
                                        "| | | | | | | |\n" +
                                        "| | | | | | | |\n" +
                                        "| | | | | | | |\n" +
                                        "| |G| | | | | |\n" +
                                        "| |R| | | | | |\n"));
    }


    @Test
    public void whenBoardIsEmptyThenGameIsntFinished(){
        assertFalse("Board must be empty before start playing", connect4.isFinished());
    }


    @Test
    public void whenBoardIsFullThenGameIsFinished() throws ColumnFullOfDIscsException, OutOfBoardException {
        for (int i=1;i<Connect4.BOARD_ROWS+1;i++){
            for (int j=1;j<Connect4.BOARD_COLUMNS+1;j++){
                connect4.insertDisc(j);
            }
        }
        assertTrue("The game must be finished", connect4.isFinished());
    }


    @Test
    public void whenTheGameStartsThenNoWinner(){
        assertThat(Connect4.NO_WINNER, equalTo(connect4.winner()));
    }


    @Test
    public void whenPutThreeVerticalDiscsThenNoWinner() throws ColumnFullOfDIscsException, OutOfBoardException {
        for (int i=0;i<3;i++) {
            connect4.insertDisc(1);//R
            connect4.insertDisc(2);//G
        }
        assertThat(Connect4.NO_WINNER, equalTo(connect4.winner()));
    }


    @Test
    public void whenPutFourGreenVerticalDiscsThenGreenWinner() throws ColumnFullOfDIscsException, OutOfBoardException {
        connect4.insertDisc(1);//R
        connect4.insertDisc(1);//G
        for (int i=0;i<3;i++) {
            connect4.insertDisc(2);//R
            connect4.insertDisc(1);//G
        }
        assertThat(connect4.winner(), equalTo(Connect4.GREEN_WINNER));
    }


    @Test
    public void whenPutThreeHorizontalDiscsThenNoWinner() throws ColumnFullOfDIscsException, OutOfBoardException {
        for (int i=1;i<4;i++) {
            connect4.insertDisc(i);//R
            connect4.insertDisc(i);//G
        }
        assertThat(Connect4.NO_WINNER, equalTo(connect4.winner()));
    }


    @Test
    public void whenPutFourHorizontalDiscsSeparatedByEmptyThenNoWinner() throws ColumnFullOfDIscsException, OutOfBoardException {
        for (int i=1;i<4;i++) {
            connect4.insertDisc(i);//R
            connect4.insertDisc(i);//G
        }
        connect4.insertDisc(5);
        assertThat(Connect4.NO_WINNER, equalTo(connect4.winner()));
    }


    @Test
    public void whenPutFourRedHorizontalDiscsThenRedWinner() throws ColumnFullOfDIscsException, OutOfBoardException {
        for (int i=1;i<5;i++) {
            connect4.insertDisc(i);//R
            connect4.insertDisc(i);//G
        }
        assertThat(connect4.winner(), equalTo(Connect4.RED_WINNER));
    }


    @Test
    public void whenMakeALeftToRightDiagonalOfThreeThenNoWinner() throws ColumnFullOfDIscsException, OutOfBoardException {
        connect4.insertDisc(1);//R
        connect4.insertDisc(2);//G
        connect4.insertDisc(2);//R
        connect4.insertDisc(3);//G
        connect4.insertDisc(4);//R
        connect4.insertDisc(3);//G
        connect4.insertDisc(3);//R
        assertThat(Connect4.NO_WINNER, equalTo(connect4.winner()));
    }


    @Test
    public void whenMakeARightToLeftDiagonalOfThreeThenNoWinner() throws ColumnFullOfDIscsException, OutOfBoardException {
        connect4.insertDisc(7);//R
        connect4.insertDisc(6);//G
        connect4.insertDisc(6);//R
        connect4.insertDisc(5);//G
        connect4.insertDisc(4);//R
        connect4.insertDisc(5);//G
        connect4.insertDisc(5);//R
        assertThat(Connect4.NO_WINNER, equalTo(connect4.winner()));
    }


    @Test
    public void whenMakeALeftToRightDiagonalOfFourThenRedWins() throws ColumnFullOfDIscsException, OutOfBoardException {
        connect4.insertDisc(1);//R
        connect4.insertDisc(2);//G
        connect4.insertDisc(2);//R
        connect4.insertDisc(3);//G
        connect4.insertDisc(4);//R
        connect4.insertDisc(3);//G
        connect4.insertDisc(3);//R
        connect4.insertDisc(4);//G
        connect4.insertDisc(5);//R
        connect4.insertDisc(4);//G
        connect4.insertDisc(4);//R
        assertThat(connect4.winner(), equalTo(Connect4.RED_WINNER));
    }


    @Test
    public void whenMakeARightToLeftDiagonalOfFourThenRedWins() throws ColumnFullOfDIscsException, OutOfBoardException {
        connect4.insertDisc(2);//R
        connect4.insertDisc(2);//G
        connect4.insertDisc(3);//R
        connect4.insertDisc(2);//G
        connect4.insertDisc(2);//R
        connect4.insertDisc(3);//G
        connect4.insertDisc(3);//R
        connect4.insertDisc(4);//G
        connect4.insertDisc(4);//R
        connect4.insertDisc(4);//G
        connect4.insertDisc(5);//R
        assertThat(connect4.winner(), equalTo(Connect4.RED_WINNER));
    }
}