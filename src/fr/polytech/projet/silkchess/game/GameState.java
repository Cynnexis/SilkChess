package fr.polytech.projet.silkchess.game;

import java.io.Serializable;

public enum GameState implements Serializable {
	INITIALIZING,
	PLAYING,
	PAUSE,
	B_WIN,
	W_WIN
}
