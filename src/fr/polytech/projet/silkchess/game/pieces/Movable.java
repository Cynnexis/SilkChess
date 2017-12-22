package fr.polytech.projet.silkchess.game.pieces;

import fr.polytech.projet.silkchess.game.CPoint;

public interface Movable {
	
	/**
	 * Tell if the piece can move to the destination
	 * @param destination The destination in the board
	 * @return
	 */
	boolean canMove(CPoint destination);
}
