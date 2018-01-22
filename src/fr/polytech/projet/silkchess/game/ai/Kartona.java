package fr.polytech.projet.silkchess.game.ai;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Couple;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.MoveManager;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.pieces.Piece;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Random;

public class Kartona {
	
	@NotNull
	private IntelligenceMode mode = IntelligenceMode.RANDOM;
	@NotNull
	private Color color = Color.BLACK;
	
	public Kartona(@NotNull IntelligenceMode mode, @NotNull Color color) {
		if (mode == null || color == null)
			throw new NullPointerException();
		
		setMode(mode);
		setColor(color);
	}
	
	public @NotNull Couple<Piece, CPoint> think(@NotNull Chessboard board) {
		if (board == null)
			throw new NullPointerException();
		
		Couple<Piece, CPoint> result = new Couple<>();
		
		ArrayList<Piece> pieces = board.getAll(color);
		ArrayList<Piece> foes = board.getAll(Color.invert(color));
		
		switch (getMode())
		{
			case STRATEGY:
				throw new NotImplementedException();
			default:
				Random rand = new Random(System.currentTimeMillis());
				Piece randomPiece;
				boolean playable = false;
				while (!playable) {
					randomPiece = pieces.get(rand.nextInt(pieces.size()));
					
					try {
						ArrayList<CPoint> possibleMoves = MoveManager.computeAllPossibleMoveWithCheck(board, randomPiece);
						if (possibleMoves.size() > 0) {
							result.setX(randomPiece);
							// TODO: Add a condition to eat a piece if it is possible
							result.setY(possibleMoves.get(rand.nextInt(possibleMoves.size())));
							playable = true;
						}
					} catch (NoPieceException e) {
						playable = false;
					}
				}
				break;
		}
		
		return result;
	}
	
	/* GETTERS & SETTERS */
	
	public IntelligenceMode getMode() {
		return mode;
	}
	
	public void setMode(IntelligenceMode mode) {
		this.mode = mode;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
