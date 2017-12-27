package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

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
	public boolean canMove(CPoint destination) {
		return false;
	}
}
