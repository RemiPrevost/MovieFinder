package mf.database.dao;

import java.sql.Connection;
import java.util.ArrayList;

public abstract class DAO<T> {
	protected Connection connect = null;
	
	public DAO(Connection conn) {
		this.connect = conn;
	}
	
	public abstract boolean create(T obj);
	
	public abstract boolean delete(T obj);
	
	public abstract boolean update(T old_obj, T new_obj);
	
	public abstract ArrayList<T> find(T obj);
	
	protected final String CastToInput(String str_in) {
		String str_out = str_in;
		
		/* élimine des espaces en début */
		while (str_out.charAt(0) == ' ')
			str_out = str_out.substring(1);
		
		/* élimine des espaces à la fin */
		while (str_out.charAt(str_out.length() -1) == ' ')
			str_out = str_out.substring(0, str_out.length() -1);
		
		int i = 1;
		boolean space = false;
		/* élimine des espaces entre les mots lorqu'il y a plus de deux espaces consécutifs */
		while (i < str_out.length()-1) {
			if (str_out.charAt(i) == ' ') {
				if (!space)
					space = true;
				else {
					str_out = str_out.substring(0, i) + str_out.substring(i+1, str_out.length());
					space = false;
				}
			}
			else
				space = false;
			
			i++;
		}
		
		return str_out.toLowerCase();
	}
	
	protected final String CastToOutput(String str_in) {
		if (str_in.isEmpty()) {
			return str_in;
		}
		
		String str_out = str_in.toLowerCase();
				
		/* élimine des espaces en début */
		while (str_out.charAt(0) == ' ')
			str_out = str_out.substring(1);
		
		/* élimine des espaces à la fin */
		while (str_out.charAt(str_out.length() -1) == ' ')
			str_out = str_out.substring(0, str_out.length() -1);
		
		/* mise en majuscule de la première lettre */
		str_out = str_out.substring(0, 1).toUpperCase() + str_out.substring(1,str_out.length());
		
		int i = 1;
		boolean space = false;
		/* élimine des espaces entre les mots lorqu'il y a plus de deux espaces consécutifs */
		while (i < str_out.length()-1) {
			if (str_out.charAt(i) == ' ') {
				if (!space)
					space = true;
				else {
					str_out = str_out.substring(0, i) + str_out.substring(i+1, str_out.length());
					space = false;
				}
			}
			else
				space = false;
			
			i++;
		}
		
		i = 1;
		/* pour toutes les lettres de début de mot, mise en majuscule */ 
		while (i < str_out.length()-1) {
			if (str_out.charAt(i) == ' ' || str_out.charAt(i) == '\'')
				str_out = str_out.substring(0,i+1) + str_out.substring(i+1, i+2).toUpperCase() + str_out.substring(i+2,str_out.length());

			i++;
		}
		
		return str_out;
	}
}
