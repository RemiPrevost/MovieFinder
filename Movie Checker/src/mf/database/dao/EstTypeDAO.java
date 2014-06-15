package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.EstType;
import mf.database.pojo.Film;
import mf.database.pojo.Type;
import mf.exception.bdd.InvalidName;

public class EstTypeDAO extends DAO<EstType> {

	public EstTypeDAO(Connection conn) {
		super(conn);
	}

	public boolean create(EstType est_type) {
		String query = "INSERT INTO est_type (genre, titre_f) VALUES (?,?)";
		
		if (est_type.getGenre().getGenre().isEmpty() || est_type.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(est_type.getGenre().getGenre()));
			prepare.setString(2, est_type.getFilm().getTitreDisque());
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			return false;
		}	
	}


	public boolean delete(EstType est_type) {
		String query = "DELETE FROM est_type WHERE genre = ? AND titre_f = ?";
		
		if (est_type.getGenre().getGenre().isEmpty() || est_type.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(est_type.getGenre().getGenre()));
			prepare.setString(2, est_type.getFilm().getTitreDisque());
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}


	public boolean update(EstType old_est_type, EstType new_est_type) {
		String query ="UPDATE est_type SET ";
		boolean has_previous = false;
		int num = 1;
		
		if (old_est_type.getGenre().getGenre().isEmpty() || old_est_type.getFilm().isEmpty() || new_est_type.isEmpty())
			return false;
		
		try {
			if (!new_est_type.getGenre().getGenre().isEmpty()) {
				query += " genre = ? ";
				has_previous = true;
			}
			if (!new_est_type.getFilm().isEmpty()){
				if (has_previous)
					query += " , titre_f = ? ";
				else
					query += " titre_f = ? ";
			}
			query += " WHERE genre = ? AND titre_f = ? ";
			
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			
			if (!new_est_type.getGenre().getGenre().isEmpty()) {
				prepare.setString(num, CastToInput(new_est_type.getGenre().getGenre()));
				num++;
			}
			if (!new_est_type.getFilm().isEmpty()){
				prepare.setString(num, new_est_type.getFilm().getTitreDisque());
				num++;
			}
			
			prepare.setString(num, CastToInput(old_est_type.getGenre().getGenre()));
			num++;
			prepare.setString(num, old_est_type.getFilm().getTitreDisque());
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}


	public ArrayList<EstType> find(EstType est_type) {
		ArrayList<EstType> liste_est_type = new ArrayList<EstType>();
		String query = "SELECT * FROM est_type WHERE ";
		
		boolean has_previous = false;
		int num = 1;
		
		ResultSet result;
		PreparedStatement prepare;
		Statement state;
			
		try {	
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			if (est_type.isEmpty()){
				query = "SELECT * FROM est_type";
			}
			if (!est_type.getGenre().isEmpty()) {
				query += " genre = ? ";
				has_previous = true;
			}
			if (!est_type.getFilm().isEmpty()) {
				if (has_previous)
					query += " AND titre_f = ?";
				else 
					query += " titre_f = ?";
			}
			
			query += " ORDER BY titre_f";
				
			prepare = PsConnection.getInstance().prepareStatement(query);
				
			if (!est_type.getGenre().isEmpty()){
				prepare.setString(num, CastToInput(est_type.getGenre().getGenre()));
				num++;
			}
			if (!est_type.getFilm().isEmpty()) {
				prepare.setString(num, est_type.getFilm().getTitreDisque());
				num++;
			}
			
			result = state.executeQuery(prepare.toString());
				
			while(result.next())
					liste_est_type.add(new EstType(new Film(result.getString(1)),new Type(CastToOutput(result.getString(2)))));
					
			result.close();
			state.close();
					
			} catch (SQLException | InvalidName e){
				e.printStackTrace();
			}
		
			return liste_est_type;
		}
	
}
