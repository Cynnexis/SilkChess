package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

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
	public boolean canMove(CPoint destination) {
		return false;
	}
}
