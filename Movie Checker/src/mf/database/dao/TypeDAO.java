package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.Type;

public class TypeDAO extends DAO<Type> {

	public TypeDAO(Connection conn) {
		super(conn);
	}

	//Ajoute un genre de film à la table types
	public boolean create(Type genre) {
		String query = "INSERT INTO types (nom_t) VALUES (?)";
		
		if (genre.getGenre().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(genre.getGenre()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			return false;
		}
	}
	
	// retire de la table types le genre reçu en paramètre
	public boolean delete(Type genre) {
		String query = "DELETE FROM types WHERE nom_t = ?";
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(genre.getGenre()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}

	// modifie le nom d'un genre
	public boolean update(Type old_genre, Type new_genre) {
		String query ="UPDATE types SET nom_t = ? WHERE nom_t = ?";
		
		if (new_genre.getGenre().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(new_genre.getGenre()));
			prepare.setString(2, CastToInput(old_genre.getGenre()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}
	
	// Recherche dans la table types le genre reçu en paramètre
	public ArrayList<Type> find(Type genre) {
		String query = new String();
		ArrayList<Type> liste_genre = new ArrayList<Type>();
		ResultSet result;
		PreparedStatement prepare;
		Statement state;
		
		try {	
			if (genre.getGenre().isEmpty()) {
				query = "SELECT * FROM types ORDER BY nom_t";
				prepare = PsConnection.getInstance().prepareStatement(query);
			} 
			else {
				query = "SELECT * FROM types WHERE nom_t = ?";
				prepare = PsConnection.getInstance().prepareStatement(query);
				prepare.setString(1, CastToInput(genre.getGenre()));
			}
			
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			result = state.executeQuery(prepare.toString());
			
			while(result.next())
					liste_genre.add(new Type(CastToOutput(result.getObject(1).toString())));
			
			result.close();
			state.close();

		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return liste_genre;
	}

	


}
