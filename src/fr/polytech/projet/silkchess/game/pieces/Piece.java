package fr.polytech.projet.silkchess.game.pieces;

import com.sun.istack.internal.NotNull;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Piece implements Serializable, Movable, Transferable {

	public static final DataFlavor PIECE_DATA_FLAVOR = new DataFlavor(Piece.class, "java/Piece");
	
	@NotNull
	private Color color = Color.BLACK;
	@NotNull
	private CPoint position = new CPoint();
	@NotNull
	private boolean hasMoved = false;
	
	public Piece(Color color, CPoint position) {
		setColor(color);
		setPosition(position);
		setHasMoved(false);
	}
	public Piece(Color color) {
		setColor(color);
		setPosition(new CPoint());
		setHasMoved(false);
	}
	public Piece(CPoint position) {
		setColor(Color.BLACK);
		setPosition(position);
		setHasMoved(false);
	}
	public Piece() {
		setColor(Color.BLACK);
		setPosition(CPoint.fromPoint(0, 0));
		setHasMoved(false);
	}
	
	public boolean canMove(CPoint destination) {
		ArrayList<CPoint> list = possibleMoves();
		return list.contains(destination);
	}
	
	/* GETTERS & SETTERS */
	
	public @NotNull Color getColor() {
		return color;
	}
	
	public void setColor(@NotNull Color color) {
		if (color != null)
			this.color = color;
	}
	
	public @NotNull CPoint getPosition() {
		return position;
	}
	
	public void setPosition(@NotNull CPoint position) {
		if (position == null || position.getX() == null || position.getY() == null)
			//throw new NullPointerException();
			System.err.println("Position cannot be null");
		
		CPoint oldPos = this.position;
		this.position = position;
		
		if (!oldPos.equals(position))
			setHasMoved(true);
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	private void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Piece)) return false;
		Piece piece = (Piece) o;
		return getColor() == piece.getColor() &&
				Objects.equals(getPosition(), piece.getPosition());
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(getColor(), getPosition());
	}
	
	@Override
	public String toString() {
		return "Piece{" +
				"color=" + (color != null ? color.toString() : "(null)") +
				", position=" + (position != null ? position.toString() : "(null)") +
				'}';
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { PIECE_DATA_FLAVOR };
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(PIECE_DATA_FLAVOR);
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return this;
	}
}
