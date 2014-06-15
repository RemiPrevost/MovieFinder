package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.Acteur;
import mf.exception.bdd.InvalidName;

public class ActeurDAO extends DAO<Acteur> {
	public ActeurDAO(Connection conn) {
		super(conn);
	}
	
	// Insère un acteur dans la table acteurs et retourne TRUE ou FALSE en cas d'erreur
	public boolean create(Acteur acteur){
		String query = "INSERT INTO acteurs (nom_a, prenom_a) VALUES (?,?)";
		
		if (acteur.getNom().isEmpty() || acteur.getPrenom().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(acteur.getNom()));
			prepare.setString(2, CastToInput(acteur.getPrenom()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			return false;
		}	
		
	}

	// Retire un acteur de la table acteurs et retourne TRUE ou FALSE en cas d'erreur
	public boolean delete(Acteur acteur) {
		String query = "DELETE FROM acteurs WHERE nom_a = ? AND prenom_a = ?";
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(acteur.getNom()));
			prepare.setString(2, CastToInput(acteur.getPrenom()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
		
	}
	
	//Modifie les nom et le prenom d'un acteur et renvoi TRUE ou FALSE si erreur
	public boolean update(Acteur old_acteur, Acteur new_acteur) {
		String query ="UPDATE acteurs SET nom_a = ? , prenom_a = ? WHERE nom_a = ? AND prenom_a = ?";
		
		if (new_acteur.getNom().isEmpty() || new_acteur.getPrenom().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(new_acteur.getNom()));
			prepare.setString(2, CastToInput(new_acteur.getPrenom()));
			prepare.setString(3, CastToInput(old_acteur.getNom()));
			prepare.setString(4, CastToInput(old_acteur.getPrenom()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
		
	}

	// Retourne un ArrayList contenant tous les acteurs portant le nom "nom"
	public ArrayList<Acteur> find(Acteur acteur) {
			String query = new String();
			ArrayList<Acteur> liste_acteur = new ArrayList<Acteur>();
			ResultSet result;
			PreparedStatement prepare;
			Statement state;
			
			try {	
				if (acteur.getNom().isEmpty()) {
					if(acteur.getPrenom().isEmpty()){
						query = "SELECT * FROM acteurs ORDER BY prenom_a";
						prepare = PsConnection.getInstance().prepareStatement(query);
						
					}
					else {
						query = "SELECT * FROM acteurs WHERE prenom_a = ? ORDER BY prenom_a";
						prepare = PsConnection.getInstance().prepareStatement(query);
						prepare.setString(1, CastToInput(acteur.getPrenom()));
					}
				} 
				else {
					if(acteur.getPrenom().isEmpty()) {
						query = "SELECT * FROM acteurs WHERE nom_a = ? ORDER BY prenom_a";
						prepare = PsConnection.getInstance().prepareStatement(query);
						prepare.setString(1, CastToInput(acteur.getNom()));
					}
					else {
						query = "SELECT * FROM acteurs WHERE prenom_a = ? AND nom_a = ?";
						prepare = PsConnection.getInstance().prepareStatement(query);
						prepare.setString(1, CastToInput(acteur.getPrenom()));
						prepare.setString(2, CastToInput(acteur.getNom()));
					}
				}
				
				
				state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				result = state.executeQuery(prepare.toString());
				
				while(result.next())
						liste_acteur.add(new Acteur(CastToOutput(result.getObject(1).toString()),CastToOutput(result.getObject(2).toString())));
				
				result.close();
				state.close();

			} catch (SQLException | InvalidName e){
				e.printStackTrace();
			}
			
			return liste_acteur;
		}
	
}