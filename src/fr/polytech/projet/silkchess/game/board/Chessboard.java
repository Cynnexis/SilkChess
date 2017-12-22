package fr.polytech.projet.silkchess.game.board;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Matrix;
import fr.polytech.projet.silkchess.game.pieces.NoPiece;
import fr.polytech.projet.silkchess.game.pieces.Piece;

public class Chessboard extends Matrix<Piece> {
	
	public Chessboard() {
		super(8, 8, new NoPiece());
		
		// TODO: Initialize the game by placing the pieces at the right place. Use a 'newGame' method for that?
	}
	
	public Piece get(char x, int y) {
		return this.get('A' - x, y - 1);
	}
	
	public boolean set(char x, int y, Piece value) {
		return this.set('A' - x, y - 1, value);
	}
	
	public <T extends Piece> T search(T piece) {
		//if (piece instanceof )
		return null;
	}
}
