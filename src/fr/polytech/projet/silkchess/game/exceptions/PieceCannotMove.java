package fr.polytech.projet.silkchess.game.exceptions;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.pieces.Piece;

public class PieceCannotMove extends Exception {
	
	public PieceCannotMove(Piece piece, CPoint dest) {
		super("The piece " + piece.toString() + " cannot move to (" + dest.getX() + " ; " + dest.getY() + ").");
	}
	public PieceCannotMove(Piece piece, Point dest) {
		super("The piece " + piece.toString() + " cannot move to (" + CPoint.fromPoint(dest).getX() + " ; " + CPoint.fromPoint(dest).getY() + ").");
	}
}
