package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.pieces.King;
import fr.polytech.projet.silkchess.game.pieces.NoPiece;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class Check {
	
	public static boolean checkIfTileIsChecked(@NotNull Chessboard board, @NotNull Color color, @NotNull CPoint position) throws NullPointerException {
		if (board == null || color == null || position == null)
			throw new NullPointerException();
		
		// Get all the enemies from the board (except NoPiece pieces)
		ArrayList<Piece> foes = new ArrayList<>();
		for (int i = 0; i < board.getNbColumns(); i++)
			for (int j = 0; j < board.getNbRows(); j++)
				if (board.get(i, j) != null && !(board.get(i, j) instanceof NoPiece) && board.get(i, j).getColor() != color)
					foes.add(board.get(i, j));
		
		for (Piece p : foes) {
			try {
				if (MoveManager.computeAllPossibleMoveWithoutCheck(board, color, p.getPosition()).contains(position))
					return true;
			} catch (NoPieceException ex) {
				ex.printStackTrace();
			}
		}
		
		return false;
	}
	public static boolean checkIfTileIsChecked(@NotNull Chessboard board, @NotNull Color color, @NotNull Point position) {
		return checkIfTileIsChecked(board, color, CPoint.fromPoint(position));
	}
	
	public static boolean checkIfPieceIsChecked(@NotNull Chessboard board, @NotNull Piece piece) {
		return checkIfTileIsChecked(board, piece.getColor(), piece.getPosition());
	}
	
	public static @NotNull CheckState checkCheck(@NotNull Chessboard board) throws NullPointerException {
		if (board == null)
			throw new NullPointerException();
		
		CheckState state = CheckState.NO_CHECKSTATE;
		
		// Get all the kings from the board
		ArrayList<King> kings = new ArrayList<>(2);
		for (int i = 0; i < board.getNbColumns(); i++)
			for (int j = 0; j < board.getNbRows(); j++)
				if (board.get(i, j) instanceof King)
					kings.add((King) board.get(i, j));
		
		for (int i = 0; i < kings.size() && state == CheckState.NO_CHECKSTATE; i++)
			if (checkIfPieceIsChecked(board, kings.get(i)))
				state = kings.get(i).getColor() == Color.BLACK ? CheckState.B_CHECK : CheckState.W_CHECK;
		
		/**
		 * How a checkmate/stalemate is detected:
		 * CHECKMATE: the king is in check, all tiles around him are in checks, and no ally can help him
		 * STALEMATE: the king IS NOT in check, all tiles around him are in checks, and no ally can help him
		 */
		
		// Check stalemate
		ArrayList<Piece> blacks = board.getAll(Color.BLACK);
		ArrayList<Piece> whites = board.getAll(Color.WHITE);
		if (blacks.size() == 1 && whites.size() == 1 && blacks.get(0) instanceof King && whites.get(0) instanceof King)
			return CheckState.STALEMATE;
		
		
		// Compute all possible chessboard for every position of every piece. If no such chessboard exist, checkmate!
		
		// For each colors
		Color currentColor;
		for (int i = 0; i < Color.values().length ; i++) {
			currentColor = Color.values()[i];
			ArrayList<Piece> allies = board.getAll(currentColor);
			
			boolean noMovePossible = true;
			for (int j = 0; j < allies.size() && noMovePossible; j++) {
				Piece ally = allies.get(j);
				if (!(ally instanceof NoPiece))
					if (MoveManager.computeAllPossibleMoveWithCheck(board, ally).size() > 0)
						noMovePossible = false;
			}
			
			if (noMovePossible) {
				state = currentColor == Color.BLACK ? CheckState.W_CHECKMATE : CheckState.B_CHECKMATE;
				break;
			}
		}
		
		return state;
	}
	
	private static boolean areAllTilesAroundKingChecked(@NotNull Chessboard board, @NotNull King king) throws NoPieceException {
		ArrayList<CPoint> list = MoveManager.computeAllPossibleMoveWithoutCheck(board, king);
		
		boolean tileNotCheckedDetected = false;
		for (int i = 0; i < list.size() && !tileNotCheckedDetected; i++)
			// checkedKing.getColor() cannot not be null
			//noinspection ConstantConditions
			if (!checkIfTileIsChecked(board, king.getColor(), list.get(i)))
				tileNotCheckedDetected = true;
		
		return !tileNotCheckedDetected;
	}
}
