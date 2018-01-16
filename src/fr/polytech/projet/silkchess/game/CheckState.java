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
	 * Black loses
	 */
	B_CHECKMATE,
	
	/**
	 * White loses
	 */
	W_CHECKMATE,
	
	/**
	 * If a player has no piece but a king, and this king is not in check BUT it cannot moved without being in check,
	 * then it's a stalemate (a draw). There is no winner, no loser.
	 */
	STALEMATE
}
