package fr.polytech.projet.silkchess.ui.window;

import com.sun.media.sound.InvalidFormatException;
import fr.berger.enhancedlist.matrix.Matrix;
import fr.berger.enhancedlist.matrix.MatrixListener;
import fr.berger.enhancedlist.Point;
import fr.polytech.projet.silkchess.game.*;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.ai.IntelligenceMode;
import fr.polytech.projet.silkchess.game.board.Chessboard;
import fr.polytech.projet.silkchess.game.exceptions.NoPieceException;
import fr.polytech.projet.silkchess.game.exceptions.PieceCannotMove;
import fr.polytech.projet.silkchess.game.exceptions.PieceDoesNotBelongToPlayerException;
import fr.polytech.projet.silkchess.game.exceptions.TileFullException;
import fr.polytech.projet.silkchess.game.io.Saver;
import fr.polytech.projet.silkchess.game.pieces.Piece;
import fr.polytech.projet.silkchess.ui.window.components.JChessboard;
import fr.polytech.projet.silkchess.ui.window.components.JChessboardListener;
import fr.polytech.projet.silkchess.ui.window.components.PlayerBoardPanel;
import fr.polytech.projet.silkchess.ui.window.components.Tile;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class Frame extends JFrame {
	
	// Singleton Pattern
	private static Frame frame = new Frame();
	
	private Engine engine = new Engine();
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu m_file = new JMenu("File");
	private JMenuItem mi_newGame = new JMenuItem("New Game");
	private JMenuItem mi_save = new JMenuItem("Save");
	private JMenuItem mi_open = new JMenuItem("Open");
	private JMenuItem mi_quit = new JMenuItem("Exit");
	
	private JMenu m_playAgainst = new JMenu("Play Against...");
	private ButtonGroup bg_playAgainst = new ButtonGroup();
	private JRadioButtonMenuItem mi_playAgainstPlayer = new JRadioButtonMenuItem("Play Against Player");
	private JRadioButtonMenuItem mi_playAgainstKartona = new JRadioButtonMenuItem("Play Against Kartona");
	private JMenu m_kartonaDifficulty = new JMenu("Kartona Difficulty");
	private ButtonGroup bg_kartonaDifficulty = new ButtonGroup();
	private JRadioButtonMenuItem mi_kartonaRandom = new JRadioButtonMenuItem("Random");
	private JRadioButtonMenuItem mi_kartonaStrategy = new JRadioButtonMenuItem("Strategy");
	
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
				switch (newState) {
					case INITIALIZING:
						chessboard.resetHightlight();
						break;
					case PLAYING:
						break;
					case PAUSE:
						break;
					case B_WIN:
						JOptionPane.showMessageDialog(Frame.this, "Black won!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
						chessboard.resetHightlight();
						break;
					case W_WIN:
						JOptionPane.showMessageDialog(Frame.this, "White won!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
						chessboard.resetHightlight();
						break;
				}
			}
			
			@Override
			public void onCheckStateChanged(CheckState check) {
				switch (check)
				{
					case NO_CHECKSTATE:
						break;
					case B_CHECK:
						JOptionPane.showMessageDialog(Frame.this, "Black is in check", "Check", JOptionPane.INFORMATION_MESSAGE);
						chessboard.resetHightlight();
						break;
					case W_CHECK:
						JOptionPane.showMessageDialog(Frame.this, "White is in check", "Check", JOptionPane.INFORMATION_MESSAGE);
						chessboard.resetHightlight();
						break;
					case B_CHECKMATE:
						JOptionPane.showMessageDialog(Frame.this, "Black is checkmate!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
						chessboard.resetHightlight();
						break;
					case W_CHECKMATE:
						JOptionPane.showMessageDialog(Frame.this, "Black is checkmate!", "Checkmate", JOptionPane.INFORMATION_MESSAGE);
						chessboard.resetHightlight();
						break;
					case STALEMATE:
						JOptionPane.showMessageDialog(Frame.this, "Stalemate!", "Stalemate", JOptionPane.INFORMATION_MESSAGE);
						chessboard.resetHightlight();
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
							} catch (NoPieceException ignored) {
								//ignored.printStackTrace();
							}
						}
						else {
							chessboard.setSelectedTile(null);
						}
					}
					else {
						try {
							engine.play(selectedTile.getPiece().getPosition(), tile.getPiece().getPosition());
						} catch (NoPieceException | PieceDoesNotBelongToPlayerException | TileFullException | PieceCannotMove ignored) {
							//ignored.printStackTrace();
						} catch (NullPointerException e) {
							e.printStackTrace();
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
		
		mi_newGame.addActionListener((ActionEvent e) -> {
			if (engine != null)
				engine.newGame(true);
		});
		
		mi_save.addActionListener((ActionEvent e) -> {
			if (engine != null) {
				JFileChooser jfc = new JFileChooser("./");
				jfc.setDialogTitle("Save chessboard...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file | (*.txt)", "txt");
				jfc.addChoosableFileFilter(filter);
				jfc.setFileFilter(filter);
				int returnValue = jfc.showSaveDialog(Frame.this);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String path = jfc.getSelectedFile().getAbsolutePath();
					if (!path.endsWith(".txt"))
						path += ".txt";
					if (!Saver.save(engine.getBoard(), path))
						Saver.save(engine.getBoard());
				}
			}
		});
		
		mi_open.addActionListener((ActionEvent e) -> {
			if (engine != null) {
				JFileChooser jfc = new JFileChooser("./");
				jfc.setDialogTitle("Open a chessboard...");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setMultiSelectionEnabled(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text file | (*.txt)", "txt");
				jfc.addChoosableFileFilter(filter);
				int returnValue = jfc.showOpenDialog(Frame.this);
				
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					Chessboard board = null;
					try {
						board = Saver.load(jfc.getSelectedFile());
					} catch (InvalidFormatException e1) {
						e1.printStackTrace();
					}
					
					if (board == null) {
						JOptionPane.showMessageDialog(Frame.this, "The file not found", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else {
						for (int i = 0; i < engine.getBoard().getNbColumns(); i++)
							for (int j = 0; j < engine.getBoard().getNbRows(); j++)
								engine.getBoard().set(i, j, board.get(i, j));
						
						engine.newGame(false);
						chessboard.resetHightlight();
						pbp_black.resetGraveyard();
						pbp_white.resetGraveyard();
					}
				}
			}
		});
		
		mi_playAgainstPlayer.addActionListener((ActionEvent e) -> {
			if (engine != null) {
				engine.setUseKartona(false);
				m_kartonaDifficulty.setEnabled(false);
			}
		});
		
		mi_playAgainstKartona.addActionListener((ActionEvent e) -> {
			if (engine != null) {
				engine.setUseKartona(true);
				m_kartonaDifficulty.setEnabled(true);
			}
		});
		
		mi_kartonaRandom.addActionListener((ActionEvent e) -> {
			if (engine != null)
				engine.getKartona().setMode(IntelligenceMode.RANDOM);
		});
		
		mi_kartonaStrategy.addActionListener((ActionEvent e) -> {
			if (engine != null)
				engine.getKartona().setMode(IntelligenceMode.STRATEGY);
		});
		
		mi_quit.addActionListener((ActionEvent e) -> Frame.this.dispose());
		
		m_file.add(mi_newGame);
		m_file.addSeparator();
		m_file.add(mi_save);
		m_file.add(mi_open);
		m_file.addSeparator();
		m_file.add(mi_quit);
		
		bg_playAgainst.add(mi_playAgainstPlayer);
		bg_playAgainst.add(mi_playAgainstKartona);
		
		mi_playAgainstPlayer.setSelected(true);
		
		bg_kartonaDifficulty.add(mi_kartonaRandom);
		bg_kartonaDifficulty.add(mi_kartonaStrategy);
		
		mi_kartonaRandom.setSelected(true);
		
		m_kartonaDifficulty.add(mi_kartonaRandom);
		m_kartonaDifficulty.add(mi_kartonaStrategy);
		
		m_playAgainst.add(mi_playAgainstPlayer);
		m_playAgainst.add(mi_playAgainstKartona);
		m_playAgainst.addSeparator();
		m_playAgainst.add(m_kartonaDifficulty);
		
		menuBar.add(m_file);
		menuBar.add(m_playAgainst);
		
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
		this.setJMenuBar(menuBar);
		
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
