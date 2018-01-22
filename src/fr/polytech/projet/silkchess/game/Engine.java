package fr.polytech.projet.silkchess.game;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Couple;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.debug.Debug;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.exceptions.PieceCannotMove;
import fr.polytech.projet.silkchess.game.exceptions.PieceDoesNotBelongToPlayerException;
import fr.polytech.projet.silkchess.game.exceptions.TileFullException;
import fr.polytech.projet.silkchess.game.pieces.*;
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
		public void onCheckStateChanged(CheckState state) { }
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
		
		if (!canMove(CPoint.fromPoint(xsrc, ysrc), CPoint.fromPoint(xdest, ydest)))
			throw new PieceCannotMove(srcpiece, new Point(xdest, ydest));
		
		move(srcpiece, destpiece.getPosition());
		
		getCurrentPlayer().setNbRound(getCurrentPlayer().getNbRound() + 1);
		
		setCheck(Check.checkCheck(getBoard()));
		
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
	
	public boolean canMove(@NotNull CPoint point, @NotNull CPoint dest) {
		try {
			return MoveManager.computeAllPossibleMoveWithCheck(getBoard(), getToken(), point).contains(dest);
		} catch (NoPieceException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void move(@NotNull Piece src, @NotNull CPoint dest) {
		if (src == null || dest == null)
			throw new NullPointerException();
		
		CPoint pieceSource = src.getPosition();
		
		Piece p = getBoard().move(src, dest);
		
		// Castling move
		if (SpecialMove.IS_CASTLING != null && SpecialMove.IS_CASTLING.getX() != null && SpecialMove.IS_CASTLING.getY() != null &&
				(src instanceof King) && src.getPosition().equals(SpecialMove.IS_CASTLING.getX().getPosition())) {
			Rook r = SpecialMove.IS_CASTLING.getY();
			SpecialMove.IS_CASTLING = null;
			if (r.getColor() == Color.BLACK && r.getPosition().equals(new CPoint('A', 8)))
				getBoard().move(r, new CPoint('D', 8));
			else if (r.getColor() == Color.BLACK && r.getPosition().equals(new CPoint('H', 8)))
				getBoard().move(r, new CPoint('F', 8));
			else if (r.getColor() == Color.WHITE && r.getPosition().equals(new CPoint('A', 1)))
				getBoard().move(r, new CPoint('D', 1));
			else if (r.getColor() == Color.WHITE && r.getPosition().equals(new CPoint('H', 1)))
				getBoard().move(r, new CPoint('F', 1));
		}
		
		// En passant move
		if (SpecialMove.IS_MOVING_EN_PASSANT != null && SpecialMove.IS_MOVING_EN_PASSANT.getX() != null && SpecialMove.IS_MOVING_EN_PASSANT.getY() != null &&
				(src instanceof Pawn)) {
			NoPiece noPiece = new NoPiece(SpecialMove.IS_MOVING_EN_PASSANT.getY().getColor(), SpecialMove.IS_MOVING_EN_PASSANT.getY().getPosition());
			getBoard().set(noPiece.getPosition(), noPiece);
			getCurrentPlayer().kill(SpecialMove.IS_MOVING_EN_PASSANT.getY());
			SpecialMove.IS_MOVING_EN_PASSANT = null;
		}
		
		if (p != null)
			getCurrentPlayer().kill(p);
		
		MoveManager.PREVIOUS_ACTION = new Couple<>(src, pieceSource);
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
		if (check != null) {
			CheckState oldState = this.check;
			this.check = check;
			
			if (oldState != this.check)
				engineListener.onCheckStateChanged(this.check);
		}
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
			public void onCheckStateChanged(CheckState state) { }
			@Override
			public void onPieceKilled(Piece pieceKilled) { }
		};
	}
}
