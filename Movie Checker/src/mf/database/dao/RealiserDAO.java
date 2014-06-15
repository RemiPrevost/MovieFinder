package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.Film;
import mf.database.pojo.Realisateur;
import mf.database.pojo.Realiser;
import mf.exception.bdd.InvalidName;


public class RealiserDAO extends DAO<Realiser> {

	public RealiserDAO(Connection conn) {
		super(conn);
	}
	
	// ajoute à la table realiser l'objet realiser
	public boolean create(Realiser realiser) {
		String query = "INSERT INTO realiser (nom_r, prenom_r, titre_disque_f) VALUES (?,?,?)";
		
		if (realiser.getRealisateur().isEmpty() || realiser.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(realiser.getRealisateur().getNom()));
			prepare.setString(2, CastToInput(realiser.getRealisateur().getPrenom()));
			prepare.setString(3, realiser.getFilm().getTitreDisque());
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			return false;
		}	
	}

	//supprime l'élément répondant aux critère du paramètre realiser
	public boolean delete(Realiser realiser) {
		String query = "DELETE FROM realiser WHERE nom_r = ? AND prenom_r = ? AND titre_disque_f = ?";
		
		if (realiser.getRealisateur().isEmpty() || realiser.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(realiser.getRealisateur().getNom()));
			prepare.setString(2, CastToInput(realiser.getRealisateur().getPrenom()));
			prepare.setString(3, realiser.getFilm().getTitreDisque());
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}

	// modifie les champs souhaités correspondant aux critères de old_realiser pour les remplacer par ceux de new_realiser
	public boolean update(Realiser old_realiser, Realiser new_realiser) {
		String query ="UPDATE realiser SET "; ;
		boolean has_previous = false;
		int num = 1;
		
		if (old_realiser.getRealisateur().isEmpty() || old_realiser.getFilm().isEmpty() || new_realiser.isEmpty())
			return false;
		
		try {
			if (!new_realiser.getRealisateur().isEmpty()) {
				query += " nom_r = ? , prenom_r = ? ";
				has_previous = true;
			}
			if (!new_realiser.getFilm().isEmpty()){
				if (has_previous)
					query += " , titre_disque_f = ? ";
				else
					query += " titre_disque_f = ? ";
			}
			query += " WHERE nom_r = ? AND prenom_r = ? AND titre_disque_f = ?";
			
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			
			if (!new_realiser.getRealisateur().isEmpty()) {
				prepare.setString(num, CastToInput(new_realiser.getRealisateur().getNom()));
				num++;
				prepare.setString(num, CastToInput(new_realiser.getRealisateur().getPrenom()));
				num++;
			}
			if (!new_realiser.getFilm().isEmpty()){
				prepare.setString(num, new_realiser.getFilm().getTitreDisque());
				num++;
			}
			
			prepare.setString(num, CastToInput(old_realiser.getRealisateur().getNom()));
			num++;
			prepare.setString(num, CastToInput(old_realiser.getRealisateur().getPrenom()));
			num++;
			prepare.setString(num, CastToInput(old_realiser.getFilm().getTitreDisque()));
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
		
	}

	// retourne une liste de realiser correspondant au realiser passé en paramètre
	public ArrayList<Realiser> find(Realiser realiser) {
		ArrayList<Realiser> liste_jouer = new ArrayList<Realiser>();
		String query = "SELECT * FROM realiser WHERE ";
		
		boolean has_jouer = false;
		int num = 1;
			
		ResultSet result;
		PreparedStatement prepare;
		Statement state;
			
		try {	
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			if (realiser.isEmpty()){
				query = "SELECT * FROM realiser";
			}
			if (!realiser.getRealisateur().isEmpty()) {
				query += " nom_r = ? AND prenom_r = ?";
				has_jouer = true;
			}
			if (!realiser.getFilm().isEmpty()) {
				if (has_jouer)
					query += " AND titre_disque_f = ?";
				else 
					query += " titre_disque_f = ?";
			}
			
			query += " ORDER BY prenom_r";
				
			prepare = PsConnection.getInstance().prepareStatement(query);
				
			if (!realiser.getRealisateur().isEmpty()){
				prepare.setString(num, CastToInput(realiser.getRealisateur().getNom()));
				num++;
				prepare.setString(num, CastToInput(realiser.getRealisateur().getPrenom()));
				num++;
			}
			if (!realiser.getFilm().isEmpty()) {
				prepare.setString(num, realiser.getFilm().getTitreDisque());
				num++;
			}
			
			result = state.executeQuery(prepare.toString());
				
			while(result.next())
					liste_jouer.add(new Realiser(new Realisateur(CastToOutput(result.getString(1)),CastToOutput(result.getString(2))),new Film(result.getString(3))));
					
			result.close();
			state.close();
					
			} catch (SQLException | InvalidName e){
				e.printStackTrace();
			}
		
			return liste_jouer;
		}
}
