package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.Realisateur;

public class RealisateurDAO extends DAO<Realisateur> {

	public RealisateurDAO(Connection conn) {
		super(conn);
	}
	
	// Insère un realisateur dans la table realisateurs et retourne TRUE ou FALSE en cas d'erreur
	public boolean create(Realisateur realisateur) {
		String query = "INSERT INTO realisateurs (nom_r, prenom_r) VALUES (?,?)";
		
		if (realisateur.getNom().isEmpty() || realisateur.getPrenom().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(realisateur.getNom()));
			prepare.setString(2, CastToInput(realisateur.getPrenom()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			return false;
		}	
		
	}

	// Retire un realisateur de la table realisateurs et retourne TRUE ou FALSE en cas d'erreur
	public boolean delete(Realisateur realisateur) {
		String query = "DELETE FROM realisateurs WHERE nom_r = ? AND prenom_r = ?";
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(realisateur.getNom()));
			prepare.setString(2, CastToInput(realisateur.getPrenom()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
		
	}
	
	//Modifie les nom et le prenom d'un realisateur et renvoi TRUE ou FALSE si erreur
	public boolean update(Realisateur old_realisateur, Realisateur new_realisateur) {
		String query ="UPDATE realisateurs SET nom_r = ? , prenom_r = ? WHERE nom_r = ? AND prenom_r = ?";
		
		if (new_realisateur.getNom().isEmpty() || new_realisateur.getPrenom().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, CastToInput(new_realisateur.getNom()));
			prepare.setString(2, CastToInput(new_realisateur.getPrenom()));
			prepare.setString(3, CastToInput(old_realisateur.getNom()));
			prepare.setString(4, CastToInput(old_realisateur.getPrenom()));
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
		
	}

	// Retourne un ArrayList contenant tous les realisateurs portant le nom "nom"
	public ArrayList<Realisateur> find(Realisateur realisateur) {
			String query = new String();
			ArrayList<Realisateur> liste_realisateur = new ArrayList<Realisateur>();
			ResultSet result;
			PreparedStatement prepare;
			Statement state;
			
			try {	
				if (realisateur.getNom().isEmpty()) {
					if(realisateur.getPrenom().isEmpty()){
						query = "SELECT * FROM realisateurs ORDER BY prenom_r";
						prepare = PsConnection.getInstance().prepareStatement(query);
						
					}
					else {
						query = "SELECT * FROM realisateurs WHERE prenom_r = ? ORDER BY prenom_r";
						prepare = PsConnection.getInstance().prepareStatement(query);
						prepare.setString(1, CastToInput(realisateur.getPrenom()));
					}
				} 
				else {
					if(realisateur.getPrenom().isEmpty()) {
						query = "SELECT * FROM realisateurs WHERE nom_r = ? ORDER BY prenom_r";
						prepare = PsConnection.getInstance().prepareStatement(query);
						prepare.setString(1, CastToInput(realisateur.getNom()));
					}
					else {
						query = "SELECT * FROM realisateurs WHERE prenom_r = ? AND nom_r = ?";
						prepare = PsConnection.getInstance().prepareStatement(query);
						prepare.setString(1, CastToInput(realisateur.getPrenom()));
						prepare.setString(2, CastToInput(realisateur.getNom()));
					}
				}
				
				
				state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				result = state.executeQuery(prepare.toString());
				
				while(result.next())
						liste_realisateur.add(new Realisateur(CastToOutput(result.getObject(1).toString()),CastToOutput(result.getObject(2).toString())));
				
				result.close();
				state.close();

			} catch (SQLException e){
				e.printStackTrace();
			}
			
			return liste_realisateur;
		}
	
}
