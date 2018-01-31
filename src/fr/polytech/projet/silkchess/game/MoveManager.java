package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.pieces.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class MoveManager {
	
	public static Couple<Piece, CPoint> PREVIOUS_ACTION = null;
	
	/**
	 * Compute all possible move of the piece located at {@code position}, regarding the board and all special rules,
	 * unlike the method {@code Piece.possibleMoves()}.
	 * @param position The position of the piece to compute
	 * @return Return the list of possible points
	 * @throws NoPieceException Throw this exception if the piece at the location {@code position} is an instance of
	 * {@code NoPiece}
	 * @see Piece
	 * @see fr.polytech.projet.silkchess.game.pieces.Movable
	 * @see NoPiece
	 * @see CPoint
	 */
	public static ArrayList<CPoint> computeAllPossibleMoveWithoutCheck(@NotNull Chessboard board, @NotNull Color token, @NotNull CPoint position) throws NoPieceException {
		if (board == null || token == null || position == null)
			return null;
		
		Piece piece = board.get(position);
		
		if (piece == null)
			throw new NullPointerException();
		
		if (piece instanceof NoPiece)
			throw new NoPieceException(position);
		
		ArrayList<CPoint> list = piece.possibleMoves();
		
		if (piece instanceof King)
		{
			deleteAlliesPositions(board, piece, list);
			
			// Search all the friendly rooks
			ArrayList<Piece> pieces = board.getAll(piece.getColor());
			
			for (Piece p : pieces) {
				if (p instanceof Rook) {
					if (SpecialMove.checkIfCastlingIsPossible(board, (King) piece, (Rook) p, false)) {
						if (p.getColor() == Color.BLACK && p.getPosition().equals(new CPoint('A', 8)))
							list.add(new CPoint('C', 8));
						else if (p.getColor() == Color.BLACK && p.getPosition().equals(new CPoint('H', 8)))
							list.add(new CPoint('G', 8));
						else if (p.getColor() == Color.WHITE && p.getPosition().equals(new CPoint('A', 1)))
							list.add(new CPoint('C', 1));
						else if (p.getColor() == Color.WHITE && p.getPosition().equals(new CPoint('H', 1)))
							list.add(new CPoint('G', 1));
						break;
					}
				}
			}
			
		}
		else if (piece instanceof Queen || piece instanceof Rook || piece instanceof Bishop)
		{
			// Get all the enemies on the board
			ArrayList<Piece> foes = board.getAll();
			
			// For each piece 'p'...
			for (Piece p : foes) {
				// ... Get all tiles behind the piece 'p', and remove them from the list
				ArrayList<CPoint> notAvailable = computePlacesBehindFoe(board, piece, p);
				list.removeAll(notAvailable);
			}
			
			deleteAlliesPositions(board, piece, list);
		}
		else if (piece instanceof Knight)
		{
			deleteAlliesPositions(board, piece, list);
		}
		else if (piece instanceof Pawn) {
			boolean canMoveForward = true;
			
			Point point = CPoint.toPoint(piece.getPosition());
			
			// Get the only available position:
			Point pos = CPoint.toPoint(list.get(0));
			
			// If this position is occupied by a piece, the pawn cannot go there
			if (!(board.get(pos) instanceof NoPiece)) {
				list.remove(0);
				canMoveForward = false;
			}
			
			if (canMoveForward && SpecialMove.checkIfFirstMoveIsPossible((Pawn) piece))
				list.add(CPoint.fromPoint(point.getX(), point.getY() + (piece.getColor() == Color.BLACK ? +2 : -2)));
			
			// Special move: Eat in diagonal
			checkIfPawnCanEat(board, list, piece, point.getX() - 1, point.getY() + (piece.getColor() == Color.BLACK ? +1 : -1));
			checkIfPawnCanEat(board, list, piece, point.getX() + 1, point.getY() + (piece.getColor() == Color.BLACK ? +1 : -1));
			
			// Special move: En passant
			if (PREVIOUS_ACTION != null) {
				if (SpecialMove.checkIfEnPassantIsPossible(board, (Pawn) piece, PREVIOUS_ACTION)) {
					int x = CPoint.toPoint(SpecialMove.IS_MOVING_EN_PASSANT.getX().getPosition()).getX(), y = CPoint.toPoint(SpecialMove.IS_MOVING_EN_PASSANT.getX().getPosition()).getY();
					if (piece.getColor() == Color.BLACK)
						y++;
					else
						y--;
					
					if (CPoint.toPoint(SpecialMove.IS_MOVING_EN_PASSANT.getY().getPosition()).getX() < x)
						x--;
					else
						x++;
					
					if (0 <= x && x < board.getNbColumns() && 0 <= y && y <= board.getNbRows())
						list.add(CPoint.fromPoint(x, y));
				}
			}
		}
		
		return list;
	}
	public static ArrayList<CPoint> computeAllPossibleMoveWithoutCheck(@NotNull Chessboard board, @NotNull Piece piece) throws NoPieceException {
		if (piece instanceof NoPiece)
			throw new NoPieceException(piece.getPosition());
		
		return computeAllPossibleMoveWithoutCheck(board, piece.getColor(), piece.getPosition());
	}
	
	public static ArrayList<CPoint> computeAllPossibleMoveWithCheck(@NotNull Chessboard board, @NotNull Piece piece) throws NoPieceException {
		if (piece == null)
			return null;
		
		if (piece instanceof NoPiece)
			throw new NoPieceException(piece.getPosition());
		
		ArrayList<CPoint> list = computeAllPossibleMoveWithoutCheck(board, piece);
		
		// If the piece is a king, ...
		if (piece instanceof King) {
			// ...delete all tiles which can put the king under check for the next round
			for (int i = 0; i < list.size(); i++) {
				// The program creates a NEW piece and chessboard to "simulate" the move.
				// The keyword "new" is very important, because in java "a = b" means that if 'a' is changed, so is 'b'.
				// Java passes the pointer reference through the equality operation. The "new" keyword prevent this
				// effect by creating a new instance (a new memory area) of the class.
				King copyKing = new King(piece.getColor(), piece.getPosition());
				Chessboard c = new Chessboard(board);
				
				c.move(copyKing, list.get(i));
				if (Check.checkIfPieceIsChecked(c, copyKing)) {
					list.remove(i);
					i--;
				}
			}
		}
		// If it is a normal piece, check that the move of this piece won't put its king under check
		else
		{
			try {
				// Search the ally king :
				King king = null;
				for (int i = 0, maxi = board.getNbColumns(); i < maxi && king == null; i++) {
					for (int j = 0, maxj = board.getNbRows(); j < maxj && king == null; j++) {
						if (board.get(i, j) != null && board.get(i, j) instanceof King && board.get(i, j).getColor() == piece.getColor())
							king = (King) board.get(i, j);
					}
				}
				
				if (king == null)
					throw new NullPointerException();
				
				for (int i = 0; i < list.size(); i++) {
					// To create a new instance of the piece (like the method did for the king case), the program must
					// use Java Reflection and create a new instance through `Class<?>.newInstance()` method
					Piece newInstance = piece.getClass().newInstance();
					newInstance.setColor(piece.getColor());
					newInstance.setPosition(piece.getPosition());
					// Create a copy of the chessboard
					Chessboard c = new Chessboard(board);
					
					c.move(newInstance, list.get(i));
					if (Check.checkIfPieceIsChecked(c, king)) {
						list.remove(i);
						i--;
					}
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	public static ArrayList<CPoint> computeAllPossibleMoveWithCheck(@NotNull Chessboard board, @NotNull Color token, @NotNull CPoint position) throws NoPieceException {
		Piece piece = board.get(position);
		return computeAllPossibleMoveWithCheck(board, piece);
	}
	
	private static void deleteAlliesPositions(Chessboard board, Piece piece, ArrayList<CPoint> list) {
		for (int i = 0; i < list.size(); i++) {
			Point p = CPoint.toPoint(list.get(i));
			// If the possible tile where the knight can go is a piece of the same color of the knight, then it cannot go there
			if (board.get(p).getColor() == piece.getColor() && !(board.get(p) instanceof NoPiece)) {
				list.remove(i);
				i--;
			}
		}
	}
	
	@SuppressWarnings("SpellCheckingInspection")
	private static boolean checkIfPawnCanEat(Chessboard board, ArrayList<CPoint> list, Piece piece, Point check) {
		if (((check.getX() < 0 || check.getX() >= board.getNbColumns()) || (check.getY() < 0 || check.getY() >= board.getNbRows()) || !(piece instanceof Pawn)))
			return false;
		
		if (board.get(check).getColor() == Color.invert(piece.getColor()) && !(board.get(check) instanceof NoPiece)) {
			list.add(CPoint.fromPoint(check));
			return true;
		}
		
		return false;
	}
	@SuppressWarnings("SpellCheckingInspection")
	private static boolean checkIfPawnCanEat(Chessboard board, ArrayList<CPoint> list, Piece piece, int x, int y) {
		return checkIfPawnCanEat(board, list, piece, new Point(x, y));
	}
	
	/**
	 * <p>
	 *  Compute all tiles behind the piece {@code foe} in the view point of the piece {@code current}.
	 * </p>
	 * <p>
	 *  Example:
	 * </p>
	 * <p>...........</p>
	 * <p>...o.......</p>
	 * <p>....x......</p>
	 * <p>.....*.....</p>
	 * <p>......*....</p>
	 * <p></p>
	 * <p> o : {@code current}</p>
	 * <p> x : {@code foe}</p>
	 * <p> * : places return by this method</p>
	 * @param current The current view point
	 * @param foe The foe piece
	 * @return Return the list of all tiles behind {@code foe}
	 * @throws NullPointerException Throw if {@code current} or {@code foe} is null
	 * @throws NoPieceException Throw if {@code current} or {@code foe} is an instance of {@code NoPiece}
	 * @see NoPiece
	 */
	public static ArrayList<CPoint> computePlacesBehindFoe(Chessboard board, @NotNull Piece current, @NotNull Piece foe) throws NullPointerException, NoPieceException {
		if (current == null || foe == null)
			throw new NullPointerException();
		
		if (current instanceof NoPiece)
			throw new NoPieceException(current.getPosition());
		
		if (foe instanceof NoPiece)
			throw new NoPieceException(foe.getPosition());
		
		ArrayList<CPoint> list = new ArrayList<>();
		
		if (current.getPosition().equals(foe.getPosition()))
			return list;
		
		Point pcurrent = CPoint.toPoint(current.getPosition());
		Point pfoe = CPoint.toPoint(foe.getPosition());
		int x = pcurrent.getX(), y = pcurrent.getY();
		int xf = pfoe.getX(), yf = pfoe.getY();
		
		// Top-left
		if (x < xf && y < yf)
			for (int i = xf + 1, j = yf + 1; i <= board.getNbColumns() && j <= board.getNbRows(); i++, j++)
				list.add(CPoint.fromPoint(i, j));
			// Top
		else if (x == xf && y < yf)
			for (int j = yf + 1; j < board.getNbRows(); j++)
				list.add(CPoint.fromPoint(xf, j));
			// Top-right
		else if (x > xf && y < yf)
			for (int i = xf - 1, j = yf + 1; i >= 0 && j <= board.getNbRows(); i--, j++)
				list.add(CPoint.fromPoint(i, j));
			// Right
		else if (x > xf && y == yf)
			for (int i = xf - 1; i >= 0; i--)
				list.add(CPoint.fromPoint(i, yf));
			// Bottom-right
		else if (x > xf && y > yf)
			for (int i = xf - 1, j = yf - 1; i >= 0 && j >= 0; i--, j--)
				list.add(CPoint.fromPoint(i, j));
			// Bottom
		else if (x == xf && y > yf)
			for (int j = yf - 1; j >= 0; j--)
				list.add(CPoint.fromPoint(xf, j));
			// Bottom-left
		else if (x < xf && y > yf)
			for (int i = xf + 1, j = yf - 1; i < board.getNbColumns() && j >= 0; i++, j--)
				list.add(CPoint.fromPoint(i, j));
			// Left
		else if (x < xf && y == yf)
			for (int i = xf + 1; i < board.getNbColumns(); i++)
				list.add(CPoint.fromPoint(i, yf));
		
		// Remove the position of the foe
		list.remove(foe.getPosition());
		
		return list;
	}
}
