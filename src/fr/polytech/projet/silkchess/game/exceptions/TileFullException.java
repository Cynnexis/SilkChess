package fr.polytech.projet.silkchess.game.exceptions;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;

public class TileFullException extends Exception {
	
	public TileFullException(CPoint cpoint) {
		super("The tile (" + cpoint.getX() + " ; " + cpoint.getY() + ") already contains a piece.");
	}
	public TileFullException(Point point) {
		super("The tile (" + CPoint.fromPoint(point).getX() + " ; " + CPoint.fromPoint(point).getY() + ") already contains a piece.");
	}
}
