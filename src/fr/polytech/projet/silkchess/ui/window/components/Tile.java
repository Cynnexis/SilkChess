package fr.polytech.projet.silkchess.ui.window.components;

import com.sun.istack.internal.NotNull;
import fr.polytech.projet.silkchess.debug.Debug;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.pieces.*;
import fr.polytech.projet.silkchess.ui.preferences.EPref;
import fr.polytech.projet.silkchess.ui.preferences.Pref;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Tile extends JPanel implements MouseListener, Serializable, Transferable, DropTargetListener {
	
	public static final DataFlavor TILE_DATA_FLAVOR = new DataFlavor(Tile.class, "java/Tile");
	
	@NotNull
	private Color color = Color.BLACK;
	@NotNull
	private Piece piece = new NoPiece();
	private boolean isHighlight = false;
	@NotNull
	private ArrayList<ActionListener> actionListeners = new ArrayList<>();
	
	private JLabel l_piece = new JLabel();
	
	@NotNull
	private TileListener tileListener = new TileListener() {
		@Override
		public void onColorChanged(Color color) { }
		@Override
		public void onPieceChanged(Piece piece) { }
		@Override
		public void onTileDropBegin(Tile startTile) { }
		@Override
		public void onTileDropStop(Tile stopTile) { }
	};
	
	public Tile() {
		init(Color.BLACK, new NoPiece());
	}
	public Tile(@NotNull Color color) {
		init(color, new NoPiece());
	}
	public Tile(@NotNull Color color, @NotNull Piece piece) {
		init(color, piece);
	}
	
	private void init(@NotNull Color color, Piece piece) {
		setColor(color);
		setPiece(piece);
		setHighlight(false);
		this.addMouseListener(this);
		this.add(l_piece);
		
		this.setDropTarget(new DropTarget(this, this));
		this.setTransferHandler(new TileTransferHandler());
	}
	
	public void resetColor() {
		setColor(getColor());
	}
	
	/* JPanel Override */
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		switch (getColor())
		{
			case BLACK:
				this.setBackground(new java.awt.Color(214, 137, 75));
				break;
			case WHITE:
				this.setBackground(new java.awt.Color(255, 205, 158));
				break;
		}
		
		if (isHighlight()) {
			int minDimension = this.getWidth() < this.getHeight() ? this.getWidth() : this.getHeight();
			
			// Margin:
			minDimension = (int) ((float) minDimension - ((float) minDimension * .4f));
			
			g.setColor(new java.awt.Color(83, 164, 130, 128));
			g.fillOval((this.getWidth() - minDimension) / 2, (this.getHeight() - minDimension) / 2, minDimension, minDimension);
		}
	}
	
	/* MouseListener Events */
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!Pref.getBoolean(EPref.CHESSBOARD_CONTROL)) {
			
			if (e == null)
				e = new MouseEvent(this, MouseEvent.BUTTON1, 0, InputEvent.BUTTON1_DOWN_MASK, 0, 0, 1, false);
			
			if (e.getButton() == MouseEvent.BUTTON1) {
				tileListener.onTileDropBegin(this);
				
				if (actionListeners != null && actionListeners.size() > 0)
					for (ActionListener al : actionListeners)
						if (al != null)
							al.actionPerformed(new ActionEvent(e.getSource(), e.getID(), e.paramString()));
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		/*if (Pref.getBoolean(EPref.CHESSBOARD_CONTROL)) {
			tileListener.onTileDropBegin(this);
			
			JComponent comp = (JComponent) e.getSource();
			TileTransferHandler th = (TileTransferHandler) comp.getTransferHandler();
			
			Debug.println("Tile.mousePressed> comp: (Tile) (Piece) " + ((Tile) comp).getPiece().toString() + "\n\tth: " + th.toString() + "\n\te: " + e.toString());
			
			th.exportAsDrag(this, e, TransferHandler.MOVE);
		}*/
	}
	
	@Override
	public void mouseReleased(MouseEvent e) { }
	
	@Override
	public void mouseEntered(MouseEvent e) { }
	
	@Override
	public void mouseExited(MouseEvent e) { }
	
	/* GETTERS & SETTERS */
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(@NotNull Color color) {
		this.color = color;
		repaint();
		tileListener.onColorChanged(this.color);
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public void setPiece(@NotNull Piece piece) {
		this.piece = piece;
		refreshLabel();
		repaint();
		tileListener.onPieceChanged(this.piece);
	}
	
	public boolean isHighlight() {
		return isHighlight;
	}
	
	public void setHighlight(boolean highlight) {
		isHighlight = highlight;
		repaint();
	}
	
	public ArrayList<ActionListener> getActionListeners() {
		return actionListeners;
	}
	
	public void setActionListeners(@NotNull ArrayList<ActionListener> actionListeners) {
		this.actionListeners = actionListeners;
	}
	
	public void addActionListener(ActionListener action) {
		this.actionListeners.add(action);
	}
	
	public TileListener getTileListener() {
		return tileListener;
	}
	
	public void setTileListener(TileListener tileListener) {
		this.tileListener = tileListener != null ? tileListener : new TileListener() {
			@Override
			public void onColorChanged(Color color) { }
			@Override
			public void onPieceChanged(Piece piece) { }
			@Override
			public void onTileDropBegin(Tile startTile) { }
			@Override
			public void onTileDropStop(Tile stopTile) { }
		};
	}
	
	/* OTHERS */
	
	public void refreshLabel() {
		l_piece.setFont(new Font(l_piece.getFont().getName(), Font.BOLD, 30));
		
		/*if (getPiece() instanceof King)
			l_piece.setText(Character.toString((char) 9818));
		else if (getPiece() instanceof Queen)
			l_piece.setText(Character.toString((char) 9819));
		else if (getPiece() instanceof Rook)
			l_piece.setText(Character.toString((char) 9820));
		else if (getPiece() instanceof Bishop)
			l_piece.setText(Character.toString((char) 9821));
		else if (getPiece() instanceof Knight)
			l_piece.setText(Character.toString((char) 9822));
		else if (getPiece() instanceof Pawn)
			l_piece.setText(Character.toString((char) 9823));
		else
			l_piece.setText("");*/
		
		l_piece.setText(Character.toString(PieceRepresentation.getRepresentation(getPiece())));
		
		switch (getPiece().getColor())
		{
			case BLACK:
				l_piece.setForeground(java.awt.Color.BLACK);
				/*if (getPiece() instanceof King)
					l_piece.setText(Character.toString((char) 9818));
				else if (getPiece() instanceof Queen)
					l_piece.setText(Character.toString((char) 9819));
				else if (getPiece() instanceof Rook)
					l_piece.setText(Character.toString((char) 9820));
				else if (getPiece() instanceof Bishop)
					l_piece.setText(Character.toString((char) 9821));
				else if (getPiece() instanceof Knight)
					l_piece.setText(Character.toString((char) 9822));
				else if (getPiece() instanceof Pawn)
					l_piece.setText(Character.toString((char) 9823));*/
				break;
			case WHITE:
				l_piece.setForeground(java.awt.Color.WHITE);
				/*if (getPiece() instanceof King)
					l_piece.setText(Character.toString((char) 9812));
				else if (getPiece() instanceof Queen)
					l_piece.setText(Character.toString((char) 9813));
				else if (getPiece() instanceof Rook)
					l_piece.setText(Character.toString((char) 9814));
				else if (getPiece() instanceof Bishop)
					l_piece.setText(Character.toString((char) 9815));
				else if (getPiece() instanceof Knight)
					l_piece.setText(Character.toString((char) 9816));
				else if (getPiece() instanceof Pawn)
					l_piece.setText(Character.toString((char) 9817));*/
				break;
		}
	}
	
	/* OVERRIDES */
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Tile)) return false;
		Tile tile = (Tile) o;
		return getColor() == tile.getColor() &&
				Objects.equals(getPiece(), tile.getPiece());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getColor(), getPiece());
	}
	
	@Override
	public String toString() {
		return "Tile{" +
				"color=" + color +
				", piece=" + piece +
				'}';
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { TILE_DATA_FLAVOR };
	}
	
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(TILE_DATA_FLAVOR);
	}
	
	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return this;
	}
	
	@Override
	public void dragEnter(DropTargetDragEvent dtde) { }
	
	@Override
	public void dragOver(DropTargetDragEvent dtde) { }
	
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		Debug.println("Tile.dropActionChanged> dtde: " + dtde.toString());
	}
	
	@Override
	public void dragExit(DropTargetEvent dte) { }
	
	@Override
	public void drop(DropTargetDropEvent dtde) {
		Debug.println("Tile.drop> dtde: " + dtde.toString() +
				"\n\tdtde.getSource(): " + dtde.getSource().toString() +
				"\n\tdtde.getCurrentDataFlavors(): " + Arrays.toString(dtde.getCurrentDataFlavors()));
		
		if ((dtde.getDropAction() & TileTransferHandler.MOVE) != 0) {
			dtde.acceptDrop(dtde.getDropAction());
			Transferable t = dtde.getTransferable();
			
			try {
				dtde.dropComplete(dropComponent(t));
			} catch (Exception ex) {
				ex.printStackTrace();
				dtde.dropComplete(false);
			}
		}
		else
			dtde.rejectDrop();
	}
	
	public boolean dropComponent(Transferable t) throws IOException, UnsupportedFlavorException {
		Debug.println("Tile.dropComponent> t: " + t.toString() +
				"\n\tt.isDataFlavorSupported(TILE_DATA_FLAVOR): " + t.isDataFlavorSupported(TILE_DATA_FLAVOR));
		Object o = t.getTransferData(Tile.TILE_DATA_FLAVOR);
		
		if (o instanceof Tile)
		{
			Tile tile = (Tile) o;
			Debug.println("\ttile: (Piece)" + tile.getPiece().toString());
			
			CPoint currentPos = this.getPiece().getPosition();
			this.setPiece(tile.getPiece());
			this.getPiece().setPosition(currentPos);
			
			return true;
		}
		return false;
	}
}
