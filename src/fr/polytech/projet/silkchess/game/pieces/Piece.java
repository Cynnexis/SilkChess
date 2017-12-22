package fr.polytech.projet.silkchess.game.pieces;

import com.sun.istack.internal.NotNull;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;

import java.io.Serializable;

public abstract class Piece implements Serializable, Movable {

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
}
