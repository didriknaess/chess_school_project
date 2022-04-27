package chess.io;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.datamodel.GameState;
import chess.datamodel.Piece;
import chess.datamodel.Piece.Color;

public class BoardIOTest {
    
    private GameState game;
    private GameState loadGame; //Needs to be declared, even though VS Code says it doesnt
    private BoardIO boardIO;

    private void createGame() {
        ArrayList<Piece> pieces = new ArrayList<>(
            Arrays.asList(
            Piece.createNewPiece("pa7"), Piece.createNewPiece("pb7"), Piece.createNewPiece("pc7"), Piece.createNewPiece("pd7"), Piece.createNewPiece("pe7"), Piece.createNewPiece("pf7"), Piece.createNewPiece("pg7"), Piece.createNewPiece("ph7"),
            Piece.createNewPiece("Pa2"), Piece.createNewPiece("Pb2"), Piece.createNewPiece("Pc2"), Piece.createNewPiece("Pd2"), Piece.createNewPiece("Pe2"), Piece.createNewPiece("Pf2"), Piece.createNewPiece("Pg2"), Piece.createNewPiece("Ph2"),
            Piece.createNewPiece("ra8"), Piece.createNewPiece("rh8"), Piece.createNewPiece("Rh1"), Piece.createNewPiece("Ra1"), Piece.createNewPiece("nb8"), Piece.createNewPiece("ng8"), Piece.createNewPiece("Nb1"), Piece.createNewPiece("Ng1"),
            Piece.createNewPiece("bc8"), Piece.createNewPiece("bf8"), Piece.createNewPiece("Bc1"), Piece.createNewPiece("Bf1"), Piece.createNewPiece("qd8"), Piece.createNewPiece("Qd1"), Piece.createNewPiece("ke8"), Piece.createNewPiece("Ke1")
        ));
        for (Piece piece : pieces) {
            piece.setFirstTurnMoved(-1);
            this.game.addPiece(piece);
        }
        this.game.setSecondsRemainingBlack(600);
        this.game.setSecondsRemainingWhite(600);
        this.game.setWhoseTurn(Color.WHITE);
    }

    @BeforeEach
    public void setup() {
        game = new GameState();
        boardIO = new BoardIO();
        createGame();
    }

    @Test
    @DisplayName("Testing both saving and loading a game togheter. See this as the most appropriate solution")
    public void testBoardIO() {
        GameState loadGame;

        try {
            boardIO.saveFile("testing.txt", game);
        } catch (Exception e) {
            fail("Could not save game");
            System.out.println(e.getStackTrace());
            return;
        }

        try {
            loadGame = boardIO.loadFile("testing.txt"); 
        } catch (Exception e) {
            fail("Could not load game");
            System.out.println(e.getStackTrace());
            return;
        }

        assertTrue(loadGame.isValid());
        assertTrue(game.equals(loadGame));
    }

    @Test
	public void testLoadNonExistingFile() {
        assertThrows(
				FileNotFoundException.class, () -> 
                loadGame = boardIO.loadFile("hey"),
                "FileNotFoundException should be thrown when file does not exist!"
                );
	}

	@Test
	public void testLoadInvalidFile() {
		assertThrows(
				Exception.class, () -> 
                loadGame = boardIO.loadFile("InvalidChess.txt"),
				"An exception should be thrown if loaded file is invalid!");
	}

	@AfterAll
	static void teardown() {
		File newTestSaveFile = new File(BoardIO.getFilePath("testing.txt"));
		newTestSaveFile.delete();
	}


}
