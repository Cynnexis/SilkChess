package fr.polytech.projet.silkchess.ui.window.components;

import com.sun.istack.internal.Nullable;

public interface JChessboardListener {
	
	void onSelectedTileChanged(@Nullable Tile tile);
}
