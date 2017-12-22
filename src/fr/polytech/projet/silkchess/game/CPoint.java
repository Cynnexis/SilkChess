package fr.polytech.projet.silkchess.game;

import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.Point;

public class CPoint extends Couple<Character, Integer> {
	
	public CPoint(char x, int y) {
		super(x, y);
	}
	public CPoint() {
		super();
	}
	
	public Point convertToPoint() {
		return new Point('A' - getX(), getY());
	}
}
