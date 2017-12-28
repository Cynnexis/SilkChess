package fr.polytech.projet.silkchess.game;

import java.io.Serializable;

/**
 * Enumeration to define the check state of the game
 * @author Valentin Berger
 */
public enum CheckState implements Serializable {
	/**
	 * No check for now
	 */
	NO_CHECKSTATE,
	
	/**
	 * Black is losing
	 */
	B_CHECK,
	
	/**
	 * White is losing
	 */
	W_CHECK,
	
	/**
	 * Black lose
	 */
	B_CHECKMATE,
	
	/**
	 * White lose
	 */
	W_CHECKMATE,
	
	/**
	 * Black's King cannot move any longer, but it is not in the range of a White's piece
	 */
	B_DRAW,
	
	/**
	 * White's King cannot move any longer, but it is not in the range of a Black's piece
	 */
	W_DRAW
}
