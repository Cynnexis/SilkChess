package fr.polytech.projet.silkchess.game.pieces;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.util.ArrayList;

public class Queen extends Piece {
	
	public Queen(Color color, CPoint position) {
		super(color, position);
	}
	public Queen(Color color) {
		super(color);
	}
	public Queen(CPoint position) {
		super(position);
	}
	public Queen() {
		super();
	}
	
	@Override
	public ArrayList<CPoint> possibleMoves() {
		ArrayList<CPoint> list = new ArrayList<>();
		Point src = CPoint.toPoint(getPosition());
		
		// Compute all possible points
		ArrayList<Point> points = new ArrayList<>();
		for (int i = 0; i < 8 ; i++) {
			if (src.getX() != i)
				points.add(new Point(i, src.getY())); // Row
			
			if (src.getY() != i)
				points.add(new Point(src.getX(), i)); // Column
			
			if (src.getX() != i && src.getY() != i) {
				points.add(new Point(i - (src.getY() - src.getX()), i - (src.getY() - src.getX()))); // Diagonal Decreasing
				points.add(new Point(src.getY() + src.getX() - i, src.getX() + src.getY() - i)); // Diagonal Increasing
			}
		}
		
		deletePointsOutOfGrid(points);
		
		// Changing all Point to CPoint
		return PointsToCPoints(points);
	}
}
