package fr.polytech.projet.silkchess.ui.window.components;

import fr.polytech.projet.silkchess.game.pieces.*;

public class PieceRepresentation {
	
	public static char getRepresentation(Piece piece) {
		char c = (char) 0;
		
		if (piece instanceof King)
			c = (char) 9818;
		else if (piece instanceof Queen)
			c = (char) 9819;
		else if (piece instanceof Rook)
			c = (char) 9820;
		else if (piece instanceof Bishop)
			c = (char) 9821;
		else if (piece instanceof Knight)
			c = (char) 9822;
		else if (piece instanceof Pawn)
			c = (char) 9823;
		
		return c;
	}
}
