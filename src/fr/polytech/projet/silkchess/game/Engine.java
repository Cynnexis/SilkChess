package fr.polytech.projet.silkchess.game;

import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.pieces.*;

public class Engine {
	
	private Chessboard board = new Chessboard();
	
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
	
	/* GETTERS & SETTERS */
	
	public Chessboard getBoard() {
		return board;
	}
	
	public void setBoard(Chessboard board) {
		this.board = board;
	}
}
