package fr.polytech.projet.silkchess.game.exceptions;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.pieces.Piece;

public class NoPieceException extends Exception {
	
	public NoPieceException(CPoint cpoint) {
		super("No piece at the coordinates (" + cpoint.getX() + " ; " + cpoint.getY() + ").");
	}
	public NoPieceException(Point point) {
		super("No piece at the coordinates (" + CPoint.fromPoint(point).getX() + " ; " + CPoint.fromPoint(point).getY() + ").");
	}
}
