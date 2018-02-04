package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import java.util.ArrayList;

/**
 * Player is a class which manage a player of the chess game.
 * @author Valentin Berger
 */
public class Player {
	
	@NotNull
	private Color color = Color.BLACK;
	private int nbRound = 0;
	@NotNull
	private ArrayList<Piece> killedEnemies = new ArrayList<>();
	
	@NotNull
	private PlayerListener playerListener = new PlayerListener() {
		@Override
		public void onColorChanged(Color color) { }
		@Override
		public void onNbRoundChanged(int nbRound) { }
		@Override
		public void onKilledEnemyAdded(Piece killedEnemy) { }
	};
	
	public Player(@NotNull Color color) {
		setColor(color);
		setNbRound(0);
		setKilledEnemies(new ArrayList<>());
	}
	
	public boolean kill(Piece piece) {
		if (piece == null)
			return false;
		
		if (piece.getColor() == Color.invert(getColor())) {
			killedEnemies.add(piece);
			this.playerListener.onKilledEnemyAdded(piece);
			return true;
		}
		
		return false;
	}
	
	/* GETTERS & SETTERS */
	
	public @NotNull Color getColor() {
		return color;
	}
	
	public void setColor(@NotNull Color color) {
		if (color != null) {
			this.color = color;
			this.playerListener.onColorChanged(this.color);
		}
	}
	
	public int getNbRound() {
		return nbRound;
	}
	
	public void setNbRound(int nbRound) {
		if (nbRound >= 0) {
			this.nbRound = nbRound;
			this.playerListener.onNbRoundChanged(nbRound);
		}
	}
	
	public @NotNull ArrayList<Piece> getKilledEnemies() {
		return killedEnemies;
	}
	
	public void setKilledEnemies(@NotNull ArrayList<Piece> killedEnemies) {
		if (killedEnemies != null)
			this.killedEnemies = killedEnemies;
	}
	
	public @NotNull PlayerListener getPlayerListener() {
		return playerListener;
	}
	
	public void setPlayerListener(@NotNull PlayerListener playerListener) {
		this.playerListener = playerListener != null ? playerListener : new PlayerListener() {
			@Override
			public void onColorChanged(Color color) { }
			@Override
			public void onNbRoundChanged(int nbRound) { }
			@Override
			public void onKilledEnemyAdded(Piece killedEnemy) { }
		};
	}
}
