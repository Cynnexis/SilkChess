package fr.polytech.projet.silkchess.game.pieces;

import com.sun.istack.internal.NotNull;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public abstract class Piece implements Serializable, Movable, Transferable {

	public static final DataFlavor PIECE_DATA_FLAVOR = new DataFlavor(Piece.class, "java/Piece");
	
	@NotNull
	private Color color = Color.BLACK;
	@NotNull
	private CPoint position = new CPoint();
	
	public Piece(Color color, CPoint position) {
		setColor(color);
		setPosition(position);
	}
	public Piece(Color color) {
		setColor(color);
		setPosition(new CPoint());
	}
	public Piece(CPoint position) {
		setColor(Color.BLACK);
		setPosition(position);
	}
	public Piece() {
		setColor(Color.BLACK);
		setPosition(new CPoint());
	}
	
	/* GETTERS & SETTERS */
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public CPoint getPosition() {
		return position;
	}
	
	public void setPosition(CPoint position) {
		this.position = position;
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
