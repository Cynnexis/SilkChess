package fr.polytech.projet.silkchess.game.pieces;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.util.ArrayList;

public class Rook extends Piece {
	
	public Rook(Color color, CPoint position) {
		super(color, position);
	}
	public Rook(Color color) {
		super(color);
	}
	public Rook(CPoint position) {
		super(position);
	}
	public Rook() {
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
		}
		
		deletePointsOutOfGrid(points);
		
		// Changing all Point to CPoint
		return PointsToCPoints(points);
	}
}
