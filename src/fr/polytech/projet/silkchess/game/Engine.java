package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.exceptions.PieceCannotMove;
import fr.polytech.projet.silkchess.game.exceptions.PieceDoesNotBelongToPlayerException;
import fr.polytech.projet.silkchess.game.exceptions.TileFullException;
import fr.polytech.projet.silkchess.game.pieces.*;
import fr.polytech.projet.silkchess.ui.window.components.Tile;
import org.omg.PortableServer.THREAD_POLICY_ID;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.ArrayList;

public class Engine implements Serializable {
	
	private Chessboard board = new Chessboard();
	private GameState state = GameState.INITIALIZING;
	private CheckState check = CheckState.NO_CHECKSTATE;
	private Color token = Color.WHITE;
	
	public Engine() {
	
	}
	
	public void newGame() {
		board.reset();
		
		// Black
		board.set('A', 8, new Rook(Color.BLACK, new CPoint('A', 8)));
		board.set('B', 8, new Knight(Color.BLACK, new CPoint('B', 8)));
		board.set('C', 8, new Bishop(Color.BLACK, new CPoint('C', 8)));
		board.set('D', 8, new Queen(Color.BLACK, new CPoint('D', 8)));
		board.set('E', 8, new King(Color.BLACK, new CPoint('E', 8)));
		board.set('F', 8, new Bishop(Color.BLACK, new CPoint('F', 8)));
		board.set('G', 8, new Knight(Color.BLACK, new CPoint('G', 8)));
		board.set('H', 8, new Rook(Color.BLACK, new CPoint('H', 8)));
		
		for (char x = 'A'; x <= 'H'; x++)
			board.set(x, 7, new Pawn(Color.BLACK, new CPoint(x, 7)));
		
		// White
		for (char x = 'A'; x <= 'H'; x++)
			board.set(x, 2, new Pawn(Color.WHITE, new CPoint(x, 2)));
		
		board.set('A', 1, new Rook(Color.WHITE, new CPoint('A', 1)));
		board.set('B', 1, new Knight(Color.WHITE, new CPoint('B', 1)));
		board.set('C', 1, new Bishop(Color.WHITE, new CPoint('C', 1)));
		board.set('D', 1, new Queen(Color.WHITE, new CPoint('D', 1)));
		board.set('E', 1, new King(Color.WHITE, new CPoint('E', 1)));
		board.set('F', 1, new Bishop(Color.WHITE, new CPoint('F', 1)));
		board.set('G', 1, new Knight(Color.WHITE, new CPoint('G', 1)));
		board.set('H', 1, new Rook(Color.WHITE, new CPoint('H', 1)));
	}
	
	public void play(int xsrc, int ysrc, int xdest, int ydest) throws NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
		Piece srcpiece = board.get(xsrc, ysrc);
		Piece destpiece = board.get(xdest, ydest);
		
		if (srcpiece == null || srcpiece.getPosition() == null || srcpiece instanceof NoPiece)
			throw new NoPieceException(srcpiece.getPosition());
		
		if (srcpiece.getColor() != token)
			throw new PieceDoesNotBelongToPlayerException(srcpiece, token);
		
		if (destpiece == null || destpiece.getPosition() == null)
			throw new NoPieceException(destpiece.getPosition());
		
		if (!(destpiece instanceof NoPiece))
			throw new TileFullException(destpiece.getPosition());
		
		if (!canMove(CPoint.fromPoint(xsrc, ysrc), CPoint.fromPoint(xdest, ydest)))
			throw new PieceCannotMove(srcpiece, new Point(xdest, ydest));
		
		move(srcpiece, destpiece.getPosition());
		
