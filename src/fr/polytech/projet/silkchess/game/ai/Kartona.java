package fr.polytech.projet.silkchess.game.ai;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.exceptions.InfiniteLoopException;
import fr.berger.enhancedlist.tree.Node;
import fr.berger.enhancedlist.tree.Tree;
import fr.polytech.projet.silkchess.debug.Debug;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.MoveManager;
import fr.polytech.projet.silkchess.game.Player;
import fr.polytech.projet.silkchess.game.ai.minimax.ChessMinimax;
import fr.polytech.projet.silkchess.game.ai.minimax.ChessMinimaxParameters;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.pieces.Piece;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Random;

public class Kartona extends Player {
	
	@NotNull
	private IntelligenceMode mode = IntelligenceMode.RANDOM;
	
	public Kartona(@NotNull IntelligenceMode mode, @NotNull Color color) {
		super(color);
		
		if (mode == null || color == null)
			throw new NullPointerException();
		
		setMode(mode);
		setColor(color);
	}
	
	/**
	 * Make Kartona thonk about a move
	 * @param board The current chessboard
	 * @return Return a couple (piece, cpoint) where "piece" is the piece to move and "cpoint" its destination. If the
	 *         couple is however null, then it means that Kartona forfeits.
	 */
	public @Nullable Couple<Piece, CPoint> think(@NotNull Chessboard board) {
		if (board == null)
			throw new NullPointerException();
		
		Couple<Piece, CPoint> result = new Couple<>();
		
		ArrayList<Piece> pieces = board.getAll(getColor());
		ArrayList<Piece> foes = board.getAll(Color.invert(getColor()));
		
		switch (getMode())
		{
			case STRATEGY:
				ChessMinimax minimax = new ChessMinimax();
				Node<ChessMinimaxParameters> tree = minimax.constructNode(board, getColor(), 1);
				Couple<Integer, ChessMinimaxParameters> r = new Couple<>();
				try {
					r = minimax.minimax(tree);
					System.out.println("Kartona.think> Strategy result = " + r.toString());
				} catch (InfiniteLoopException ex) {
					// If the minimax throws an InfiniteLoopException, it means that no move is possible. Therefore,
					// Kartona must forfeit by returning 'null' value.
					return null;
				}
				
				Piece p = r.getY().getLastMovedPiece();
				p.setPosition(r.getY().getSource());
				result.setX(p);
				result.setY(r.getY().getLastMovedPiece().getPosition());
				break;
			default:
				Random rand = new Random(System.currentTimeMillis());
				Piece randomPiece;
				InfiniteLoopException ex = new InfiniteLoopException(100L);
				boolean playable = false;
				try {
					while (!playable) {
						randomPiece = pieces.get(rand.nextInt(pieces.size()));
						
						try {
							ArrayList<CPoint> possibleMoves = MoveManager.computeAllPossibleMoveWithCheck(board, randomPiece);
							if (possibleMoves.size() > 0) {
								result.setX(randomPiece);
								
								CPoint bestMove = null;
								for (int i = 0; i < possibleMoves.size() && bestMove == null; i++) {
									Piece dest = board.get(possibleMoves.get(i));
									if (dest.getColor() == Color.invert(getColor()))
										bestMove = possibleMoves.get(i);
								}
								
								if (bestMove == null)
									result.setY(possibleMoves.get(rand.nextInt(possibleMoves.size())));
								else
									result.setY(bestMove);
								
								playable = true;
							}
						} catch (NoPieceException e) {
							playable = false;
						}
						
						ex.increment();
					}
				} catch (InfiniteLoopException ex1) {
					// If the minimax throws an InfiniteLoopException, it means that no move is possible. Therefore,
					// Kartona must forfeit by returning 'null' value.
					result = null;
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
}
