package mf.database.dao;

import java.sql.Connection;

import mf.database.PsConnection;
import mf.database.pojo.*;

public class DAOFactory {
	protected static final Connection conn = PsConnection.getInstance();

	public static DAO<Film> getFilmDAO(){
		return new FilmDAO(conn);
	}
	
	public static DAO<Acteur> getActeurDAO(){
		return new ActeurDAO(conn);
	}
	
	public static DAO<Realisateur> getRealisateurDAO(){
		return new RealisateurDAO(conn);
	}
	
	public static DAO<Realiser> getRealiserDAO(){
		return new RealiserDAO(conn);
	}
	
	public static DAO<Jouer> getJouerDAO(){
		return new JouerDAO(conn);
	}
	
	public static DAO<Type> getTypesDAO(){
		return new TypeDAO(conn);
	}
	
	public static DAO<EstType> getEst_TypeDAO(){
		return new EstTypeDAO(conn);
	}
	
	public static DAO<Nationalite> getNationaliteDAO(){
		return new NationaliteDAO(conn);
	}
	
	public static DAO<EstNation> getEst_NationDAO(){
		return new EstNationDAO(conn);
	}
}
