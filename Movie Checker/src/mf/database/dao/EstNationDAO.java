package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.EstNation;
import mf.database.pojo.Film;
import mf.database.pojo.Nationalite;
import mf.exception.bdd.InvalidData;
import mf.exception.bdd.InvalidName;

public class EstNationDAO extends DAO<EstNation> {

	public EstNationDAO(Connection conn) {
		super(conn);
	}

	public boolean create(EstNation est_nation) {
		String query = "INSERT INTO est_nation (nation, film) VALUES (?,?)";
		
		if (est_nation.getNation().getNation().isEmpty() || est_nation.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(est_nation.getNation().getNation()));
			prepare.setString(2, est_nation.getFilm().getTitreDisque());
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			return false;
		}	
	}


	public boolean delete(EstNation est_nation) {
		String query = "DELETE FROM est_nation WHERE nation = ? AND film = ?";
		
		if (est_nation.getNation().getNation().isEmpty() || est_nation.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(est_nation.getNation().getNation()));
			prepare.setString(2, est_nation.getFilm().getTitreDisque());
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}


	public boolean update(EstNation old_est_nation, EstNation new_est_nation) {
		String query ="UPDATE est_nation SET ";
		boolean has_previous = false;
		int num = 1;
		
		if (old_est_nation.getNation().getNation().isEmpty() || old_est_nation.getFilm().isEmpty() || new_est_nation.isEmpty())
			return false;
		
		try {
			if (!new_est_nation.getNation().getNation().isEmpty()) {
				query += " nation = ? ";
				has_previous = true;
			}
			if (!new_est_nation.getFilm().isEmpty()){
				if (has_previous)
					query += " , film = ? ";
				else
					query += " film = ? ";
			}
			query += " WHERE nation = ? AND film = ? ";
			
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			
			if (!new_est_nation.getNation().getNation().isEmpty()) {
				prepare.setString(num, CastToInput(new_est_nation.getNation().getNation()));
				num++;
			}
			if (!new_est_nation.getFilm().isEmpty()){
				prepare.setString(num, new_est_nation.getFilm().getTitreDisque());
				num++;
			}
			
			prepare.setString(num, CastToInput(old_est_nation.getNation().getNation()));
			num++;
			prepare.setString(num, old_est_nation.getFilm().getTitreDisque());
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}


	public ArrayList<EstNation> find(EstNation est_nation) {
		ArrayList<EstNation> liste_est_nation = new ArrayList<EstNation>();
		String query = "SELECT * FROM est_nation WHERE ";
		
		boolean has_previous = false;
		int num = 1;
		
		ResultSet result;
		PreparedStatement prepare;
		Statement state;
			
		try {	
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			if (est_nation.isEmpty()){
				query = "SELECT * FROM est_nation";
			}
			if (!est_nation.getNation().isEmpty()) {
				query += " nation = ? ";
				has_previous = true;
			}
			if (!est_nation.getFilm().isEmpty()) {
				if (has_previous)
					query += " AND film = ?";
				else 
					query += " film = ?";
			}
			
			query += " ORDER BY film";
				
			prepare = PsConnection.getInstance().prepareStatement(query);
				
			if (!est_nation.getNation().isEmpty()){
				prepare.setString(num, CastToInput(est_nation.getNation().getNation()));
				num++;
			}
			if (!est_nation.getFilm().isEmpty()) {
				prepare.setString(num, est_nation.getFilm().getTitreDisque());
				num++;
			}
			
			result = state.executeQuery(prepare.toString());
				
			while(result.next())
					liste_est_nation.add(new EstNation(new Film(result.getString(2)),new Nationalite(CastToOutput(result.getString(1)))));
					
			result.close();
			state.close();
					
			} catch (SQLException | InvalidName | InvalidData e){
				e.printStackTrace();
			}
		
			return liste_est_nation;
		}
	
}
