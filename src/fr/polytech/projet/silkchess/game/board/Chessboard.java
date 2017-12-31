package fr.polytech.projet.silkchess.game.board;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Matrix;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.pieces.*;

import java.util.ArrayList;

/**
 * Chessboard Class.
 * In the basis (0 ; 0) -> (7, 7), the grid is:
 * (0 ; 0) (1 ; 0) (2 ; 0) (3 ; 0) (4 ; 0) (5 ; 0) (6 ; 0) (7 ; 0)
 * (0 ; 1) (1 ; 1) (2 ; 1) (3 ; 1) (4 ; 1) (5 ; 1) (6 ; 1) (7 ; 1)
 * (0 ; 2) (1 ; 2) (2 ; 2) (3 ; 2) (4 ; 2) (5 ; 2) (6 ; 0) (7 ; 2)
 * (0 ; 3) (1 ; 3) (2 ; 3) (3 ; 3) (4 ; 3) (5 ; 3) (6 ; 0) (7 ; 3)
 * (0 ; 4) (1 ; 4) (2 ; 4) (3 ; 4) (4 ; 4) (5 ; 4) (6 ; 0) (7 ; 4)
 * (0 ; 5) (1 ; 5) (2 ; 5) (3 ; 5) (4 ; 5) (5 ; 5) (6 ; 0) (7 ; 5)
 * (0 ; 6) (1 ; 6) (2 ; 6) (3 ; 6) (4 ; 6) (5 ; 6) (6 ; 0) (7 ; 6)
 * (0 ; 7) (1 ; 7) (2 ; 7) (3 ; 7) (4 ; 7) (5 ; 7) (6 ; 0) (7 ; 7)
 * Whereas in the basis ('A' ; 1) -> ('H' ; 8):
 * ('A' ; 8) ('B' ; 8) ('C' ; 8) ('D' ; 8) ('E' ; 8) ('F' ; 8) ('G' ; 8) ('H' ; 8)
 * ('A' ; 7) ('B' ; 7) ('C' ; 7) ('D' ; 7) ('E' ; 7) ('F' ; 7) ('G' ; 7) ('H' ; 7)
 * ('A' ; 6) ('B' ; 6) ('C' ; 6) ('D' ; 6) ('E' ; 6) ('F' ; 6) ('G' ; 6) ('H' ; 6)
 * ('A' ; 5) ('B' ; 5) ('C' ; 5) ('D' ; 5) ('E' ; 5) ('F' ; 5) ('G' ; 5) ('H' ; 5)
 * ('A' ; 4) ('B' ; 4) ('C' ; 4) ('D' ; 4) ('E' ; 4) ('F' ; 4) ('G' ; 4) ('H' ; 4)
 * ('A' ; 3) ('B' ; 3) ('C' ; 3) ('D' ; 3) ('E' ; 3) ('F' ; 3) ('G' ; 3) ('H' ; 3)
 * ('A' ; 2) ('B' ; 2) ('C' ; 2) ('D' ; 2) ('E' ; 2) ('F' ; 2) ('G' ; 2) ('H' ; 2)
 * ('A' ; 1) ('B' ; 1) ('C' ; 1) ('D' ; 1) ('E' ; 1) ('F' ; 1) ('G' ; 1) ('H' ; 1)
 *
 */
public class Chessboard extends Matrix<Piece> {
	
	public Chessboard() {
		super(8, 8, new NoPiece());
		
		reset();
	}
	
	public void reset() {
		this.clear(new NoPiece());
		
		for (int i = 0; i < getNbColumns(); i++)
			for (int j = 0; j < getNbRows(); j++)
				this.set(i, j, new NoPiece(CPoint.fromPoint(i, j)));
		
		this.set('A', 8, new Rook());
	}
	
	public boolean move(CPoint source, CPoint dest) {
		Piece piece = this.get(source);
		// Check if move is possible
		if (piece.canMove(dest))
		{
			boolean b1 = this.set(dest, piece);
			boolean b2 = this.set(source, new NoPiece(source));
			return b1 && b2;
		}
		else
			return false;
	}
	public boolean move(char sourceX, int sourceY, char destX, int destY) {
		return move(new CPoint(sourceX, sourceY), new CPoint(destX, destY));
	}
	public boolean move(Point source, Point dest) {
		return move(CPoint.fromPoint(source), CPoint.fromPoint(dest));
	}
	public boolean move(int sourceX, int sourceY, int destX, int destY) {
		return move(new Point(sourceX, sourceY), new Point(destX, destY));
	}
	
	public Piece get(CPoint cpoint) {
		return this.get(cpoint.getX() - 'A', getNbRows() - cpoint.getY());
	}
	public Piece get(char x, int y) {
		return this.get(new CPoint(x, y));
	}
	
	public boolean set(CPoint cpoint, Piece value) {
		return this.set(cpoint.getX() - 'A', getNbRows() - cpoint.getY() , value);
	}
	public boolean set(char x, int y, Piece value) {
		return this.set(new CPoint(x, y), value);
	}
	
	public <T extends Piece> ArrayList<CPoint> search(T piece, Color color) {
		ArrayList<CPoint> list = new ArrayList<>();
		
		for (int i = 0; i < getNbColumns(); i++)
		{
			for (int j = 0; j < getNbRows(); j++)
			{
				Piece currentP = this.get(i, j);
				if ((currentP instanceof King && piece instanceof King) ||
						(currentP instanceof Queen && piece instanceof Queen) ||
						(currentP instanceof Rook && piece instanceof Rook) ||
						(currentP instanceof Bishop && piece instanceof Bishop) ||
						(currentP instanceof Knight && piece instanceof Knight) ||
						(currentP instanceof Pawn && piece instanceof Pawn)
						&& (currentP.getColor() == color))
					list.add(currentP.getPosition());
			}
		}
		
		return list;
	}
	
	public ArrayList<Piece> getAll() {
		ArrayList<Piece> pieces = new ArrayList<>();
		for (int i = 0; i < getNbColumns(); i++) {
			for (int j = 0; j < getNbRows(); j++) {
				Piece p = get(i, j);
				if (!(p instanceof NoPiece))
					pieces.add(p);
			}
		}
		return pieces;
	}
	public ArrayList<Piece> getAll(Color color) {
		ArrayList<Piece> pieces = getAll();
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).getColor() != color) {
				pieces.remove(i);
				i--;
			}
		}
		return pieces;
	}
}
