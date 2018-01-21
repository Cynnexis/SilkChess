package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.pieces.King;
import fr.polytech.projet.silkchess.game.pieces.NoPiece;
import fr.polytech.projet.silkchess.game.pieces.Pawn;
import fr.polytech.projet.silkchess.game.pieces.Rook;

import java.io.Serializable;

public class SpecialMove {
	/*
	NOTHING, // No Special Move
	CASTLING, // Move that involves the King and a Rook
	FIRST_MOVE, // A Pawn can go 2 tiles ahead
	PROMOTION, // A Pawn which is in the last row becomes a Queen
	EN_PASSANT // A Pawn kills enemies in diagonal
	*/
	
	public static Couple<King, Rook> IS_CASTLING = null;
	
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
		
		// TODO: Check if the king is in check AND all the cases between the rook and the king are not check too
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
		
		return !pawn.hasMoved();
	}
	
	/**
	 *
	 * @param board
	 * @param player
	 * @return Return the index of the pawn which can be promoted. If {@code -1} is returned, then no pawn must be
	 * promoted.
	 */
	public static int checkIfPromotionIsPossible(@NotNull Chessboard board, @NotNull Color player) throws NullPointerException {
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
					return i;
			}
		}
		
		return -1;
	}
	
	public static boolean checkIfEnPassantIsPossible(Chessboard board, Pawn pawn) {
		// TODO: Tu sais ce que t'as à faire.
		return false;
	}
}
