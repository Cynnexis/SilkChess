package fr.polytech.projet.silkchess.ui.window.components;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.berger.enhancedlist.Matrix;
import fr.berger.enhancedlist.MatrixListener;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.SpecialMove;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.pieces.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

public class JChessboard extends JPanel implements Serializable {
	
	private Matrix<Tile> board;
	
	private JPanel p_board = new JPanel();
	private JPanel p_xlabel = new JPanel();
	private JPanel p_ylabel = new JPanel();
	
	@Nullable
	private Tile selectedTile = null;
	
	@NotNull
	private JChessboardListener jChessboardListener = new JChessboardListener() {
		@Override
		public void onSelectedTileChanged(Tile tile) { }
	};
	
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
				/*t.addActionListener((ActionListener & Serializable) (ActionEvent e) -> {
					try {
						Piece p = ((Tile) e.getSource()).getPiece();
						if (p != null && p.getPosition() != null) {
							Point pos = CPoint.toPoint(p.getPosition());
							System.out.println("Button (" + p.getPosition().getX() + " ; " + p.getPosition().getY() + ") -> (" + pos.getX() + " ; " + pos.getY() + ")");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});*/
				t.setTileListener(new TileListener() {
					@Override
					public void onColorChanged(Color color) { }
					
					@Override
					public void onPieceChanged(Piece piece) { }
					
					@Override
					public void onTileDropBegin(Tile startTile) {
						if (startTile != null) {
							if (startTile.equals(selectedTile))
								selectedTile = null;
							else
								selectedTile = startTile;
							
							jChessboardListener.onSelectedTileChanged(selectedTile);
						}
					}
					
					@Override
					public void onTileDropStop(Tile stopTile) {
						// TODO: Do something
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
		
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
	}
	
	public void set(@NotNull Chessboard chessboard) {
		if (chessboard != null) {
			for (int i = 0; i < chessboard.getNbColumns(); i++) {
				for (int j = 0; j < chessboard.getNbRows(); j++) {
					try {
						Piece piece = chessboard.get(i, j);
						set(i, j, piece != null ? piece : new NoPiece(CPoint.fromPoint(i, j)));
					} catch (IndexOutOfBoundsException ignored) { }
				}
			}
		}
	}
	
	public void set(int x, int y, @NotNull Piece piece) {
		if (piece != null) {
			if (board != null && board.get(x, y) != null)
				board.get(x, y).setPiece(piece);
			
			if (p_board != null) {
				try {
					Tile t = ((Tile) p_board.getComponent(x + y * getBoard().getNbColumns()));
					if (t != null)
						t.setPiece(piece);
				} catch (ClassCastException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public void hightlightTile(Point point) {
		getBoard().get(point).setBackground(java.awt.Color.BLUE);
	}
	
	public void resetHightlight() {
		for (int i = 0; i < getBoard().getNbColumns(); i++) {
			for (int j = 0; j < getBoard().getNbRows(); j++) {
				getBoard().get(i, j).resetColor();
			}
		}
	}
	
	/* GETTERS & SETTERS */
	
	public Matrix<Tile> getBoard() {
		return board;
	}
	
	public void setBoard(Matrix<Tile> board) {
		this.board = board;
	}
	
	public Tile getSelectedTile() {
		return selectedTile;
	}
	
	public void setSelectedTile(Tile selectedTile) {
		this.selectedTile = selectedTile;
	}
	
	public JChessboardListener getJChessboardListener() {
		return jChessboardListener;
	}
	
	public void setJChessboardListener(@NotNull JChessboardListener jChessboardListener) {
		this.jChessboardListener = jChessboardListener != null ? jChessboardListener : new JChessboardListener() {
			@Override
			public void onSelectedTileChanged(Tile tile) { }
		};
	}
}
