package fr.polytech.projet.silkchess.ui.window;

import fr.berger.enhancedlist.Matrix;
import fr.berger.enhancedlist.MatrixListener;
import fr.polytech.projet.silkchess.game.Engine;
import fr.polytech.projet.silkchess.game.pieces.Piece;
import fr.polytech.projet.silkchess.ui.window.components.JChessboard;
import fr.polytech.projet.silkchess.ui.window.components.Tile;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
	
	// Singleton Pattern
	private static Frame frame = new Frame();
	
	private Engine engine = new Engine();
	
	private JChessboard chessboard = new JChessboard(engine.getBoard().getNbColumns(), engine.getBoard().getNbRows());
	
	private Frame() {
		engine.getBoard().setListener(new MatrixListener() {
			@Override
			public void OnCellChanged(int x, int y, Object value) {
				if (!(value instanceof Piece))
					return;
				
				try  {
					Piece p = (Piece) value;
					chessboard.set(x, y, p);
				} catch (ClassCastException ex) {
					ex.printStackTrace();
				}
			}
			
			@Override
			public void OnCellRead(int x, int y, Object value) {
			
			}
		});
		
		engine.newGame();
	}
	
	public void display() {
		
		this.setTitle(System.getProperty("program.name"));
		this.setSize(640, 480);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.getContentPane().add(chessboard, BorderLayout.CENTER);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		SwingUtilities.updateComponentTreeUI(this);
		
		this.setVisible(true);
	}
	
	public static Frame getInstance() {
		return frame;
	}
}
