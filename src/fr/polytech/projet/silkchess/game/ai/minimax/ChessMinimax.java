package fr.polytech.projet.silkchess.game.ai.minimax;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.exceptions.InfiniteLoopException;
import fr.berger.enhancedlist.tree.Node;
import fr.berger.enhancedlist.tree.Tree;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.MoveManager;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.pieces.*;

import java.util.ArrayList;

/**
 * Source:
 *  * https://en.wikipedia.org/wiki/Minimax
 *  * https://medium.freecodecamp.org/simple-chess-ai-step-by-step-1d55a9266977
 *
 */
public class ChessMinimax extends AbstractMinimax<ChessMinimaxParameters> {
	
	/*
	@Deprecated
	public Tree<Chessboard> constructTree(@NotNull Chessboard board, @NotNull Color token, int depth) {
		Node<ChessMinimaxParameters> nodes = constructNode(board, token, depth);
		//
	}
	*/
	
	public @NotNull Node<ChessMinimaxParameters> constructNode(@NotNull Chessboard board, @NotNull Color token, int depth) {
		if (depth <= 0)
			return null;
		
		if (board == null || token == null)
			throw new NullPointerException();
		
		// Create the instance of the tree
		Node<ChessMinimaxParameters> root = new Tree<>();
		
		// Get all player's pieces
		ArrayList<Piece> pieces = board.getAll(token);
		
		for (Piece piece : pieces) {
			ArrayList<CPoint> possibilities;
			try {
				possibilities = MoveManager.computeAllPossibleMoveWithCheck(board, piece);
			} catch (NoPieceException ignored) {
				possibilities = new ArrayList<>();
			}
			
			Piece copyPiece = null;
			try {
				copyPiece = piece.getClass().newInstance();
				copyPiece.setColor(piece.getColor());
				copyPiece.setPosition(piece.getPosition());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if (copyPiece != null) {
				CPoint src = piece.getPosition();
				
				for (CPoint possibility : possibilities) {
					Chessboard copyBoard = new Chessboard(board);
					copyBoard.move(copyPiece, possibility);
					
					// Now that the node is created, either step down again (if depth != 1) or just add the chessboard
					// as a child
					if (depth > 1)
						root.addChild(constructNode(copyBoard, token, depth - 1));
					else
						root.addChild(new ChessMinimaxParameters(copyBoard, piece, src, token));
				}
			}
		}
		
		return root;
	}
	
	@Override
	public int compute(ChessMinimaxParameters data) {
		if (data == null)
			throw new IllegalArgumentException();
		
		for (Object datum : data)
			if (datum == null)
				throw new IllegalArgumentException();
		
		Chessboard board = data.getChessboard();
		Piece piece = data.getLastMovedPiece();
		CPoint source = data.getSource();
		Color token = data.getPlayer();
		
		int score = 0;
		if (piece instanceof King) {
			score = 900;
		}
		else if (piece instanceof Queen) {
			score = 90;
		}
		else if (piece instanceof Rook) {
			score = 50;
		}
		else if (piece instanceof Knight) {
			score = 30;
		}
		else if (piece instanceof Bishop) {
			score = 30;
		}
		else if (piece instanceof Pawn) {
			score = 10;
		}
		else {
			score = 0;
		}
		
		if (piece.getColor() == Color.invert(token))
			score = -score;
		
		return score;
	}
}
