package fr.polytech.projet.silkchess.ui.window;

import fr.berger.enhancedlist.Matrix;
import fr.berger.enhancedlist.MatrixListener;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.*;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.exceptions.PieceCannotMove;
import fr.polytech.projet.silkchess.game.exceptions.PieceDoesNotBelongToPlayerException;
import fr.polytech.projet.silkchess.game.exceptions.TileFullException;
import fr.polytech.projet.silkchess.game.pieces.Piece;
import fr.polytech.projet.silkchess.ui.window.components.JChessboard;
import fr.polytech.projet.silkchess.ui.window.components.JChessboardListener;
import fr.polytech.projet.silkchess.ui.window.components.PlayerBoardPanel;
import fr.polytech.projet.silkchess.ui.window.components.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Frame extends JFrame {
	
	// Singleton Pattern
	private static Frame frame = new Frame();
	
	private Engine engine = new Engine();
	
	private PlayerBoardPanel pbp_black = new PlayerBoardPanel(Color.BLACK);
	private JChessboard chessboard = new JChessboard(engine.getBoard().getNbColumns(), engine.getBoard().getNbRows());
	private PlayerBoardPanel pbp_white = new PlayerBoardPanel(Color.WHITE);
	
	// The tile which is selected, waiting for the user to click on a second tile to move a piece
	private Tile selectedTile = null;
	
	private Frame() {
		engine.setEngineListener(new EngineListener() {
			@Override
			public void onPieceMoved(CPoint source, Piece piece) {
				if (chessboard != null && engine != null && engine.getBoard() != null)
					chessboard.set(engine.getBoard());
			}
			
			@Override
			public void onTokenChanged(Color token) {
				switch (token)
				{
					case BLACK:
						pbp_black.setTokenEnable(true);
						pbp_white.setTokenEnable(false);
						break;
					case WHITE:
						pbp_black.setTokenEnable(false);
						pbp_white.setTokenEnable(true);
						break;
				}
			}
			
			@Override
			public void onGameStateChanged(GameState oldState, GameState newState) {
			
			}
			
			@Override
			public void onCheckStateChanged(CheckState check) {
				switch (check)
				{
					case NO_CHECKSTATE:
						break;
					case B_CHECK:
						JOptionPane.showMessageDialog(Frame.this, "Check", "Black is in check", JOptionPane.INFORMATION_MESSAGE);
						break;
					case W_CHECK:
						JOptionPane.showMessageDialog(Frame.this, "Check", "White is in check", JOptionPane.INFORMATION_MESSAGE);
						break;
					case B_CHECKMATE:
						JOptionPane.showMessageDialog(Frame.this, "Checkmate", "Black is checkmate!", JOptionPane.INFORMATION_MESSAGE);
						break;
					case W_CHECKMATE:
						JOptionPane.showMessageDialog(Frame.this, "Checkmate", "Black is checkmate!", JOptionPane.INFORMATION_MESSAGE);
						break;
					case STALEMATE:
						JOptionPane.showMessageDialog(Frame.this, "Stalemate", "Stalemate!", JOptionPane.INFORMATION_MESSAGE);
						break;
				}
			}
			
			@Override
			public void onPieceKilled(Piece pieceKilled) {
			
			}
		});
		
		engine.getBoard().setListener(new MatrixListener() {
			@Override
			public void OnCellChanged(int x, int y, Object value) {
				if (chessboard != null && engine != null && engine.getBoard() != null)
					chessboard.set(engine.getBoard());
			}
			
			@Override
			public void OnCellRead(int x, int y, Object value) { }
		});
		
		engine.getpBlack().setPlayerListener(new PlayerListener() {
			@Override
			public void onColorChanged(Color color) { }
			
			@Override
			public void onNbRoundChanged(int nbRound) {
				pbp_black.fromPlayer(engine.getpBlack());
			}
			
			@Override
			public void onKilledEnemyAdded(Piece killedEnemy) {
				pbp_black.fromPlayer(engine.getpBlack());
			}
		});
		
		engine.getpWhite().setPlayerListener(new PlayerListener() {
			@Override
			public void onColorChanged(Color color) { }
			
			@Override
			public void onNbRoundChanged(int nbRound) {
				pbp_white.fromPlayer(engine.getpWhite());
			}
			
			@Override
			public void onKilledEnemyAdded(Piece killedEnemy) {
				pbp_white.fromPlayer(engine.getpWhite());
			}
		});
		
		chessboard.setJChessboardListener(new JChessboardListener() {
			@Override
			public void onSelectedTileChanged(Tile tile) {
				if (tile != null) {
					if (selectedTile == null) {
						Piece piece = tile.getPiece();
						if (piece.getColor() == engine.getToken()) {
							try {
								ArrayList<CPoint> list = MoveManager.computeAllPossibleMoveWithCheck(engine.getBoard(), engine.getToken(), piece.getPosition());
								for (CPoint c : list)
									chessboard.hightlightTile(CPoint.toPoint(c));
								
								selectedTile = tile;
							} catch (NoPieceException e) {
								e.printStackTrace();
							}
						}
						else {
							chessboard.setSelectedTile(null);
						}
					}
					else {
						try {
							engine.play(selectedTile.getPiece().getPosition(), tile.getPiece().getPosition());
						} catch (NullPointerException e) {
							e.printStackTrace();
						} catch (NoPieceException e) {
							e.printStackTrace();
						} catch (PieceDoesNotBelongToPlayerException e) {
							e.printStackTrace();
						} catch (TileFullException e) {
							e.printStackTrace();
						} catch (PieceCannotMove pieceCannotMove) {
							pieceCannotMove.printStackTrace();
						}
						
						chessboard.resetHightlight();
						selectedTile = null;
					}
				}
				else {
					chessboard.resetHightlight();
					selectedTile = null;
				}
			}
		});
		
		engine.newGame();
	}
	
	public void display() {
		Dimension dim = new Dimension(640, 640);
		
		this.setTitle(System.getProperty("program.name"));
		this.setSize(dim);
		this.setMinimumSize(dim);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		this.getContentPane().add(pbp_black, BorderLayout.NORTH);
		this.getContentPane().add(chessboard, BorderLayout.CENTER);
		this.getContentPane().add(pbp_white, BorderLayout.SOUTH);
		
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
