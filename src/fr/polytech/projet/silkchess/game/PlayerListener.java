package fr.polytech.projet.silkchess.game;

import fr.polytech.projet.silkchess.game.pieces.Piece;

public interface PlayerListener {
	
	void onColorChanged(Color color);
	
	void onNbRoundChanged(int nbRound);
	
	void onKilledEnemyAdded(Piece killedEnemy);
}
