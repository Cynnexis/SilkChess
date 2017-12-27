package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

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
	public boolean canMove(CPoint destination) {
		return false;
	}
}
