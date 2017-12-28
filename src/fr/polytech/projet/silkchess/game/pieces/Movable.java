package fr.polytech.projet.silkchess.game.pieces;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;

import java.util.ArrayList;

public interface Movable {
	
	ArrayList<CPoint> possibleMoves();
	
	default void deletePointsOutOfGrid(ArrayList<Point> points) {
		// Delete points out of the grid
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if ((p.getX() < 0 || p.getX() >= 8) || (p.getY() < 0 || p.getY() >= 8)) {
				points.remove(i);
				i--;
			}
		}
	}
	
	default ArrayList<CPoint> PointsToCPoints(ArrayList<Point> points) {
		ArrayList<CPoint> list = new ArrayList<>(points.size());
		
		for (Point p : points)
			list.add(CPoint.fromPoint(p));
		
		return list;
	}
	
	default ArrayList<Point> CPointsToPoints(ArrayList<CPoint> cpoints) {
		ArrayList<Point> list = new ArrayList<>(cpoints.size());
		
		for (CPoint c : cpoints)
			list.add(CPoint.toPoint(c));
		
		return list;
	}
}
