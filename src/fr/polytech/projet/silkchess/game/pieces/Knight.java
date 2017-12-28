package fr.polytech.projet.silkchess.game.pieces;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.util.ArrayList;

public class Knight extends Piece {
	
	public Knight(Color color, CPoint position) {
		super(color, position);
	}
	public Knight(Color color) {
		super(color);
	}
	public Knight(CPoint position) {
		super(position);
	}
	public Knight() {
		super();
	}
	
	@Override
	public ArrayList<CPoint> possibleMoves() {
		ArrayList<CPoint> list = new ArrayList<>();
		Point src = CPoint.toPoint(getPosition());
		
		// Compute all possible points
		ArrayList<Point> points = new ArrayList<>();
		// Top-right corner
		points.add(new Point(src.getX() + 1, src.getY() - 2));
		points.add(new Point(src.getX() + 2, src.getY() - 1));
		
		// Bottom-right corner
		points.add(new Point(src.getX() + 2, src.getY() + 1));
		points.add(new Point(src.getX() + 1, src.getY() + 2));
		
		// Bottom-left corner
		points.add(new Point(src.getX() - 1, src.getY() + 2));
		points.add(new Point(src.getX() - 2, src.getY() + 1));
		
		// Top-left corner
		points.add(new Point(src.getX() - 2, src.getY() - 1));
		points.add(new Point(src.getX() - 1, src.getY() - 2));
		
		deletePointsOutOfGrid(points);
		
		// Changing all Point to CPoint
		return PointsToCPoints(points);
	}
}
