package fr.polytech.projet.silkchess.game.pieces;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.util.ArrayList;

public class King extends Piece {
	
	public King(Color color, CPoint position) {
		super(color, position);
	}
	public King(Color color) {
		super(color);
	}
	public King(CPoint position) {
		super(position);
	}
	public King() {
		super();
	}
	
	@Override
	public ArrayList<CPoint> possibleMoves() {
		Point src = CPoint.toPoint(getPosition());
		
		// Compute all possible points
		ArrayList<Point> points = new ArrayList<>();
		points.add(new Point(src.getX() - 1, src.getY() - 1)); // Diagonal left-top
		points.add(new Point(src.getX(), src.getY() - 1)); // Top
		points.add(new Point(src.getX() + 1, src.getY() - 1)); // Diagonal right-top
		points.add(new Point(src.getX() + 1, src.getY())); // Right
		points.add(new Point(src.getX() + 1, src.getY() + 1)); // Diagonal right-bottom
		points.add(new Point(src.getX(), src.getY() + 1)); // Bottom
		points.add(new Point(src.getX() - 1, src.getY() + 1)); // Diagonal left-bottom
		points.add(new Point(src.getX() - 1, src.getY())); // Left
		
		deletePointsOutOfGrid(points);
		
		// Changing all Point to CPoint
		return PointsToCPoints(points);
	}
}
