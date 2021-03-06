package fr.polytech.projet.silkchess.game.board;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Point;
import fr.berger.enhancedlist.matrix.Matrix;
import fr.polytech.projet.silkchess.debug.Debug;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.Player;
import fr.polytech.projet.silkchess.game.pieces.*;
import fr.polytech.projet.silkchess.ui.window.components.PieceRepresentation;

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
 * @author Valentin Berger
 */
public class Chessboard extends Matrix<Piece> {
	
	public Chessboard() {
		super(8, 8, new NoPiece());
		
		reset();
	}
	public Chessboard(Chessboard copy) {
		super(copy.getNbColumns(), copy.getNbRows(), new NoPiece());
		
		reset();
		
		for (int i = 0; i < getNbColumns(); i++) {
			for (int j = 0; j < getNbRows(); j++) {
				if (copy.get(i, j) == null)
					this.set(i, j, new NoPiece(CPoint.fromPoint(i, j)));
				else
					this.set(i, j, copy.get(i, j));
			}
		}
	}
	
	/**
	 * Reset the chessboard by by placing {@code NoPiece} instance for each tile.
	 * @see NoPiece
	 */
	public void reset() {
		this.clear(new NoPiece());
		
		for (int i = 0; i < getNbColumns(); i++)
			for (int j = 0; j < getNbRows(); j++)
				this.set(i, j, new NoPiece(CPoint.fromPoint(i, j)));
	}
	
	public @Nullable Piece get(CPoint cpoint) {
		return this.get(cpoint.getX() - 'A', getNbRows() - cpoint.getY());
	}
	public @Nullable Piece get(char x, int y) {
		return this.get(new CPoint(x, y));
	}
	
	public boolean set(CPoint cpoint, Piece value) {
		return this.set(cpoint.getX() - 'A', getNbRows() - cpoint.getY() , value);
	}
	public boolean set(char x, int y, Piece value) {
		return this.set(new CPoint(x, y), value);
	}
	
	public @Nullable Piece move(@NotNull Piece src, @NotNull CPoint dest) {
		Piece pieceKilled = null;
		if (!(get(dest) instanceof NoPiece))
			pieceKilled = get(dest);
		
		set(dest, src);
		set(src.getPosition(), new NoPiece(src.getPosition()));
		get(dest).setPosition(dest);
		return pieceKilled;
	}
	public @Nullable Piece move(@NotNull Piece piece, @NotNull Point dest) {
		return move(piece, CPoint.fromPoint(dest));
	}
	
	@Deprecated
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
	public ArrayList<CPoint> search(Class<? extends Piece> piece, Color color) {
		ArrayList<CPoint> list = new ArrayList<>();
		
		for (int i = 0; i < getNbColumns(); i++)
		{
			for (int j = 0; j < getNbRows(); j++)
			{
				Piece currentP = this.get(i, j);
				if (piece.isInstance(currentP))
					list.add(currentP.getPosition());
			}
		}
		
		return list;
	}
	
	public @NotNull ArrayList<Piece> getAll() {
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
	public @NotNull ArrayList<Piece> getAll(Color color) {
		ArrayList<Piece> pieces = getAll();
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).getColor() != color) {
				pieces.remove(i);
				i--;
			}
		}
		return pieces;
	}
	
	/* OVERRIDE */
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("\n");
		
		for (int i = 0; i < getNbColumns(); i++) {
			
			if (i == 0) {
				// Building horizontal line
				for (int j = 0; j < getNbRows() * 2 + 1; j++)
					build.append(j % 2 == 0 ? '+' : '-');
				build.append("\n");
			}
			
			// Building pieces
			for (int j = 0; j < getNbRows(); j++) {
				if (j == 0)
					build.append("|");
				build.append(PieceRepresentation.getRepresentation(get(j, i), get(j, i).getColor())).append("|");
			}
			
			build.append("\n");
			
			// Building horizontal line
			for (int j = 0; j < getNbRows() * 2 + 1; j++)
				build.append(j % 2 == 0 ? '+' : '-');
			build.append("\n");
		}
		
		return build.toString();
	}
}
