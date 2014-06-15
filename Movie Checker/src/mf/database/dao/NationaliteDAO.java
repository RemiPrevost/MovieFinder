package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.Nationalite;

public class NationaliteDAO extends DAO<Nationalite> {
	
	public NationaliteDAO(Connection conn) {
		super(conn);
	}

	//Ajoute un nation à la table nationalites
	public boolean create(Nationalite nation) {
		String query = "INSERT INTO nationalites (nation) VALUES (?)";
		
		if (nation.getNation().isEmpty())
			return false; 
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(nation.getNation()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (Exception e){
			return false;
		}
	}

	// retire de la table nationalites le nation reçu en paramètre
	public boolean delete(Nationalite nation) {
		String query = "DELETE FROM nationalites WHERE nation = ?";
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(nation.getNation()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}

	// modifie le nom d'un nation
	public boolean update(Nationalite old_nation, Nationalite new_nation) {
		String query ="UPDATE nationalites SET nation = ? WHERE nation = ?";
		
		if (new_nation.getNation().isEmpty())
			return false; 
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(new_nation.getNation()));
			prepare.setString(2, CastToInput(old_nation.getNation()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}

	// Recherche dans la table nationalites le nation reçu en paramètre
	public ArrayList<Nationalite> find(Nationalite nation) {
		String query = new String();
		ArrayList<Nationalite> liste_nation = new ArrayList<Nationalite>();
		ResultSet result;
		PreparedStatement prepare;
		Statement state;
			
		try {	
			if (nation.getNation().isEmpty()) {
				query = "SELECT * FROM nationalites ORDER BY nation";
				prepare = PsConnection.getInstance().prepareStatement(query);
			} 
			else {
				query = "SELECT * FROM nationalites WHERE nation = ?";
				prepare = PsConnection.getInstance().prepareStatement(query);
				prepare.setString(1, CastToInput(nation.getNation()));
			}
				
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			result = state.executeQuery(prepare.toString());
				
			while(result.next())
					liste_nation.add(new Nationalite(CastToOutput(result.getObject(1).toString())));
				
			result.close();
			state.close();

		} catch (SQLException e){
			e.printStackTrace();
		}
			
		return liste_nation;
	}
	
}
