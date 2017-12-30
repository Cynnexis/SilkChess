package fr.polytech.projet.silkchess.ui.preferences.generator;

public interface IGenerator {
	
	/**
	 * Convert the object to string
	 * @return the representation of the object
	 */
	String convertToString();
	
	/**
	 * Construct the object from string
	 * @param representation the string
	 * @return Return true if everything is ok, false otherwise
	 */
	boolean fromString(String representation);
}
