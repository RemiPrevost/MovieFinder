package mf.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mf.database.PsConnection;
import mf.database.pojo.Film;
import mf.exception.bdd.InvalidDate;

public class FilmDAO extends DAO<Film> {
	public FilmDAO(Connection conn){
		super(conn);
	}

	// Insère un film dans la table films et retourne TRUE ou FALSE en cas d'erreur
	public boolean create(Film film){
		String query = "INSERT INTO films (nom_disque_f, nom_f, date_f, seen_f, note, nom_affiche) VALUES (?,?,?,?,?,?)";
		
		if (film.getTitreDisque().isEmpty())
			return false;
		
		try {
			PreparedStatement prepare = PsConnection.getInstance().prepareStatement(query);
			prepare.setString(1, film.getTitreDisque());
			if (film.getTitre().toString().isEmpty())
				prepare.setString(2,null);
			else
				prepare.setString(2, CastToInput(film.getTitre().toString()));
			
			prepare.setInt(3, film.getDate());
			
			prepare.setBoolean(4, film.isSeen());
			prepare.setFloat(5, film.getNote());
			if (film.getAffiche().isEmpty())
				prepare.setString(6,null);
			else
				prepare.setString(6, film.getAffiche());
			
			
			Statement state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}

	// Retire un film de la table films et retourne TRUE ou FALSE en cas d'erreur
	public boolean delete(Film film) {
		String query = "DELETE FROM films ";
		
		boolean has_where = false;
		int num = 1;
		
		PreparedStatement prepare;
		Statement state;
			
		try {
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				
			if (!film.getTitreDisque().isEmpty()){
				has_where = true;
				query += " WHERE nom_disque_f = ?";
			}
			if (!film.getTitre().isEmpty()) {
				if (has_where)
					query += " AND nom_f = ?";
				else {
					query += " WHERE nom_f = ?";
					has_where = true; 
				}
			}
			if (film.getDate() != -1) {
				if (has_where)
					query += " AND date_f = ?";
				else {
					query += " WHERE date_f = ?";
					has_where = true; 
				}	
			}
			if (!film.getAffiche().toString().isEmpty()) {
				if (has_where)
					query += " AND nom_affiche = ?";
				else {
					query += " WHERE nom_affiche = ?";
					has_where = true; 
				}
			}
			if (film.getSeen() != -1){
				if (has_where)
					query += " AND seen_f = ?";
				else {
					query += " WHERE seen_f = ?";
					has_where = true; 
				}
			}
			if (film.getNote() != -1){
				if (has_where)
					query += " AND note = ?";
				else {
					query += " WHERE note = ?";
					has_where = true; 
				}
			}
			
			prepare = PsConnection.getInstance().prepareStatement(query);
			
			if (!film.getTitreDisque().isEmpty()){
				prepare.setString(num, film.getTitreDisque());
				num++;
			}
			if (!film.getTitre().isEmpty()) {
				prepare.setString(num, CastToInput(film.getTitre()));
				num++;
			}
			if (film.getDate() != -1) {
				prepare.setInt(num, film.getDate());
				num++;
			}
			if (!film.getAffiche().toString().isEmpty()) {
				prepare.setString(num, film.getAffiche().toString());
				num++;
			}
			if (film.getSeen() != -1){
				prepare.setBoolean(num, film.isSeen());
				num++;
			}
			if (film.getNote() != -1){
				prepare.setFloat(num, film.getNote());
				num++;
			}

			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}
	
	//Modifie les champs souhaités d'un ancien film par ceux d'un nouveau et renvoi TRUE ou FALSE si erreur
	public boolean update(Film old_film, Film new_film) {
		String query = "UPDATE films SET ";
		
		boolean has_where = false;
		boolean has_previous = false;
		int num = 1;
		
		PreparedStatement prepare;
		Statement state;
			
		try {
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			if (!new_film.getTitreDisque().isEmpty()){
				has_previous = true;
				query += " nom_disque_f = ?";	
			}
			if (!new_film.getTitre().isEmpty()) {
				if (!has_previous){
					query += " nom_f = ?";
					has_previous = true; 
				}
				else {
					query += " , nom_f = ?";
				}
			}
			if (new_film.getDate() != -1) {
				if (!has_previous){
					query += "  date_f = ?";
					has_previous = true; 
				}
				else {
					query += " , date_f = ?";
				}	
			}
			if (!new_film.getAffiche().toString().isEmpty()) {
				if (!has_previous) {
					query += " nom_affiche = ?";
					has_previous = true; 
				}
				else {
					query += " , nom_affiche = ?";
				}
			}
			if (new_film.getSeen() != -1){
				if (!has_previous){
					query += " seen_f = ?";
					has_previous = true; 
				}
				else {
					query += " , seen_f = ?";
				}
			}
			if (new_film.getNote() != -1){
				if (!has_previous){
					query += " note = ?";
					has_previous = true; 
				}
				else {
					query += " , note = ?";
				}
			}
			
			if (!old_film.getTitreDisque().isEmpty()){
				has_where = true;
				query += " WHERE nom_disque_f = ?";	
			}
			if (!old_film.getTitre().isEmpty()) {
				if (has_where)
					query += " AND nom_f = ?";
				else {
					query += " WHERE nom_f = ?";
					has_where = true; 
				}
			}
			if (old_film.getDate() != -1) {
				if (has_where)
					query += " AND date_f = ?";
				else {
					query += " WHERE date_f = ?";
					has_where = true; 
				}	
			}
			if (!old_film.getAffiche().toString().isEmpty()) {
				if (has_where)
					query += " AND nom_affiche = ?";
				else {
					query += " WHERE nom_affiche = ?";
					has_where = true; 
				}
			}
			if (old_film.getSeen() != -1){
				if (has_where)
					query += " AND seen_f = ?";
				else {
					query += " WHERE seen_f = ?";
					has_where = true; 
				}
			}
			
			if (old_film.getNote() != -1){
				if (has_where)
					query += " AND note = ?";
				else {
					query += " WHERE note = ?";
					has_where = true;
				}
			}
			
			prepare = PsConnection.getInstance().prepareStatement(query);
			
			if (!new_film.getTitreDisque().isEmpty()){
				prepare.setString(num, new_film.getTitreDisque());
				num++;
			}
			if (!new_film.getTitre().isEmpty()) {
				prepare.setString(num, CastToInput(new_film.getTitre()));
				num++;
			}
			if (new_film.getDate() != -1) {
				prepare.setInt(num, new_film.getDate());
				num++;
			}
			if (!new_film.getAffiche().toString().isEmpty()) {
				prepare.setString(num, new_film.getAffiche().toString());
				num++;
			}
			if (new_film.getSeen() != -1){
				prepare.setBoolean(num, new_film.isSeen());
				num++;
			}
			if (new_film.getNote() != -1){
				prepare.setFloat(num, new_film.getNote());
				num++;
			}
			if (!old_film.getTitreDisque().isEmpty()){
				prepare.setString(num, old_film.getTitreDisque());
				num++;
			}
			if (!old_film.getTitre().isEmpty()) {
				prepare.setString(num, CastToInput(old_film.getTitre()));
				num++;
			}
			if (old_film.getDate() != -1) {
				prepare.setInt(num, old_film.getDate());
				num++;
			}
			if (!old_film.getAffiche().toString().isEmpty()) {
				prepare.setString(num, old_film.getAffiche().toString());
				num++;
			}
			if (old_film.getSeen() != -1){
				prepare.setBoolean(num, old_film.isSeen());
				num++;
			}
			if (old_film.getNote() != -1){
				prepare.setFloat(num, new_film.getNote());
				num++;
			}
			state.executeUpdate(prepare.toString());
			
			return true;
		} catch (SQLException e){
			e.printStackTrace();
			return false;
		}	
	}


	// Retourne un ArrayList contenant tous les films correspondant à l'objet reçu
	public ArrayList<Film> find(Film film) {
		String query = "SELECT * FROM films ";
		ArrayList<Film> liste_film = new ArrayList<Film>();
		
		boolean has_where = false;
		int num = 1;
			
		ResultSet result;
		PreparedStatement prepare;
		Statement state;
		
		try {	
			state = PsConnection.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			if (!film.getTitreDisque().isEmpty()){
				has_where = true;
				query += " WHERE nom_disque_f = ?";	
			}
			if (!film.getTitre().isEmpty()) {
				if (has_where)
					query += " AND nom_f = ?";
				else {
					query += " WHERE nom_f = ?";
					has_where = true; 
				}
			}
			if (film.getDate() != -1) {
				if (has_where)
					query += " AND date_f = ?";
				else {
					query += " WHERE date_f = ?";
					has_where = true; 
				}	
			}
			if (!film.getAffiche().toString().isEmpty()) {
				if (has_where)
					query += " AND nom_affiche = ?";
				else {
					query += " WHERE nom_affiche = ?";
					has_where = true; 
				}
			}
			if (film.getSeen() != -1){
				if (has_where)
					query += " AND seen_f = ?";
				else {
					query += " WHERE seen_f = ?";
					has_where = true; 
				}
			}
			if (film.getNote() != -1){
				if (has_where)
					query += " AND note = ?";
				else {
					query += " WHERE note = ?";
					has_where = true; 
				}
			}
			
			query += " ORDER BY nom_disque_f";
			
			prepare = PsConnection.getInstance().prepareStatement(query);
			
			if (!film.getTitreDisque().isEmpty()){
				prepare.setString(num, film.getTitreDisque());
				num++;
			}
			if (!film.getTitre().isEmpty()) {
				prepare.setString(num, CastToInput(film.getTitre()));
				num++;
			}
			if (film.getDate() != -1) {
				prepare.setInt(num, film.getDate());
				num++;
			}
			if (!film.getAffiche().toString().isEmpty()) {
				prepare.setString(num, film.getAffiche().toString());
				num++;
			}
			if (film.getSeen() != -1){
				prepare.setBoolean(num, film.isSeen());
				num++;
			}
			if (film.getNote() != -1){
				prepare.setFloat(num, film.getNote());
				num++;
			}
			
			result = state.executeQuery(prepare.toString());
			
			while(result.next())
					liste_film.add(new Film(result.getString(3),CastToOutput(CastNullToString(result.getString(1))),result.getInt(4),CastBooleanToInt(result.getBoolean(2)),CastNullToString(result.getString(5)),result.getFloat(6)));
				
				result.close();
				state.close();

			} catch (SQLException | InvalidDate e){
				e.printStackTrace();
			}

			return liste_film;
		}
	
	// converti un boolean en int
	private int CastBooleanToInt(boolean a){
		if (a) return 1;
		else return 0;
	}
	
	//converti un objet null en string vide ""
	private String CastNullToString(String s){
		if (s == null) return "";
		else return s;
	}
	
}