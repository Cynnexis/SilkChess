package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

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
	public boolean canMove(CPoint destination) {
		return false;
	}
}
