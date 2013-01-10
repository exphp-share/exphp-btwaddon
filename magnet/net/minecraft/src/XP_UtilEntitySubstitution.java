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
	
	private Method __mappingMethod;
	private Map<Class<? extends Entity>, Class<? extends Entity>> __substitutionMap;
	
	private XP_UtilEntitySubstitution() {
		this.__substitutionMap = new HashMap<Class<? extends Entity>, Class<? extends Entity>>();
		
		this.__mappingMethod = this.__findAddMappingMethod();
	}

	// Initialization
	private Method __findAddMappingMethod() {
		Method[] methods = EntityList.class.getDeclaredMethods();
		
		// this.__debugMethodList(methods);
		
		// Search the list of EntityList methods
		int numMethods = methods.length;
		for (int i=0; i<numMethods; ++i) {
			Method m = methods[i];
			Type[] paramTypes = m.getParameterTypes();
			
			// Find the method by looking for its return type and signature
			if (m.getReturnType().equals(Void.TYPE)
			    && paramTypes.length == 3
			    && paramTypes[0] == Class.class
			    && paramTypes[1] == String.class
			    && paramTypes[2] == int.class) {
				
				// Method has been found (probably). Make it public and return it.
				m.setAccessible(true);
				return m;
			}
		}
		
		// Fail fast if the method signature appears to have changed.
		throw new RuntimeException("Fail-fast mechanism triggered! No matching signature found for addMapping().");
	}
	
	private void __debugMethodList(Method[] methods) {
		System.out.println("---Printing EntityList method signatures---");
		int numMethods = methods.length;
		for (int i=0; i<numMethods; ++i) {
			Method m = methods[i];
			System.out.print(String.format("[%d]%s %s(", i, m.getReturnType().toString(), m.getName()));
			
			Type[] paramTypes = m.getParameterTypes();
			int numParams = paramTypes.length; 
			for (int j=0; j<numParams-1; ++j) {
				System.out.print(String.format("%s, ", paramTypes[j].toString()));
			}
			System.out.print(paramTypes[numParams-1].toString() + ")");
			System.out.println();
		}
	}
	
	/**
	 * Maps entities of a certain class to be replaced by entities
	 * @param oldClass Class of entities to replace
	 * @param newClass Class of entities to use in their place
	 * @param name 
	 * @param id
	 */
	public void addSubstitution(Class<? extends Entity> oldClass, Class<? extends Entity> newClass, String name, int id) {
		try {
			// Map the new class to load from NBT
			this.__mappingMethod.invoke(oldClass, name, id);
			
		// Rethrow compiler exceptions at runtime
		} catch(InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch(IllegalAccessException e)    {
			throw new RuntimeException(e);
		}
		
		// Map the class to be converted immediately following spawn.
		this.__substitutionMap.put(oldClass, newClass);
	}
	
	public void replaceNewEntities() {
		// TODO
	}
}
