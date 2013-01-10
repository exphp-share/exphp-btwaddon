package net.minecraft.src;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class XP_UtilEntitySubstitution {
	
	/**
	 * Returns the instance of this singleton class.
	 */
	public static XP_UtilEntitySubstitution instance = new XP_UtilEntitySubstitution();
	
	private Map<Class<? extends Entity>, Class<? extends Entity>> __substitutionMap;
	
	private XP_UtilEntitySubstitution() {
		this.__substitutionMap = new HashMap<Class<? extends Entity>, Class<? extends Entity>>();
	}
	
	/**
	 * Maps entities of a certain class to be replaced by entities of another class.
	 * @param oldClass Class of entities to replace
	 * @param newClass Class of entities to use in their place
	 * @param name The string associated with oldClass in EntityList
	 * @param id The index associated with oldClass in EntityList
	 */
	public void addSubstitution(Class<? extends Entity> oldClass, Class<? extends Entity> newClass, String name, int id) {
		// Instantiate the new entity in place of the original when reading from NBT.
		ModLoader.registerEntityID(newClass, name, id);
		
		// Map the class to be converted immediately following spawn.
		this.__substitutionMap.put(oldClass, newClass);
	}
	
	public void replaceNewEntities() {
		// TODO
	}
}
