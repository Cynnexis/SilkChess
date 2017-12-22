package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

public class Pawn extends Piece {
	
	public Pawn(Color color, CPoint position) {
		super(color, position);
	}
	public Pawn(Color color) {
		super(color);
	}
	public Pawn() {
		super();
	}
	
	@Override
	public boolean canMove(CPoint destination) {
		return false;
	}
}
