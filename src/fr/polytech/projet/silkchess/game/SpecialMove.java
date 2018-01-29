package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.pieces.*;

import java.io.Serializable;

public class SpecialMove {
	/*
	NOTHING, // No Special Move
	CASTLING, // Move that involves the King and a Rook
	FIRST_MOVE, // A Pawn can go 2 tiles ahead
	PROMOTION, // A Pawn which is in the last row becomes a Queen
	EN_PASSANT // A Pawn kills enemies in diagonal
	*/
	
	/**
	 * Data to perform the "Castling" move. The first component is the king to move, and the second the rook to move
	 */
	public static Couple<King, Rook> IS_CASTLING = null;
	
	/**
	 * Data to perform the "En passant" move. The first component is the capturing pawn, and the second the captured
	 * pawn.
	 */
	public static Couple<Pawn, Pawn> IS_MOVING_EN_PASSANT = null;
	
	public static boolean checkIfCastlingIsPossible(@NotNull Chessboard board, @NotNull King king, @NotNull Rook rook, boolean checkCheck) throws NullPointerException {
		if (board == null || king == null || rook == null)
			throw new NullPointerException();
		
		// Check if the color of the rook is the same as the king's
		if (!king.getColor().equals(rook.getColor()))
			return false;
		
		// Check if the rook and the king has been moved before
		if (rook.hasMoved() || king.hasMoved())
			return false;
		
		// Check if the two pieces are aligned on an horizontal line
		if (!rook.getPosition().getY().equals(king.getPosition().getY()))
			return false;
		
		if (checkCheck && Check.checkIfPieceIsChecked(board, king))
			return false;
		
		// Check if there is a piece between them & if the tiles are checked
		boolean rookOnLeft;
		int i = CPoint.toPoint(rook.getPosition()).getX();
		
		if (i == CPoint.toPoint(king.getPosition()).getX())
			return false;
		
		rookOnLeft = i < CPoint.toPoint(king.getPosition()).getX();
		if (rookOnLeft)
			i++;
		else
			i--;
		
		while (i != CPoint.toPoint(king.getPosition()).getX())
		{
			// If a tile is checked by an enemy, return false
			if (checkCheck && Check.checkIfTileIsChecked(board, king.getColor(), new Point(i, CPoint.toPoint(rook.getPosition()).getY())))
				return false;
			
			// If a piece is found between the two pieces, return false
			if (!(board.get(i, CPoint.toPoint(rook.getPosition()).getY()) instanceof NoPiece))
				return false;
			
			if (rookOnLeft)
				i++;
			else
				i--;
		}
		
		IS_CASTLING = new Couple<>(king, rook);
		return true;
	}
	public static boolean checkIfCastlingIsPossible(@NotNull Chessboard board, @NotNull King king, @NotNull Rook rook) throws NullPointerException {
		return checkIfCastlingIsPossible(board, king, rook, true);
	}
	
	public static boolean checkIfFirstMoveIsPossible(@NotNull Pawn pawn) throws NullPointerException {
		if (pawn == null)
			throw new NullPointerException();
		
		return !pawn.hasMoved() && (pawn.getColor() == Color.BLACK ? pawn.getPosition().getY() == 7 : pawn.getPosition().getY() == 2);
	}
	
	/**
	 *
	 * @param board
	 * @param player
	 * @return Return the pawn which can be promoted. If {@code null} is returned, then no pawn must be promoted.
	 */
	public static @Nullable Pawn checkIfPromotionIsPossible(@NotNull Chessboard board, @NotNull Color player) throws NullPointerException {
		if (board == null || player == null)
			throw new NullPointerException();
		
		int y;
		if (player.equals(Color.WHITE))
			y = 0;
		else
			y = board.getNbRows() - 1;
		
		for (int i = 0; i < board.getNbColumns(); i++)
		{
			if (board.get(i, y) instanceof Pawn) {
				Pawn p = (Pawn) board.get(i, y);
				if (p.getColor().equals(player))
					return p;
			}
		}
		
		return null;
	}
	
	// TODO: Fix the bug that kill a piece at the other side of the chessboard!!!!
	
	/**
	 * Check if the "En Passant" move can be realise by {@code pawn} in the chessboard {@code board}. If the move is
	 * possible, update the static variable "IS_MOVING_EN_PASSANT"
	 * @param board The current chessboard
	 * @param pawn The pawn to test
	 * @param previousAction A couple which define the previous action done by the opposite player. Piece represents
	 *                       which piece has been played, and CPoint represent where does it come from (the source).
	 * @return Return true if the en passant move is possible for {@code pawn}, otherwise false.
	 */
	public static boolean checkIfEnPassantIsPossible(@NotNull Chessboard board, @NotNull Pawn pawn, @NotNull Couple<Piece, CPoint> previousAction) {
		if (board == null || pawn == null || previousAction == null)
			throw new NullPointerException();
		
		if (pawn.getPosition() == null || previousAction.getX() == null || previousAction.getY() == null)
			throw new NullPointerException();
		
		// If the capturing pawn is not in its 5th rank, return false
		if (!((pawn.getColor() == Color.BLACK && pawn.getPosition().getY() == 4) ||
			  (pawn.getColor() == Color.WHITE && pawn.getPosition().getY() == 5)))
			return false;
		
		// If in the previous action, the piece which was moved was not a pawn, then the "En passant" move cannot be
		// played.
		if (!(previousAction.getX() instanceof Pawn))
			return false;
		
		// If the current pawn and the pawn from the previous action are not y-aligned, return false
		if (!pawn.getPosition().getY().equals(previousAction.getX().getPosition().getY()))
			return false;
		
		// If the current pawn and the pawn from the previous action are x-aligned, return false
		if (pawn.getPosition().getX().equals(previousAction.getX().getPosition().getX()))
			return false;
		
		// If the pawn played in the previous action did not make a double-step move, then return false
		if (!((previousAction.getX().getColor() == Color.BLACK && CPoint.toPoint(previousAction.getY()).getY() + 2 == CPoint.toPoint(previousAction.getX().getPosition()).getY()) ||
			 (previousAction.getX().getColor() == Color.WHITE && CPoint.toPoint(previousAction.getY()).getY() - 2 == CPoint.toPoint(previousAction.getX().getPosition()).getY())))
			return false;
		
		/*if (pawn.getColor() == Color.BLACK) {
			//
		}
		else
		{
			//
		}*/
		
		IS_MOVING_EN_PASSANT = new Couple<>(pawn, (Pawn) previousAction.getX());
		
		return true;
	}
}
