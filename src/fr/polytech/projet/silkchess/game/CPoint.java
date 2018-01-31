package fr.polytech.projet.silkchess.game;

import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.Point;

import java.io.Serializable;

public class CPoint extends Couple<Character, Integer> implements Serializable {
	
	public CPoint(char x, int y) {
		super(x, y);
	}
	public CPoint() {
		super();
	}
	
	public static Point toPoint(CPoint cpoint) {
		if (cpoint == null || cpoint.getX() == null || cpoint.getY() == null)
			return null;
		return new Point((int) (cpoint.getX() - 'A'), 8 - cpoint.getY());
	}
	
	public static CPoint fromPoint(Point point) {
		if (point == null)
			return null;
		return new CPoint((char) (point.getX() + 'A'), 8 - point.getY());
	}
	public static CPoint fromPoint(int x, int y) {
		return fromPoint(new Point(x, y));
	}
	
	@Override
	public String toString() {
		return "(" + (getX() != null ? getX().toString() : "(null)") + " ; " + (getY() != null ? getY().toString() : "(null)") + ")";
	}
}
