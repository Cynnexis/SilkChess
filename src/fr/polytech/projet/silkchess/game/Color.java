package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;

public enum Color {
	BLACK, WHITE;
	
	public static Color invert(@NotNull Color color) {
		if (color == null)
			return null;
		
		switch (color)
		{
			case BLACK:
				return WHITE;
			case WHITE:
				return BLACK;
			default:
				return null;
		}
	}
}
