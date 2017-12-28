package fr.polytech.projet.silkchess.ui.preferences;

import com.sun.istack.internal.NotNull;

import java.util.prefs.Preferences;

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
	
	@SuppressWarnings("unchecked")
	public static <T> T get(EPref ePref) {
		try {
			if (ePref.getAssociatedValueClass().isInstance(""))
				return (T) ePref.getAssociatedValueClass().cast(preferences.get(ePref.getKey(), (String) ePref.getDefaultValue()));
			
			if (ePref.getAssociatedValueClass().isInstance(true))
				return (T) ePref.getAssociatedValueClass().cast(preferences.getBoolean(ePref.getKey(), (boolean) ePref.getDefaultValue()));
			
			if (ePref.getAssociatedValueClass().isInstance(new byte[0]))
				return (T) ePref.getAssociatedValueClass().cast(preferences.getByteArray(ePref.getKey(), (byte[]) ePref.getDefaultValue()));
			
			if (ePref.getAssociatedValueClass().isInstance(1.0d))
				return (T) ePref.getAssociatedValueClass().cast(preferences.getDouble(ePref.getKey(), (double) ePref.getDefaultValue()));
			
			if (ePref.getAssociatedValueClass().isInstance(1.0f))
				return (T) ePref.getAssociatedValueClass().cast(preferences.getFloat(ePref.getKey(), (float) ePref.getDefaultValue()));
			
			if (ePref.getAssociatedValueClass().isInstance(1))
				return (T) ePref.getAssociatedValueClass().cast(preferences.getInt(ePref.getKey(), (int) ePref.getDefaultValue()));
			
			if (ePref.getAssociatedValueClass().isInstance(1L))
				return (T) ePref.getAssociatedValueClass().cast(preferences.getLong(ePref.getKey(), (long) ePref.getDefaultValue()));
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static <T> void set(EPref ePref, T value) {
		try {
			if (ePref.getAssociatedValueClass().isInstance(""))
				preferences.put(ePref.getKey(), (String) ePref.getAssociatedValueClass().cast(value));
			else if (ePref.getAssociatedValueClass().isInstance(true))
				preferences.putBoolean(ePref.getKey(), (Boolean) ePref.getAssociatedValueClass().cast(value));
			else if (ePref.getAssociatedValueClass().isInstance(new byte[0]))
				preferences.putByteArray(ePref.getKey(), (byte[]) ePref.getAssociatedValueClass().cast(value));
			else if (ePref.getAssociatedValueClass().isInstance(1.0d))
				preferences.putDouble(ePref.getKey(), (Double) ePref.getAssociatedValueClass().cast(value));
			else if (ePref.getAssociatedValueClass().isInstance(1.0f))
				preferences.putFloat(ePref.getKey(), (Float) ePref.getAssociatedValueClass().cast(value));
			else if (ePref.getAssociatedValueClass().isInstance(1))
				preferences.putInt(ePref.getKey(), (Integer) ePref.getAssociatedValueClass().cast(value));
			else if (ePref.getAssociatedValueClass().isInstance(1L))
				preferences.putLong(ePref.getKey(), (Long) ePref.getAssociatedValueClass().cast(value));
		} catch (ClassCastException ex) {
			ex.printStackTrace();
		}
	}
	
	/* GETTER & SETTER */
	
	public static Pref getInstance() {
		return instance;
	}
	
	private static void setInstance(Pref pref1) {
		instance = pref1;
	}
	
	public static Preferences getPreferences() {
		return preferences;
	}
	
	public static void setPreferences(@NotNull Preferences preferences1) {
		preferences = preferences1;
	}
}