		invertToken();
	}
	public void play(@NotNull Point src, @NotNull Point dest) throws NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
		play(src.getX(), src.getY(), dest.getX(), dest.getY());
	}
	public void play(char xsrc, int ysrc, char xdest, int ydest) throws NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
		play(new CPoint(xsrc, ysrc), new CPoint(xdest, ydest));
	}
	public void play(@NotNull CPoint src, @NotNull CPoint dest) throws NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
		play(CPoint.toPoint(src), CPoint.toPoint(dest));
	}
	
	public boolean canMove(@NotNull CPoint point, @NotNull CPoint dest) throws NotImplementedException {
		try {
			return computeAllPossibleMove(point).contains(dest);
		} catch (NoPieceException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void move(@NotNull Piece src, @NotNull CPoint dest) {
		board.set(dest, src);
		board.set(src.getPosition(), new NoPiece(src.getPosition()));
		board.get(dest).setPosition(dest);
	}
	private void move(@NotNull Piece piece, @NotNull Point dest) {
		move(piece, CPoint.fromPoint(dest));
	}
	
	private void invertToken() {
		token = token == Color.BLACK ? Color.WHITE : Color.BLACK;
	}
	
	/**
	 * Check the chessboard to see if someone won
	 * @return Return true if one of the two player won
	 */
	public boolean checkWin() {
		boolean result = checkWin(Color.BLACK);
		
		if (!result)
			result = checkWin(Color.WHITE);
		
		return result;
	}
	public boolean checkWin(@NotNull Color color) {
		CPoint foeKing = board.search(new King(), Color.invert(color)).get(0);
		
		ArrayList<CPoint> list = new ArrayList<>();
		
		Piece[] allPieces = new Piece[] {new King(), new Queen(), new Rook(), new Bishop(), new Knight(), new Pawn()};
		
		for (Piece checkPiece : allPieces) {
			list = board.search(checkPiece, color);
			for (CPoint c : list) {
				if (canMove(c, foeKing))
				{
					// TODO: Finish this method
					
					return true;
				}
			}
		}
		
		return false;
	}
	
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
	public ArrayList<CPoint> computeAllPossibleMove(@NotNull CPoint position, @NotNull SpecialMove sm) throws NoPieceException {
		if (position == null)
			return null;
		
		if (sm == null)
			sm = SpecialMove.NOTHING;
		
		Piece piece = getBoard().get(CPoint.toPoint(position));
		
		if (piece instanceof NoPiece)
			throw new NoPieceException(position);
		
		ArrayList<CPoint> list = piece.possibleMoves();
		
		if (piece instanceof King)
		{
			deleteAlliesPositions(piece, list);
			
			// TODO: The king cannot go to a place where it can place its team in a CHECK state
			// TODO: if `sm == SpecialMove.CASTLING`, add the castling tile
		}
		else if (piece instanceof Queen)
		{
			// SO COMPLICATED
		}
		else if (piece instanceof Rook)
		{
			// SO COMPLICATED
		}
		else if (piece instanceof Bishop)
		{
			// SO COMPLICATED
		}
		else if (piece instanceof Knight)
		{
			deleteAlliesPositions(piece, list);
		}
		else if (piece instanceof Pawn)
		{
			boolean canMoveForward = true;
			
			Point point = CPoint.toPoint(piece.getPosition());
			
			// Get the only available position:
			Point pos = CPoint.toPoint(list.get(0));
			
			// If this position is occupied by an ally, the pawn cannot go there
			if (getBoard().get(pos).getColor() == piece.getColor() && !(getBoard().get(pos) instanceof NoPiece)) {
				list.remove(0);
				canMoveForward = false;
			}
			
			// Special move: First move
			if (sm == SpecialMove.FIRST_MOVE && canMoveForward) {
				list.add(CPoint.fromPoint(point.getX(), point.getY() + (piece.getColor() == Color.BLACK ? +2 : -2)));
			}
			
			// Special move: Promotion
			// TODO: The special move Promotion must be moved somewhere else
			if ((piece.getColor() == Color.BLACK && point.getY() == getBoard().getNbRows()-1) ||
					(piece.getColor() == Color.WHITE && point.getY() == 0))
			{
				Queen queen = new Queen(piece.getColor(), piece.getPosition());
				getBoard().set(piece.getPosition(), queen);
			}
			
			// Special move: En passant
			Point check = new Point();
			switch (piece.getColor())
			{
				case BLACK:
					check = new Point(point.getX() - 1, point.getY() + 1);
					if (getBoard().get(check).getColor() == Color.WHITE && !(getBoard().get(pos) instanceof NoPiece)) {
						list.add(CPoint.fromPoint(check));
						break;
					}
					
					check = new Point(point.getX() + 1, point.getY() + 1);
					if (getBoard().get(check).getColor() == Color.WHITE && !(getBoard().get(pos) instanceof NoPiece)) {
						list.add(CPoint.fromPoint(check));
						break;
					}
					break;
				case WHITE:
					check = new Point(point.getX() - 1, point.getY() - 1);
					if (getBoard().get(check).getColor() == Color.BLACK && !(getBoard().get(pos) instanceof NoPiece)) {
						list.add(CPoint.fromPoint(check));
						break;
					}
					
					check = new Point(point.getX() + 1, point.getY() - 1);
					if (getBoard().get(check).getColor() == Color.BLACK && !(getBoard().get(pos) instanceof NoPiece)) {
						list.add(CPoint.fromPoint(check));
						break;
					}
					break;
			}
		}
		
		return list;
	}
	
	private void deleteAlliesPositions(Piece piece, ArrayList<CPoint> list) {
		for (int i = 0; i < list.size(); i++) {
			Point p = CPoint.toPoint(list.get(i));
			// If the possible tile where the knight can go is a piece of the same color of the knight, then it cannot go there
			if (getBoard().get(p).getColor() == piece.getColor() && !(getBoard().get(p) instanceof NoPiece)) {
				list.remove(i);
				i--;
			}
		}
	}
	
	public ArrayList<CPoint> computeAllPossibleMove(@NotNull CPoint position) throws NoPieceException {
		return computeAllPossibleMove(position, SpecialMove.NOTHING);
	}
	
	/* GETTERS & SETTERS */
	
	public Chessboard getBoard() {
		return board;
	}
	
	public void setBoard(Chessboard board) {
		this.board = board;
	}
	
	public GameState getState() {
		return state;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
	public CheckState getCheck() {
		return check;
	}
	
	public void setCheck(CheckState check) {
		this.check = check;
	}
}
