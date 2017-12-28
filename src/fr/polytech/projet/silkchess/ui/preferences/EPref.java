package fr.polytech.projet.silkchess.ui.preferences;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;

public enum EPref implements Serializable {
	FIRSTTIME("firsttime", boolean.class, false),
	CHESSBOARD_CONTROL("chessboard.control", boolean.class, false);
	
	@NotNull
	private String key  = "";
	
	@NotNull
	private Class<?> associatedValueClass = String.class;
	
	@NotNull
	private Object defaultValue = "";
	
	EPref(String key, Class<?> associatedValueClass, Object defaultValue) {
		setKey(key);
		setAssociatedValueClass(associatedValueClass);
		setDefaultValue(defaultValue);
	}
	
	/* GETTERS & SETTERS */
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Class<?> getAssociatedValueClass() {
		return associatedValueClass;
	}
	
	public void setAssociatedValueClass(Class<?> associatedValueClass) {
		this.associatedValueClass = associatedValueClass;
	}
	
	public Object getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
}
