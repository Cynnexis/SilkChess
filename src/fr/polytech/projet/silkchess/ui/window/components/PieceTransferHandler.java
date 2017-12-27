package fr.polytech.projet.silkchess.ui.window.components;

import fr.polytech.projet.silkchess.debug.Debug;
import fr.polytech.projet.silkchess.game.CPoint;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.util.Comparator;

public class PieceTransferHandler extends TransferHandler {
	
	/**
	 * Méthode permettant à l'objet de savoir si les données reçues
	 * via un drop sont autorisées à être importées
	 * @param support
	 * @return boolean
	 */
	@Override
	public boolean canImport(TransferSupport support) {
		return support.getComponent() instanceof Tile && support.isDataFlavorSupported(Piece.PIECE_DATA_FLAVOR);
	}
	
	/**
	 * C'est ici que l'insertion des données dans notre composant est réalisée
	 * @param support
	 * @return boolean
	 */
	@Override
	public boolean importData(TransferSupport support) {
		boolean accept = false;
		
		Debug.println("PieceTransferHandler.importData> support: " + support);
		
		if (canImport(support))
		{
			Debug.println("\tcanImport(support): true");
			try {
				Debug.println("\tsupport.getComponent(): (Tile) (Piece) " + ((Tile) support.getComponent()).getPiece().toString());
				Transferable t = support.getTransferable();
				Debug.println("\tt: " + t.toString());
				Object value = t.getTransferData(Piece.PIECE_DATA_FLAVOR);
				Debug.println("\tvalue: " + value.toString());
				
				if (value instanceof Piece) {
					Piece piece = (Piece) value;
					Component comp = support.getComponent();
					
					Debug.println("\tcomp: " + comp.toString());
					if (comp instanceof Tile) {
						Tile tile = ((Tile) comp);
						CPoint newPos = tile.getPiece().getPosition();
						tile.setPiece((Piece) value);
						tile.getPiece().setPosition(newPos);
						accept = true;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return accept;
	}
	
	/**
	 * Dans cette méthode, nous allons créer l'objet utilisé par le système de drag'n drop
	 * afin de faire circuler les données entre les composants
	 * Vous pouvez voir qu'il s'agit d'un objet de type Transferable
	 * @param c
	 * @return
	 */
	@Override
	protected Transferable createTransferable(JComponent c) {
		Transferable t = null;
		
		if (c instanceof Tile) {
			Tile tile = (Tile) c;
			t = tile.getPiece();
		}
		
		return t;
	}
	
	/**
	 * Cette méthode est invoquée à la fin de l'action DROP
	 * Si des actions sont à faire ensuite, c'est ici qu'il faudra coder le comportement désiré
	 * @param source
	 * @param data
	 * @param action
	 */
	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		Debug.println("PieceTransferHandler.exportDone> source: " + source.toString() + "\t\ndata: " + data.toString() + "\t\naction: " + action);
	}
	
	/**
	 * Cette méthode est utilisée afin de déterminer le comportement
	 * du composant vis-à-vis du drag'n drop : nous retrouverons
	 * nos variables statiques COPY, MOVE, COPY_OR_MOVE, LINK ou NONE
	 * @param c
	 * @return int
	 */
	@Override
	public int getSourceActions(JComponent c) {
		return MOVE;
	}
}
