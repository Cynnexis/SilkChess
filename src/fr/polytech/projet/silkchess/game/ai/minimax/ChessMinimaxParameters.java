package fr.polytech.projet.silkchess.game.ai.minimax;

import com.sun.istack.internal.NotNull;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;

public class ChessMinimaxParameters implements Serializable, Iterable<Object> {
	
	@NotNull
	private Chessboard chessboard;
	@NotNull
	private Piece lastMovedPiece;
	@NotNull
	private CPoint source;
	@NotNull
	private Color startPlayer;
	@NotNull
	private Color player;
	
	/* CONSTRUCTOR */
	
	public ChessMinimaxParameters(@NotNull Chessboard chessboard, @NotNull Piece lastMovedPiece, @NotNull CPoint source, @NotNull Color startPlayer, @NotNull Color player) {
		setChessboard(chessboard);
		setLastMovedPiece(lastMovedPiece);
		setSource(source);
		setStartPlayer(startPlayer);
		setPlayer(player);
	}
	
	/* METHOD */
	
	public @NotNull Object get(int i) {
		switch (i) {
			case 0:
				return getChessboard();
			case 1:
				return getLastMovedPiece();
			case 2:
				return getSource();
			case 3:
				return getStartPlayer();
			case 4:
				return getPlayer();
			default:
				return null;
		}
	}
	
	/* GETTERS & SETTERS */
	
	public @NotNull Chessboard getChessboard() {
		return chessboard;
	}
	
	public void setChessboard(@NotNull Chessboard chessboard) {
		if (chessboard == null)
			throw new NullPointerException();
		
		this.chessboard = chessboard;
	}
	
	public @NotNull Piece getLastMovedPiece() {
		return lastMovedPiece;
	}
	
	public void setLastMovedPiece(@NotNull Piece lastMovedPiece) {
		if (lastMovedPiece == null)
			throw new NullPointerException();
		
		this.lastMovedPiece = lastMovedPiece;
	}
	
	public @NotNull CPoint getSource() {
		return source;
	}
	
	public void setSource(@NotNull CPoint source) {
		if (source == null)
			throw new NullPointerException();
		
		this.source = source;
	}
	
	public @NotNull Color getStartPlayer() {
		return startPlayer;
	}
	
	public void setStartPlayer(@NotNull Color startPlayer) {
		if (startPlayer == null)
			throw new NullPointerException();
		
		this.startPlayer = startPlayer;
	}
	
	public @NotNull Color getPlayer() {
		return player;
	}
	
	public void setPlayer(@NotNull Color player) {
		if (player == null)
			throw new NullPointerException();
		
		this.player = player;
	}
	
	/* OVERRIDES */
	
	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {
			
			private int index = 0;
			
			@Override
			public boolean hasNext() {
				return index < 5;
			}
			
			@Override
			public Object next() {
				return get(index++);
			}
		};
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ChessMinimaxParameters)) return false;
		ChessMinimaxParameters objects = (ChessMinimaxParameters) o;
		return Objects.equals(getChessboard(), objects.getChessboard()) &&
				Objects.equals(getLastMovedPiece(), objects.getLastMovedPiece()) &&
				Objects.equals(getSource(), objects.getSource()) &&
				getStartPlayer() == objects.getStartPlayer() &&
				getPlayer() == objects.getPlayer();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getChessboard(), getLastMovedPiece(), getSource(), getStartPlayer(), getPlayer());
	}
	
	@Override
	public String toString() {
		return "ChessMinimaxParameters{" +
				"chessboard=" + chessboard +
				", lastMovedPiece=" + lastMovedPiece +
				", source=" + source +
				", startPlayer=" + startPlayer +
				", player=" + player +
				'}';
	}
}
