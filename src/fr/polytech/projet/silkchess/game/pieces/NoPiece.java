package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

public class NoPiece extends Piece {
	
	public NoPiece(Color color, CPoint position) {
		super(color, position);
	}
	public NoPiece(Color color) {
		super(color);
	}
	public NoPiece(CPoint position) {
		super(position);
	}
	public NoPiece() {
		super();
	}
	
	@Override
	public boolean canMove(CPoint destination) {
		return false;
	}
}
