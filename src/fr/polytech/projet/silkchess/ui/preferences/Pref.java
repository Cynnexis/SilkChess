package fr.polytech.projet.silkchess.ui.preferences;

import com.sun.istack.internal.NotNull;
import fr.polytech.projet.silkchess.ui.preferences.generator.Generator;

import java.util.prefs.Preferences;

/**
 * @author Valentin Berger
 * @see Generator
 */
@Deprecated
public class Pref {
	
	/**
	 * Singleton design pattern
	 */
	@NotNull
	private static Pref instance = new Pref();

	@NotNull
	private static Preferences preferences = Preferences.userNodeForPackage(fr.polytech.projet.silkchess.SilkChess.class);
	
	private Pref() {
		preferences = Preferences.userNodeForPackage(fr.polytech.projet.silkchess.SilkChess.class);
		if (!preferences.getBoolean(EPref.FIRSTTIME.getKey(), false))
		{
			preferences.putBoolean(EPref.FIRSTTIME.getKey(), true);
			preferences.putBoolean(EPref.CHESSBOARD_CONTROL.getKey(), false);
		}
	}
	
	public static String getString(@NotNull EPref ePref) {
		return preferences.get(ePref.getKey(), (String) ePref.getDefaultValue());
	}
	
	public static boolean getBoolean(@NotNull EPref ePref) {
		return preferences.getBoolean(ePref.getKey(), (boolean) ePref.getDefaultValue());
	}
	
	public static byte[] getByteArray(@NotNull EPref ePref) {
		return preferences.getByteArray(ePref.getKey(), (byte[]) ePref.getDefaultValue());
	}
	
	public static double getDouble(@NotNull EPref ePref) {
		return preferences.getDouble(ePref.getKey(), (double) ePref.getDefaultValue());
	}
	
	public static float getFloat(@NotNull EPref ePref) {
		return preferences.getFloat(ePref.getKey(), (float) ePref.getDefaultValue());
	}
	
	public static int getInt(@NotNull EPref ePref) {
		return preferences.getInt(ePref.getKey(), (int) ePref.getDefaultValue());
	}
	
	public static long getLong(@NotNull EPref ePref) {
		return preferences.getLong(ePref.getKey(), (long) ePref.getDefaultValue());
	}
	
	public static <T extends Generator> T getObject(@NotNull EPref ePref, @NotNull T instance) {
		instance.fromString(preferences.get(ePref.getKey(), (String) ePref.getDefaultValue()));
		return instance;
	}
	
	
	public static void putString(@NotNull EPref ePref, @NotNull String value) {
		preferences.put(ePref.getKey(), value);
	}
	
	public static void putBoolean(@NotNull EPref ePref, boolean value) {
		preferences.putBoolean(ePref.getKey(), value);
	}
	
	public static void putByteArray(@NotNull EPref ePref, @NotNull byte[] value) {
		preferences.putByteArray(ePref.getKey(), value);
	}
	
	public static void putDouble(@NotNull EPref ePref, double value) {
		preferences.putDouble(ePref.getKey(), value);
	}
	
	public static void putFloat(@NotNull EPref ePref, float value) {
		preferences.putFloat(ePref.getKey(), value);
	}
	
	public static void putInt(@NotNull EPref ePref, int value) {
		preferences.putInt(ePref.getKey(), value);
	}
	
	public static void putLong(@NotNull EPref ePref, long value) {
		preferences.putLong(ePref.getKey(), value);
	}
	
	public static <T extends Generator> void putObject(@NotNull EPref ePref, @NotNull T value) {
		preferences.put(ePref.getKey(), value.convertToString());
	}
	
	/* GETTER & SETTER */
	
	public static Pref getInstance() {
		return instance;
	}
	
	private static void setInstance(@NotNull Pref pref1) {
		instance = pref1;
	}
	
	public static Preferences getPreferences() {
		return preferences;
	}
	
	public static void setPreferences(@NotNull Preferences preferences1) {
		preferences = preferences1;
	}
}
