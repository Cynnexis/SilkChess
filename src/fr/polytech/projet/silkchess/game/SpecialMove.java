package fr.polytech.projet.silkchess.game;

import java.io.Serializable;

public enum SpecialMove implements Serializable {
	NOTHING, // No Special Move
	CASTLING, // Move that involves the King and a Rook
	FIRST_MOVE, // A Pawn can go 2 tiles ahead
	PROMOTION, // A Pawn which is in the last row becomes a Queen
	EN_PASSANT // A Pawn kills enemies in diagonal
}
