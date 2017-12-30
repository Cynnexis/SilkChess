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
		Rook rook = new Rook(getColor(), getPosition());
		list.addAll(rook.possibleMoves());
		
		Bishop bishop = new Bishop(getColor(), getPosition());
		list.addAll(bishop.possibleMoves());
		
		deleteCPointsOutOfGrid(list);
		
		return list;
	}
}
