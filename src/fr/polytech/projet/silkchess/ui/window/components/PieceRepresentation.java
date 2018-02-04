package fr.polytech.projet.silkchess.ui.window.components;

import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.pieces.*;
import org.jetbrains.annotations.NotNull;

public class PieceRepresentation {
	
	private PieceRepresentation() { }
	
	//@Deprecated
	public static char getRepresentation(@NotNull Piece piece) {
		if (piece == null)
			throw new NullPointerException();
		
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
		else if (piece instanceof NoPiece)
			c = (char) 32;
		
		return c;
	}
	public static char getRepresentation(@NotNull Piece piece, @NotNull Color color) {
		if (color == null)
			throw new NullPointerException();
		
		char representation = getRepresentation(piece);
		
		if (color == Color.BLACK && !(piece instanceof NoPiece))
			representation = (char) (representation - 6);
		
		return representation;
	}
	public static char getRepresentation(@NotNull Class<? extends Piece> clazz) {
		if (clazz == null)
			throw new NullPointerException();
		
		char c = (char) 0;
		
		if (King.class.isAssignableFrom(clazz))
			c = (char) 9818;
		else if (Queen.class.isAssignableFrom(clazz))
			c = (char) 9819;
		else if (Rook.class.isAssignableFrom(clazz))
			c = (char) 9820;
		else if (Bishop.class.isAssignableFrom(clazz))
			c = (char) 9821;
		else if (Knight.class.isAssignableFrom(clazz))
			c = (char) 9822;
		else if (Pawn.class.isAssignableFrom(clazz))
			c = (char) 9823;
		else if (NoPiece.class.isAssignableFrom(clazz))
			c = (char) 32;
		
		return c;
	}
}
