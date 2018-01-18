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
		
		// TODO: Fix this bug
		// If a check is detected, check checkmate
		/*if (state == CheckState.B_CHECK || state == CheckState.W_CHECK)
		{
			// Get all tiles around the checked king
			King checkedKing = null;
			
			for (int i = 0; i < kings.size() && checkedKing == null; i++) {
				if (kings.get(i) != null && (
					(kings.get(i).getColor() == Color.WHITE && state == CheckState.W_CHECK) ||
					(kings.get(i).getColor() == Color.BLACK && state == CheckState.B_CHECK)))
					checkedKing = kings.get(i);
			}
			
			try {
				boolean result = areAllTilesAroundKingChecked(board, checkedKing);
				
				if (result)
					//noinspection ConstantConditions
					state = checkedKing.getColor() == Color.BLACK ? CheckState.B_CHECKMATE : CheckState.W_CHECKMATE;
			} catch (NoPieceException ex) {
				ex.printStackTrace();
			}
		}*/
		// If a check is not detected, then check stalemate
		/*else
		{
			boolean stalemate = false;
			int i = 0;
			for (; i < kings.size() && !stalemate; i++) {
				try {
					if (!isThereOneTileNotCheckedAroundKing(board, kings.get(i)))
						stalemate = true;
				} catch (NoPieceException ex) {
					ex.printStackTrace();
				}
			}
			
			if (stalemate) {
				if (i >= kings.size())
					state = kings.get(kings.size() - 1).getColor() == Color.BLACK ? CheckState.B_CHECKMATE : CheckState.W_CHECKMATE;
				else
					state = kings.get(i).getColor() == Color.BLACK ? CheckState.B_CHECKMATE : CheckState.W_CHECKMATE;
			}
			
		}*/
		
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
		
		return tileNotCheckedDetected;
	}
}
