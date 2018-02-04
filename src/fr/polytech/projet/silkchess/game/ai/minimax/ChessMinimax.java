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
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

/**
 * Source:
 *  * https://en.wikipedia.org/wiki/Minimax
 *  * https://medium.freecodecamp.org/simple-chess-ai-step-by-step-1d55a9266977
 * @author Valentin Berger
 */
public class ChessMinimax extends AbstractMinimax<ChessMinimaxParameters> {
	
	public @NotNull Node<ChessMinimaxParameters> constructNode(@NotNull Chessboard board, @NotNull Color token, int depth) {
		// Add one to depth because thr root is 'null'
		return constructNode(new Node<>(), board, token, ++depth);
	}
	public @NotNull Node<ChessMinimaxParameters> constructNode(@NotNull Node<ChessMinimaxParameters> node, @NotNull Chessboard board,
	                                                           @NotNull Color token, int depth) {
		if (depth <= 0)
			return null;
		
		if (node == null || board == null || token == null)
			throw new NullPointerException();
		
		// Create the instance of the tree
		//Node<ChessMinimaxParameters> root = new Node<>();
		
		// Get all player's pieces
		ArrayList<Piece> pieces = board.getAll(token);
		
		// For each piece
		for (Piece piece : pieces) {
			ArrayList<CPoint> possibilities;
			
			// Get all the possibilities of the piece
			try {
				possibilities = MoveManager.computeAllPossibleMoveWithCheck(board, piece);
			} catch (NoPieceException ignored) {
				possibilities = new ArrayList<>();
			}
			
			// Save the current position of the piece before moving it
			CPoint src = piece.getPosition();
			
			// For each possibility
			for (CPoint possibility : possibilities) {
				Piece copyPiece = null;
				
				// Try to create a new instance of piece (note: Piece is abstract)
				try {
					copyPiece = piece.getClass().newInstance();
					copyPiece.setColor(piece.getColor());
					copyPiece.setPosition(piece.getPosition());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				
				// If the piece has been fetched, make an heuristic move
				if (copyPiece != null) {
					// Copy the chessboard, and make the piece move to one possibility
					Chessboard copyBoard = new Chessboard(board);
					copyBoard.move(copyPiece, possibility);
					
					Color startPlayer = token;
					if (node.getParent() != null && node.getParent().getData() != null && node.getParent().getData().getStartPlayer() != null)
						startPlayer = node.getParent().getData().getStartPlayer();
					
					// Now that the node is created, either step down again (if depth != 1) or just add the chessboard
					// as a child
					ChessMinimaxParameters data = new ChessMinimaxParameters(copyBoard, copyPiece, src, startPlayer, token);
					Node<ChessMinimaxParameters> child = new Node<>(data);
					if (depth > 1)
						node.addChild(constructNode(child, copyBoard, Color.invert(token), depth - 1));
				}
			}
		}
		
		// Return the tree
		return node;
	}
	
	@Override
	public int compute(ChessMinimaxParameters data) {
		if (data == null)
			throw new NullPointerException();
		
		for (Object datum : data)
			if (datum == null)
				throw new NullPointerException();
		
		Chessboard board = data.getChessboard();
		Piece piece = data.getLastMovedPiece();
		CPoint source = data.getSource();
		Color startPlayer = data.getStartPlayer();
		Color token = data.getPlayer();
		
		int score = 0;
		for (int i = 0; i < board.getNbColumns(); i++) {
			for (int j = 0; j < board.getNbRows(); j++) {
				score += computePiece(board.get(i, j), startPlayer);
			}
		}
		
		return score;
	}
	
	@Contract(value = "null, _ -> fail; !null, null -> fail", pure = true)
	private int computePiece(@NotNull Piece piece, @NotNull Color token) {
		if (piece == null || token == null)
			throw new NullPointerException();
		
		int result = 0;
		if (piece instanceof King)
			result = 900;
		else if (piece instanceof Queen)
			result = 90;
		else if (piece instanceof Rook)
			result = 50;
		else if (piece instanceof Knight)
			result = 30;
		else if (piece instanceof Bishop)
			result = 30;
		else if (piece instanceof Pawn)
			result = 10;
		
		if (piece.getColor() == token)
			result = -result;
		
		return result;
	}
}
