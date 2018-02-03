package fr.polytech.projet.silkchess.ui.window.components;

import com.sun.istack.internal.NotNull;
import fr.polytech.projet.silkchess.game.Color;
import fr.polytech.projet.silkchess.game.Player;
import fr.polytech.projet.silkchess.game.pieces.Piece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class PlayerBoardPanel extends JPanel {
	
	@NotNull
	private fr.polytech.projet.silkchess.game.Color color = fr.polytech.projet.silkchess.game.Color.BLACK;
	
	private JLabel l_token = new JLabel("");
	private JLabel l_nbRoundText = new JLabel("Number of rounds: ");
	private JLabel l_nbRound = new JLabel("");
	private JLabel l_killedEnemiesText = new JLabel("Killed enemies: ");
	private JScrollPane sp_killedEnemies = new JScrollPane();
	private JLabel l_killedEnemies = new JLabel("");
	
	private JPanel p_round = new JPanel();
	private GridLayout gl_round = new GridLayout(1, 2);
	
	private GridLayout gl_main = new GridLayout(5, 1, 5, 5);
	
	public PlayerBoardPanel(fr.polytech.projet.silkchess.game.Color color) {
		setColor(color);
		
		this.setLayout(gl_main);
		
		p_round.setLayout(gl_round);
		p_round.add(l_nbRoundText);
		p_round.add(l_nbRound);
		
		sp_killedEnemies.add(l_killedEnemies);
		
		this.add(l_token);
		this.add(p_round);
		this.add(l_killedEnemiesText);
		this.add(l_killedEnemies);
		
		this.setBorder(new EmptyBorder(5, 15, 5, 15));
	}
	
	public void fromPlayer(@NotNull Player player) {
		if (player == null)
			return;
		
		l_nbRound.setText(Integer.toString(player.getNbRound()));
		
		ArrayList<Piece> pieces = player.getKilledEnemies();
		l_killedEnemies.setText("");
		for (Piece p : pieces)
			l_killedEnemies.setText(l_killedEnemies.getText() + " " + PieceRepresentation.getRepresentation(p));
		l_killedEnemies.setForeground(getColor() == Color.BLACK ? java.awt.Color.WHITE : java.awt.Color.BLACK);
	}
	
	public void setTokenEnable(boolean enable) {
		l_token.setText(enable ? color.name() + ", you can play!" : "");
	}
	
	public void resetGraveyard() {
		l_killedEnemies.setText("");
	}
	
	/* GETTER & SETTER */
	
	public @NotNull fr.polytech.projet.silkchess.game.Color getColor() {
		return color;
	}
	
	public void setColor(@NotNull fr.polytech.projet.silkchess.game.Color color) {
		if (color != null)
			this.color = color;
	}
}
