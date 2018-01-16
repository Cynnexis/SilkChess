package fr.polytech.projet.silkchess.game;

import fr.polytech.projet.silkchess.game.pieces.Piece;

public interface EngineListener {
	
	void onPieceMoved(CPoint source, Piece piece);
	
	void onTokenChanged(Color token);
	
	void onGameStateChanged(GameState oldState, GameState newState);
	
	void onCheckStateChanged(CheckState state);
	
	void onPieceKilled(Piece pieceKilled);
}
