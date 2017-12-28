package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.exceptions.PieceCannotMove;
import fr.polytech.projet.silkchess.game.exceptions.PieceDoesNotBelongToPlayerException;
import fr.polytech.projet.silkchess.game.exceptions.TileFullException;
import fr.polytech.projet.silkchess.game.pieces.*;
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
		
		if (!srcpiece.canMove(CPoint.fromPoint(new Point(xdest, ydest))))
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
	
	public boolean canMove(Piece piece, CPoint dest) throws NotImplementedException {
		// TODO: Enhance this method, it does not check if a piece is in the way, or if a piece can kill another
		return piece.canMove(dest);
	}
	
	private void move(Piece src, CPoint dest) {
		board.set(dest, src);
		board.set(src.getPosition(), new NoPiece(src.getPosition()));
		board.get(dest).setPosition(dest);
	}
	private void move(Piece piece, Point dest) {
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
				if (canMove(board.get(c), foeKing))
				{
					// TODO: Finish this method
					
					return true;
				}
			}
		}
		
		return false;
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
