package fr.polytech.projet.silkchess;

import fr.polytech.projet.silkchess.ui.window.Frame;

public class SilkChess {
	
	public static void main(String[] args) {
		System.setProperty("program.name", "Silk Chess");
		
		Frame.getInstance().display();
	}
}

