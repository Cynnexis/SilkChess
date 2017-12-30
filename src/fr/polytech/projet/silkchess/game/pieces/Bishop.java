package fr.polytech.projet.silkchess.game.pieces;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.util.ArrayList;

public class Bishop extends Piece {
	
	public Bishop(Color color, CPoint position) {
		super(color, position);
	}
	public Bishop(Color color) {
		super(color);
	}
	public Bishop(CPoint position) {
		super(position);
	}
	public Bishop() {
		super();
	}
	
	@Override
	public ArrayList<CPoint> possibleMoves() {
		ArrayList<CPoint> list = new ArrayList<>();
		Point src = CPoint.toPoint(getPosition());
		
		// Compute all possible points
		ArrayList<Point> points = new ArrayList<>();
		
		/* Diagonal Decreasing */
		for (int i = src.getX() + 1, j = src.getY() + 1; i <= 8 && j <= 8; i++, j++)
			points.add(new Point(i, j));
		
		for (int i = src.getX() - 1, j = src.getY() - 1; i >= 0 && j >= 0; i--, j--)
			points.add(new Point(i, j));
		
		/* Diagonal Increasing */
		for (int i = src.getX() + 1, j = src.getY() - 1; i <= 8 && j <= 8; i++, j--)
			points.add(new Point(i, j));
		
		for (int i = src.getX() - 1, j = src.getY() + 1; i >= 0 && j >= 0; i--, j++)
			points.add(new Point(i, j));
		
		deletePointsOutOfGrid(points);
		
		// Changing all Point to CPoint
		return PointsToCPoints(points);
	}
}
