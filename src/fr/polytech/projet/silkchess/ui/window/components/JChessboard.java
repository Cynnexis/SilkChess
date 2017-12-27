package fr.polytech.projet.silkchess.ui.window.components;

import fr.berger.enhancedlist.Matrix;
import fr.berger.enhancedlist.MatrixListener;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.pieces.NoPiece;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

public class JChessboard extends JPanel implements Serializable {
	
	private Matrix<Tile> board;
	
	private JPanel p_board = new JPanel();
	private JPanel p_xlabel = new JPanel();
	private JPanel p_ylabel = new JPanel();
	
	public JChessboard(int nbColumns, int nbRows) {
		p_board.setLayout(new GridLayout(nbColumns, nbRows));
		board = new Matrix<>(nbColumns, nbRows);
		
		board.setListener(new MatrixListener() {
			@Override
			public void OnCellChanged(int x, int y, Object value) {
				if (!(value instanceof Piece))
					return;
				
				try {
					Piece p = (Piece) value;
					set(x, y, p);
				} catch (ClassCastException ex) {
					ex.printStackTrace();
				}
			}
			
			@Override
			public void OnCellRead(int x, int y, Object value) {
				if (value instanceof Piece) {
					EventQueue.invokeLater(() -> {
						set(x, y, (Piece) value);
					});
				}
			}
		});
		
		for (int i = 0; i < nbColumns; i++)
		{
			for (int j = 0; j < nbRows; j++)
			{
				Tile t = new Tile(((i % 2 == 0 && j % 2 == 1) || ((i % 2 == 1 && j % 2 == 0)) ? Color.BLACK : Color.WHITE), new NoPiece(CPoint.fromPoint(j, i)));
				t.addActionListener((ActionListener & Serializable) (ActionEvent e) -> {
					try {
						Piece p = ((Tile) e.getSource()).getPiece();
						if (p != null && p.getPosition() != null) {
							Point pos = CPoint.toPoint(p.getPosition());
							System.out.println("Button (" + p.getPosition().getX() + " ; " + p.getPosition().getY() + ") -> (" + pos.getX() + " ; " + pos.getY() + ")");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});
				board.set(j, i, t);
				p_board.add(t);
			}
		}
		
		p_xlabel.setLayout(new GridLayout(1, nbColumns));
		for (int i = 0; i < nbColumns; i++)
			p_xlabel.add(new JLabel(Character.toString((char) ('A' + i))));
		
		p_ylabel.setLayout(new GridLayout(nbRows, 1));
		for (int j = 0; j < nbRows; j++)
			p_ylabel.add(new JLabel(Integer.toString(nbRows - j)));
		
		p_ylabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				int buttonWidth = board.get(0 , 0) != null ? board.get(0 , 0).getWidth() : 0;
				p_xlabel.setBorder(new EmptyBorder(5, p_ylabel.getWidth() + (buttonWidth/2), 5, 5));
			}
			
			@Override
			public void componentMoved(ComponentEvent e) { }
			@Override
			public void componentShown(ComponentEvent e) { }
			@Override
			public void componentHidden(ComponentEvent e) { }
		});
		
		this.setLayout(new BorderLayout());
		this.add(p_xlabel, BorderLayout.NORTH);
		this.add(p_board, BorderLayout.CENTER);
		this.add(p_ylabel, BorderLayout.WEST);
	}
	
	public void set(Chessboard chessboard) {
		for (int i = 0; i < chessboard.getNbColumns(); i++) {
			for (int j = 0; j < chessboard.getNbColumns(); j++) {
				set(i, j, chessboard.get(i, j));
			}
		}
	}
	
	public void set(int x, int y, Piece piece) {
		if (board != null && board.get(x, y) != null)
			board.get(x, y).setPiece(piece);
		
		if (p_board != null) {
			Tile t = ((Tile) p_board.getComponentAt(x, y));
			if (t != null)
				t.setPiece(piece);
		}
	}
	
	/* GETTERS & SETTERS */
	
	public Matrix<Tile> getBoard() {
		return board;
	}
	
	public void setBoard(Matrix<Tile> board) {
		this.board = board;
	}
}