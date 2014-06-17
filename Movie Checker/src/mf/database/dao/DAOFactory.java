package mf.database.dao;

import java.sql.Connection;
import java.sql.SQLException;

import mf.database.PsConnection;
import mf.database.pojo.*;

public class DAOFactory {
	protected static Connection conn;

	public static DAO<Film> getFilmDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new FilmDAO(conn);
	}
	
	public static DAO<Acteur> getActeurDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new ActeurDAO(conn);
	}
	
	public static DAO<Realisateur> getRealisateurDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new RealisateurDAO(conn);
	}
	
	public static DAO<Realiser> getRealiserDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new RealiserDAO(conn);
	}
	
	public static DAO<Jouer> getJouerDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new JouerDAO(conn);
	}
	
	public static DAO<Type> getTypesDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new TypeDAO(conn);
	}
	
	public static DAO<EstType> getEst_TypeDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new EstTypeDAO(conn);
	}
	
	public static DAO<Nationalite> getNationaliteDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new NationaliteDAO(conn);
	}
	
	public static DAO<EstNation> getEst_NationDAO() throws SQLException {
		conn = PsConnection.getInstance();
		return new EstNationDAO(conn);
	}
}
