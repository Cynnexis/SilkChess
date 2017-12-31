package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.debug.Debug;
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
	
	@NotNull
	private Chessboard board = new Chessboard();
	@NotNull
	private GameState state = GameState.INITIALIZING;
	@NotNull
	private CheckState check = CheckState.NO_CHECKSTATE;
	@NotNull
	private Color token = Color.WHITE;
	@NotNull
	private Player pBlack = new Player(Color.BLACK);
	@NotNull
	private Player pWhite = new Player(Color.WHITE);
	
	@NotNull
	private EngineListener engineListener = new EngineListener() {
		@Override
		public void onPieceMoved(CPoint source, Piece piece) { }
		@Override
		public void onTokenChanged(Color token) { }
		@Override
		public void onGameStateChanged(GameState oldState, GameState newState) { }
		@Override
		public void onPieceKilled(Piece pieceKilled) { }
	};
	
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
		
		// Reset the token to fire onTokenChanged event
		setToken(getToken());
	}
	
	@SuppressWarnings("SpellCheckingInspection")
	public void play(int xsrc, int ysrc, int xdest, int ydest) throws NullPointerException, NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
		Piece srcpiece = board.get(xsrc, ysrc);
		Piece destpiece = board.get(xdest, ydest);
		
		if (srcpiece == null || srcpiece.getPosition() == null || destpiece == null || destpiece.getPosition() == null)
			throw new NullPointerException();
		
		if (srcpiece instanceof NoPiece)
			throw new NoPieceException(srcpiece.getPosition());
		
		if (srcpiece.getColor() != token)
			throw new PieceDoesNotBelongToPlayerException(srcpiece, token);
		
		/*if (!(destpiece instanceof NoPiece))
			throw new TileFullException(destpiece.getPosition());*/
		
		if (!canMove(CPoint.fromPoint(xsrc, ysrc), CPoint.fromPoint(xdest, ydest)))
			throw new PieceCannotMove(srcpiece, new Point(xdest, ydest));
		
		move(srcpiece, destpiece.getPosition());
		
		getCurrentPlayer().setNbRound(getCurrentPlayer().getNbRound() + 1);
		
		invertToken();
	}
	public void play(@NotNull Point src, @NotNull Point dest) throws NullPointerException, NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
		play(src.getX(), src.getY(), dest.getX(), dest.getY());
	}
	@SuppressWarnings("SpellCheckingInspection")
	public void play(char xsrc, int ysrc, char xdest, int ydest) throws NullPointerException, NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
		play(new CPoint(xsrc, ysrc), new CPoint(xdest, ydest));
	}
	public void play(@NotNull CPoint src, @NotNull CPoint dest) throws NullPointerException, NoPieceException, PieceDoesNotBelongToPlayerException, TileFullException, PieceCannotMove {
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
		if (!(board.get(dest) instanceof NoPiece))
		{
			getCurrentPlayer().kill(board.get(dest));
			Debug.println(board.get(dest).toString() + " added to graveyard");
		}
		
		board.set(dest, src);
		board.set(src.getPosition(), new NoPiece(src.getPosition()));
		board.get(dest).setPosition(dest);
	}
	private void move(@NotNull Piece piece, @NotNull Point dest) {
		move(piece, CPoint.fromPoint(dest));
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
	public ArrayList<CPoint> computeAllPossibleMove(@NotNull CPoint position) throws NoPieceException {
		if (position == null)
			return null;
		
		Piece piece = getBoard().get(CPoint.toPoint(position));
		
		if (piece instanceof NoPiece)
			throw new NoPieceException(position);
		
		ArrayList<CPoint> list = piece.possibleMoves();
		
		// TODO: Delete the available tiles behind obstacles
		
		if (piece instanceof King)
		{
			deleteAlliesPositions(piece, list);
			
			// TODO: The king cannot go to a place where it can place its team in a CHECK state
			// TODO: if `sm == SpecialMove.CASTLING`, add the castling tile
		}
		else if (piece instanceof Queen || piece instanceof Rook || piece instanceof Bishop)
		{
			// Get all the enemies on the board
			ArrayList<Piece> foes = getBoard().getAll();
			
			// For each piece 'p'...
			for (Piece p : foes) {
				// ... Get all tiles behind the piece 'p', and remove them from the list
				ArrayList<CPoint> notAvailable = computePlacesBehindFoe(piece, p);
				list.removeAll(notAvailable);
			}
			
			deleteAlliesPositions(piece, list);
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
			
			// If this position is occupied by a piece, the pawn cannot go there
			if (!(getBoard().get(pos) instanceof NoPiece)) {
				list.remove(0);
				canMoveForward = false;
			}
			
			// Special move: First move
			if (canMoveForward && ((piece.getPosition().getY() == 7 && getToken() == Color.BLACK) || (piece.getPosition().getY() == 2 && getToken() == Color.WHITE))) {
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
			checkEnPassant(list, piece, point.getX() - 1, point.getY() + (piece.getColor() == Color.BLACK ? +1 : -1));
			checkEnPassant(list, piece, point.getX() + 1, point.getY() + (piece.getColor() == Color.BLACK ? +1 : -1));
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
	
	@SuppressWarnings("SpellCheckingInspection")
	private boolean checkEnPassant(ArrayList<CPoint> list, Piece piece, Point check) {
		if (((check.getX() < 0 || check.getX() >= getBoard().getNbColumns()) || (check.getY() < 0 || check.getY() >= getBoard().getNbRows()) || !(piece instanceof Pawn)))
			return false;
		
		if (getBoard().get(check).getColor() == Color.invert(piece.getColor()) && !(getBoard().get(check) instanceof NoPiece)) {
			list.add(CPoint.fromPoint(check));
			return true;
		}
		
		return false;
	}
	@SuppressWarnings("SpellCheckingInspection")
	private boolean checkEnPassant(ArrayList<CPoint> list, Piece piece, int x, int y) {
		return checkEnPassant(list, piece, new Point(x, y));
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
	public ArrayList<CPoint> computePlacesBehindFoe(@NotNull Piece current, @NotNull Piece foe) throws NullPointerException, NoPieceException {
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
			for (int i = xf + 1, j = yf + 1; i <= getBoard().getNbColumns() && j <= getBoard().getNbRows(); i++, j++)
				list.add(CPoint.fromPoint(i, j));
		// Top
		else if (x == xf && y < yf)
			for (int j = yf + 1; j < getBoard().getNbRows(); j++)
				list.add(CPoint.fromPoint(xf, j));
		// Top-right
		else if (x > xf && y < yf)
			for (int i = xf - 1, j = yf + 1; i >= 0 && j <= getBoard().getNbRows(); i--, j++)
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
			for (int i = xf + 1, j = yf - 1; i < getBoard().getNbColumns() && j >= 0; i++, j--)
				list.add(CPoint.fromPoint(i, j));
		// Left
		else if (x < xf && y == yf)
			for (int i = xf + 1; i < getBoard().getNbColumns(); i++)
				list.add(CPoint.fromPoint(i, yf));
		
		// Remove the position of the foe
		list.remove(foe.getPosition());
		
		return list;
	}
	
	/* GETTERS & SETTERS */
	
	public Chessboard getBoard() {
		return board;
	}
	
	public void setBoard(@NotNull Chessboard board) {
		if (board != null)
			this.board = board;
	}
	
	public GameState getState() {
		return state;
	}
	
	public void setState(@NotNull GameState state) {
		if (state != null) {
			GameState oldState = this.state;
			this.state = state;
			engineListener.onGameStateChanged(oldState, this.state);
		}
	}
	
	public CheckState getCheck() {
		return check;
	}
	
	public void setCheck(@NotNull CheckState check) {
		if (check != null)
			this.check = check;
	}
	
	public Color getToken() {
		return token;
	}
	
	public void setToken(@NotNull Color token) {
		if (token != null) {
			this.token = token;
			engineListener.onTokenChanged(this.token);
		}
	}
	
	private void invertToken() {
		setToken(getToken() == Color.BLACK ? Color.WHITE : Color.BLACK);
	}
	
	public Player getpBlack() {
		return pBlack;
	}
	
	public void setpBlack(Player pBlack) {
		this.pBlack = pBlack;
	}
	
	public Player getpWhite() {
		return pWhite;
	}
	
	public void setpWhite(Player pWhite) {
		this.pWhite = pWhite;
	}
	
	public @Nullable Player getCurrentPlayer() {
		switch (getToken())
		{
			case BLACK:
				return this.pBlack;
			case WHITE:
				return this.pWhite;
			default:
				return null;
		}
	}
	
	public EngineListener getEngineListener() {
		return engineListener;
	}
	
	public void setEngineListener(@NotNull EngineListener engineListener) {
		this.engineListener = engineListener != null ? engineListener : new EngineListener() {
			@Override
			public void onPieceMoved(CPoint source, Piece piece) { }
			@Override
			public void onTokenChanged(Color token) { }
			@Override
			public void onGameStateChanged(GameState oldState, GameState newState) { }
			@Override
			public void onPieceKilled(Piece pieceKilled) { }
		};
	}
}
