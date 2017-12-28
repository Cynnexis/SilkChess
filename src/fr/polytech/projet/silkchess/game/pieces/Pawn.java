package fr.polytech.projet.silkchess.game.pieces;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.util.ArrayList;

public class Pawn extends Piece {
	
	public Pawn(Color color, CPoint position) {
		super(color, position);
	}
	public Pawn(Color color) {
		super(color);
	}
	public Pawn(CPoint position) {
		super(position);
	}
	public Pawn() {
		super();
	}
	
	@Override
	public ArrayList<CPoint> possibleMoves() {
		ArrayList<CPoint> list = new ArrayList<>();
		Point src = CPoint.toPoint(getPosition());
		
		// Compute all possible points
		ArrayList<Point> points = new ArrayList<>();
		// Top-right corner
		points.add(new Point(src.getX(), src.getY() + (getColor() == Color.BLACK ? +1 : -1)));
		
		deletePointsOutOfGrid(points);
		
		// Changing all Point to CPoint
		return PointsToCPoints(points);
	}
}
