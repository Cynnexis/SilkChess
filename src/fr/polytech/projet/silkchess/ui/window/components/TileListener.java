package fr.polytech.projet.silkchess.ui.window.components;

import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import java.io.Serializable;

/**
 * Listener interface for the class Tile
 * @author Valentin Berger
 * @see Tile
 */
public interface TileListener extends Serializable {
	
	/**
	 * Method called when the color of the tile is changed
	 * @param color The new color of the tile
	 */
	void onColorChanged(Color color);
	
	/**
	 * Method called when the piece of the tile is changed
	 * @param piece The new color of the tile
	 */
	void onPieceChanged(Piece piece);
	
	/**
	 * Method called when the tile is clicked or pressed by the mouse depending on the preferences (clicking or
	 * dropping).
	 * @param startTile The tile which has been clicked or pressed
	 */
	void onTileDropBegin(Tile startTile);
	
	/**
	 * Method called when the tile is released by the mouse if preferences is "Dropping mode"
	 * @param stopTile The tile which has been released
	 */
	void onTileDropStop(Tile stopTile);
}
