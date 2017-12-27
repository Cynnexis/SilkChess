package fr.polytech.projet.silkchess.ui.window.components;

import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import java.io.Serializable;

public interface TileListener extends Serializable {
	
	void onColorChanged(Color color);
	void onPieceChanged(Piece piece);
}
