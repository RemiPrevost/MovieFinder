package mf.tests;

import java.util.ArrayList;

import mf.database.dao.DAO;
import mf.database.dao.DAOFactory;
import mf.database.pojo.*;
import mf.exception.bdd.InvalidData;
import mf.exception.bdd.InvalidDate;
import mf.exception.bdd.InvalidName;


public class test_database {

	public static void main(String[] args) {
		TestCastToOutput();
	}
	
	private static void TestCastToOutput() {
		//System.out.println("before cast : "+ "||" +"hello     world tea     (1)" + "||" + "\nafter cast :" + "||" + DAO.CastToOutput("hello  world tea (1)") + "||");
	}
	
	@SuppressWarnings("unused")
	private static void test_nationaliteDAO() {
		DAO<Nationalite> nationaliteDAO = DAOFactory.getNationaliteDAO();
		ArrayList<Nationalite> liste_nationalite;
		
		//try {
			//System.out.println(nationaliteDAO.create(new Nationalite("Italien")));
			System.out.println(nationaliteDAO.delete(new Nationalite("Italien")));
			//System.out.println(nationaliteDAO.update(new Nationalite("Américan"), new Nationalite("Américain")));
			
			liste_nationalite = nationaliteDAO.find(new Nationalite());
			
			if (liste_nationalite.isEmpty())
				System.out.println("Liste vide");

			for (Nationalite n : liste_nationalite)
				System.out.println(n.toString());
			
		/*} catch (InvalidName e) {
			e.printStackTrace(); 
		}*/
	}
	
	@SuppressWarnings("unused")
	private static void test_est_typeDAO() {
		DAO<EstType> est_typeDAO = DAOFactory.getEst_TypeDAO();
		ArrayList<EstType> liste_est_type;
		
		try {
			//System.out.println(est_typeDAO.create(new Est_Type(new Film("test5"), new Types("Science Fiction"))));
			//System.out.println(est_typeDAO.delete(new Est_Type(new Film("test5"), new Types("Science Fiction"))));
			System.out.println(est_typeDAO.update(new EstType(new Film("test3"), new Type("Aventure")),new EstType(new Film("test5"), new Type())));
			
			liste_est_type = est_typeDAO.find(new EstType());
			
			if (liste_est_type.isEmpty())
				System.out.println("Liste vide");

			for (EstType r : liste_est_type)
				System.out.println(r.toString());
			
		} catch (InvalidName e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void test_realiserDAO() {
		DAO<Realiser> realiserDAO = DAOFactory.getRealiserDAO();
		ArrayList<Realiser> liste_realiser;
		
		try {
			System.out.println(realiserDAO.create(new Realiser(new Realisateur("Scorcese","Martin"),new Film("chemin_test4"))));
			
			liste_realiser = realiserDAO.find(new Realiser(new Realisateur(),new Film()));
			
			if (liste_realiser.isEmpty())
				System.out.println("Liste vide");
		
			for (Realiser r : liste_realiser)
				System.out.println(r.toString());
			
		} catch (InvalidName e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void test_jouerDAO() {
		DAO<Jouer> jouerDAO = DAOFactory.getJouerDAO();
		ArrayList<Jouer> liste_jouer;
		
		try {
			System.out.println(jouerDAO.update(new Jouer(new Acteur("Chastain","Jessica"),new Film("chemin_test4")),new Jouer(new Acteur(),new Film("test"))));		
			
			liste_jouer = jouerDAO.find(new Jouer(new Acteur(),new Film()));
		
			if (liste_jouer.isEmpty())
				System.out.println("Liste vide");
		
			for (Jouer j : liste_jouer)
				System.out.println(j.toString());
			
		} catch (InvalidName e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void test_filmDAO (){
		DAO<Film> filmDAO = DAOFactory.getFilmDAO();
		ArrayList<Film> liste_film;
		
		
		try {
			//System.out.println(filmDAO.create(new Film("chemin_test6","",-1,-1,"",-1)));
			//System.out.println(filmDAO.update(new Film("","VB",-1,-1,"",-1), new Film("","",-1,-1,"",6)));
			System.out.println(filmDAO.delete(new Film("","",-1,1,"",-1)));
			
			liste_film = filmDAO.find(new Film());
			
			if (liste_film.isEmpty())
				System.out.println("Liste vide");
			for (Film f : liste_film)
				System.out.println(f.toString());
			
		} catch ( InvalidDate e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unused")
	private static void test_acteurDAO(){
		DAO<Acteur> acteurDAO = DAOFactory.getActeurDAO();
		ArrayList<Acteur> liste_acteur;
		try {
			//System.out.println(typesDAO.create(new Types("Comédie")));
			System.out.println(acteurDAO.delete(new Acteur("Waltz","Christoph")));
			System.out.println(acteurDAO.update(new Acteur("Chastain","Jessica"),new Acteur("Chast","Jess")));
		
			liste_acteur = acteurDAO.find(new Acteur());
			
			if (liste_acteur.isEmpty())
				System.out.println("Liste vide");
			else
				for (Acteur r : liste_acteur)
					System.out.println(r.toString());
			
		} catch (InvalidName e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unused")
	private static void test_est_nationDAO(){
		DAO<EstNation> est_nationDAO = DAOFactory.getEst_NationDAO();
		ArrayList<EstNation> liste_nation;
		
		try {
			System.out.println(est_nationDAO.delete(new EstNation(new Film("testY"),new Nationalite("Anglais"))));
		} catch (InvalidData | InvalidName e) {
			e.printStackTrace();
		}
		try {
			System.out.println(est_nationDAO.update(new EstNation(new Film("testY"),new Nationalite("Américain")),new EstNation(new Film("test3"),new Nationalite())));
		} catch (InvalidData | InvalidName e) {
			e.printStackTrace();
		}

		liste_nation = est_nationDAO.find(new EstNation());
		
		if (liste_nation.isEmpty())
			System.out.println("Liste vide");
		else
			for (EstNation n : liste_nation)
				System.out.println(n.toString());
	}
}
