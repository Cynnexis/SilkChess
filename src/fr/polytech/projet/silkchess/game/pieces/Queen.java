package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

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
	public boolean canMove(CPoint destination) {
		return false;
	}
}
