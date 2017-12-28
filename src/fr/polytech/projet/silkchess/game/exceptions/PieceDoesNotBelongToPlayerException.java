package fr.polytech.projet.silkchess.game.exceptions;

import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.pieces.Piece;

public class PieceDoesNotBelongToPlayerException extends Exception {
	
	public PieceDoesNotBelongToPlayerException(Piece piece, Color player) {
		super("The piece " + piece + " does not belong to player " + player.name() + ".");
	}
}
