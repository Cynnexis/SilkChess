package fr.polytech.projet.silkchess;

import fr.polytech.projet.silkchess.ui.window.Frame;

public class SilkChess {
	
	public static void main(String[] args) {
		// TODO: Make a chess... Good luck!
		
		System.setProperty("program.name", "Silk Chess");
		
		Frame.getInstance().display();
	}
}

/**
 * TODO:
 * Add a settings dialog, containing:
 * - Choose between Drag'n Drop or clicking
 *
 * Make the Saver
 */
