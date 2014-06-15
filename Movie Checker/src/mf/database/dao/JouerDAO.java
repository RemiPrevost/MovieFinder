package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.Acteur;
import mf.database.pojo.Film;
import mf.database.pojo.Jouer;
import mf.exception.bdd.InvalidName;


public class JouerDAO extends DAO<Jouer> {

	public JouerDAO(Connection conn) {
		super(conn);
	}

	// ajoute à la table jouer l'objet jouer
	public boolean create(Jouer jouer) {
		String query = "INSERT INTO jouer (nom_a, prenom_a, titre_disque_f) VALUES (?,?,?)";
		
		if (jouer.getActeur().isEmpty() || jouer.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(jouer.getActeur().getNom()));
			prepare.setString(2, CastToInput(jouer.getActeur().getPrenom()));
			prepare.setString(3, jouer.getFilm().getTitreDisque());
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			return false;
		}	
	}
	
	//supprime l'élément répondant aux critère du paramètre jouer
	public boolean delete(Jouer jouer) {
		String query = "DELETE FROM jouer WHERE nom_a = ? AND prenom_a = ? AND titre_disque_f = ?";
		
		if (jouer.getActeur().isEmpty() || jouer.getFilm().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(jouer.getActeur().getNom()));
			prepare.setString(2, CastToInput(jouer.getActeur().getPrenom()));
			prepare.setString(3, jouer.getFilm().getTitreDisque());
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
		
	}

	// modifie les champs souhaités correspondant aux critères de old_jouer pour les remplacer par ceux de new_jouer
	public boolean update(Jouer old_jouer, Jouer new_jouer) {
		String query ="UPDATE jouer SET "; ;
		boolean has_previous = false;
		int num = 1;
		
		if (old_jouer.getActeur().isEmpty() || old_jouer.getFilm().isEmpty() || new_jouer.isEmpty())
			return false;
		
		try {
			if (!new_jouer.getActeur().isEmpty()) {
				query += " nom_a = ? , prenom_a = ? ";
				has_previous = true;
			}
			if (!new_jouer.getFilm().isEmpty()){
				if (has_previous)
					query += " , titre_disque_f = ? ";
				else
					query += " titre_disque_f = ? ";
			}
			query += " WHERE nom_a = ? AND prenom_a = ? AND titre_disque_f = ?";
			
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			
			if (!new_jouer.getActeur().isEmpty()) {
				prepare.setString(num, CastToInput(new_jouer.getActeur().getNom()));
				num++;
				prepare.setString(num, CastToInput(new_jouer.getActeur().getPrenom()));
				num++;
			}
			if (!new_jouer.getFilm().isEmpty()){
				prepare.setString(num, new_jouer.getFilm().getTitreDisque());
				num++;
			}
			
			prepare.setString(num, CastToInput(old_jouer.getActeur().getNom()));
			num++;
			prepare.setString(num, CastToInput(old_jouer.getActeur().getPrenom()));
			num++;
			prepare.setString(num, old_jouer.getFilm().getTitreDisque());
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}

	// retourne une liste de jouer correspondant au jouer passé en paramètre
	public ArrayList<Jouer> find(Jouer jouer) {
		ArrayList<Jouer> liste_jouer = new ArrayList<Jouer>();
		String query = "SELECT * FROM jouer WHERE ";
		
		boolean has_jouer = false;
		int num = 1;
			
		ResultSet result;
		PreparedStatement prepare;
		Statement state;
			
		try {	
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			if (jouer.isEmpty()){
				query = "SELECT * FROM jouer";
			}
			if (!jouer.getActeur().isEmpty()) {
				query += " nom_a = ? AND prenom_a = ?";
				has_jouer = true;
			}
			if (!jouer.getFilm().isEmpty()) {
				if (has_jouer)
					query += " AND titre_disque_f = ?";
				else 
					query += " titre_disque_f = ?";
			}
			
			query += " ORDER BY prenom_a";
				
			prepare = PsConnection.getInstance().prepareStatement(query);
				
			if (!jouer.getActeur().isEmpty()){
				prepare.setString(num, CastToInput(jouer.getActeur().getNom()));
				num++;
				prepare.setString(num, CastToInput(jouer.getActeur().getPrenom()));
				num++;
			}
			if (!jouer.getFilm().isEmpty()) {
				prepare.setString(num, jouer.getFilm().getTitreDisque());
				num++;
			}
			
			result = state.executeQuery(prepare.toString());
				
			while(result.next())
					liste_jouer.add(new Jouer(new Acteur(CastToOutput(result.getString(1)),CastToOutput(result.getString(2))),new Film(result.getString(3))));
					
			result.close();
			state.close();
					
			} catch (SQLException | InvalidName e){
				e.printStackTrace();
			}
		
			return liste_jouer;
		}
	
}