package mf.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import mf.database.dao.DAO;
import mf.database.dao.DAOFactory;
import mf.database.pojo.*;
import mf.exception.bdd.InvalidData;
import mf.exception.bdd.InvalidDate;
import mf.exception.bdd.InvalidName;
import mf.model.events.*;
import mf.model.listeners.*;
import mf.net.Stream;

public class Model{
	private EventListenerList listeners;
	
	// Pour Realisateur :
	private DAO<Realisateur> realisateurDAO = DAOFactory.getRealisateurDAO();
	private ArrayList<Realisateur> LR = new ArrayList<Realisateur>();
	
	// Pour Acteur : 
	private DAO<Acteur> acteurDAO = DAOFactory.getActeurDAO();
	private ArrayList<Acteur> LA = new ArrayList<Acteur>();
	
	// Pour Genre :
	private DAO<Type> genreDAO = DAOFactory.getTypesDAO();
	private ArrayList<Type> LG = new ArrayList<Type>();
	
	// Pour nationalite :
	private DAO<Nationalite> nationaliteDAO = DAOFactory.getNationaliteDAO();
	private ArrayList<Nationalite> LN = new ArrayList<Nationalite>();
	
	// Pour Film :
	private DAO<EstType> est_typeDAO = DAOFactory.getEst_TypeDAO();
	private DAO<EstNation> est_nationDAO = DAOFactory.getEst_NationDAO();
	private DAO<Film> filmDAO = DAOFactory.getFilmDAO();
	private DAO<Realiser> realiserDAO = DAOFactory.getRealiserDAO();
	private DAO<Jouer> jouerDAO = DAOFactory.getJouerDAO();
	private ArrayList<FicheFilm> LFF = new ArrayList<FicheFilm>();
	private IntervalleDate intervalle_date = new IntervalleDate();
	private Filtre filtre = new Filtre();
	
	//--------------------------//
	//------ CONSTRUCTEUR ------//
	//--------------------------//
	public Model() throws SQLException {
		
		
		LR = realisateurDAO.find(new Realisateur());
		LA = acteurDAO.find(new Acteur());
		LG = genreDAO.find(new Type());
		LN = nationaliteDAO.find(new Nationalite());
		setLFF("");
		
		listeners = new EventListenerList();
	}
	
	// Méthode pour Film
	
	public void setLFF(String titre_disque) {
		String titre_partiel = filtre.getTitre_partiel();
		String titre = "";
		int date = filtre.getDate_min();
		int note_min = filtre.getNote_min();
		int seen = filtre.getSeen();
		ArrayList<Realisateur> lr = filtre.getListe_realisateurs();
		ArrayList<Acteur> la = filtre.getListe_acteurs();
		ArrayList<Type> lg = filtre.getListe_genres();
		ArrayList<Nationalite> ln = filtre.getListe_nations();
			
		ArrayList<Realisateur> lrtemp =  new ArrayList<Realisateur>();
		ArrayList<Acteur> latemp = new ArrayList<Acteur>();
		ArrayList<Type> lgtemp = new ArrayList<Type>();
		ArrayList<Nationalite> lntemp = new ArrayList<Nationalite>();
		
		LFF.clear();
		try {
			ArrayList<Film> lf = filmDAO.find(new Film(titre_disque,titre,date,seen));
			
			for (Film f : lf) {
				if (lr != null && !lr.isEmpty())
					for (Realisateur r : lr) {
						if (!realiserDAO.find(new Realiser(r,f)).isEmpty())
							lrtemp.add(r);
					}
				else
					lrtemp = null;
				if (la != null && !la.isEmpty())
					for (Acteur a : la) {
						if (!jouerDAO.find(new Jouer(a,f)).isEmpty())
							latemp.add(a);
					}
				else
					latemp = null;
				if (lg != null && !lg.isEmpty())
					for (Type g : lg) {
						if (!est_typeDAO.find(new EstType(f,g)).isEmpty())
							lgtemp.add(g);
					}
				else
					lgtemp = null;
				if (ln != null && !ln.isEmpty())
					for (Nationalite n : ln) {
						if (!est_nationDAO.find(new EstNation(f,n)).isEmpty())
							lntemp.add(n);
					}
				else
					lntemp = null;
				
				if (!((lrtemp != null && lrtemp.isEmpty()) || (latemp != null && latemp.isEmpty()) || (lgtemp != null && lgtemp.isEmpty()) || (lntemp != null && lntemp.isEmpty()))) {
					if ((note_min == -1 || f.getNote() >= note_min) && f.getTitre().contains(titre_partiel) && (intervalle_date.isNull() || intervalle_date.isIn(f.getDate())))
						LFF.add(new FicheFilm(f,lrtemp,latemp,lgtemp,lntemp));
				}
				
				lrtemp = new ArrayList<Realisateur>();
				latemp = new ArrayList<Acteur>();
				lgtemp = new ArrayList<Type>();
				lntemp = new ArrayList<Nationalite>();
			}
			
			if (this.listeners != null)
				fireListeFicheFilmChanged();
			
		} catch (InvalidDate | InvalidData | InvalidName e) {
			e.printStackTrace();
		}
	}
	
	public void updateF(String titre_disque, boolean seen, FicheFilm fiche_film) {
		try {
			if (!fiche_film.getListe_realisateurs().isEmpty()) {
				ArrayList<Realiser> liste_realiser_temp = realiserDAO.find(new Realiser(new Film(titre_disque)));
				
				for (Realiser r : liste_realiser_temp)
					realiserDAO.delete(r);
			}
			
			if (!fiche_film.getListe_acteurs().isEmpty()) {
				ArrayList<Jouer> liste_jouer_temp = jouerDAO.find(new Jouer(new Film(titre_disque)));
				
				for (Jouer j : liste_jouer_temp)
					jouerDAO.delete(j); 
			}
			
			if (!fiche_film.getListe_nations().isEmpty()) {
				ArrayList<EstNation> liste_est_nation_temp = est_nationDAO.find(new EstNation(new Film(titre_disque)));
				
				for (EstNation n : liste_est_nation_temp)
					est_nationDAO.delete(n);
			}
			
			
			if (!fiche_film.getListe_genres().isEmpty()) {
				ArrayList<EstType> liste_est_type_temp = est_typeDAO.find(new EstType(new Film(titre_disque)));
				
				for (EstType g : liste_est_type_temp)
					est_typeDAO.delete(g);
			}
			
			filmDAO.update(new Film(titre_disque), new Film(titre_disque, fiche_film.getTitre_f(), fiche_film.getDate(),CastBooleanInt(seen),Stream.downloadImageToFolder(fiche_film.getAffiche_url()),fiche_film.getNote()));
			
			for (Realisateur r : fiche_film.getListe_realisateurs()) {
				realisateurDAO.create(r);
				realiserDAO.create(new Realiser(r, new Film(titre_disque)));
				findLR();
			}
			
			for (Acteur a : fiche_film.getListe_acteurs()) {
				acteurDAO.create(a);
				jouerDAO.create(new Jouer(a, new Film(titre_disque)));
				findLA();
			}
			
			for (Nationalite n : fiche_film.getListe_nations()) {
				nationaliteDAO.create(n);
				est_nationDAO.create(new EstNation(new Film(titre_disque), n));
				findLN();
			}
			
			for (Type g : fiche_film.getListe_genres()) {
				genreDAO.create(g);
				est_typeDAO.create(new EstType(new Film(titre_disque), g));
				findLG();
			}
			
		} catch (InvalidName | InvalidDate | InvalidData | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setLFF("");
	}
	
	public ArrayList<FicheFilm> getLFF() {
		return LFF;
	}
	
	public FicheFilm getFicheFilm(String titre_disque) {
		ArrayList<Realisateur> lr = new ArrayList<Realisateur>();
		ArrayList<Acteur> la = new ArrayList<Acteur>();
		ArrayList<Type> lg = new ArrayList<Type>();
		ArrayList<Nationalite> ln = new ArrayList<Nationalite>();
		
		try {
			ArrayList<Film> lf = filmDAO.find(new Film(titre_disque));
			
			Film film = lf.get(0);
			ArrayList<Realiser> lrealiser = realiserDAO.find(new Realiser(film));
			ArrayList<Jouer> ljouer = jouerDAO.find(new Jouer(film));
			ArrayList<EstType> lest_type = est_typeDAO.find(new EstType(film));
			ArrayList<EstNation> lest_nation = est_nationDAO.find(new EstNation(film));
			
			for (Realiser r : lrealiser)
				lr.add(r.getRealisateur());
			for (Jouer j : ljouer)
				la.add(j.getActeur());
			for (EstType g : lest_type)
				lg.add(g.getGenre());
			for (EstNation n : lest_nation)
				ln.add(n.getNation());
			
			return new FicheFilm(film,lr,la,lg,ln);
			
		} catch (InvalidName e) {
			e.printStackTrace();
			return new FicheFilm();
		}
	}
	
	public boolean editF(FicheFilmRed oldFFR, FicheFilmRed newFFR) {
		try {
			if (filmDAO.update(new Film(oldFFR.getTitre_disque()), new Film(oldFFR.getTitre_disque(),newFFR.getTitre_f(),newFFR.getDate(),CastBooleanInt(newFFR.isSeen()),new String(),newFFR.getNote()))) {
				setLFF("");
				return true;
			} 
			else return false;
		} catch (InvalidDate | InvalidName e) {
			return false;
		}
	}
	
	public void deleteF(String titre_disque) {
		try {
			filmDAO.delete(new Film(titre_disque));
		} catch (InvalidName e) {
			e.printStackTrace();
		}
		setLFF("");
	}
	
	public IntervalleDate getDateExtrem() {
		int date_max = 0;
		int date_min = 1000000;
		ArrayList<Film> lf = filmDAO.find(new Film());
		for (Film f : lf) {
			if (f.getDate() > date_max)
				date_max = f.getDate();
			if (f.getDate() < date_min)
				date_min = f.getDate();
		}
		if (date_max == 0 || date_min == 1000000)
			return new IntervalleDate();
		else
			return new IntervalleDate(date_min,date_max);
	}
	
	public void editFiltreIntervalleDate(IntervalleDate intervalle_date){
		this.intervalle_date = intervalle_date;
		setLFF("");
	}
	
	public void editFiltreCurseurNote(int note_min) {
		this.filtre.setNote_min(note_min);
		setLFF("");
	}
	
	public void editFiltreSeen(int seen) {
		this.filtre.setSeen(seen);
		setLFF("");
	}
	
	public void editFiltreTitre(String titre) {
		this.filtre.setTitre_partiel(titre);
		setLFF("");
	}
	
	public void addListeFicheFilmListener(ListeFicheFilmListener listener){
		listeners.add(ListeFicheFilmListener.class, listener);
	}
	
	public void removeListeFicheFilmListener(ListeFicheFilmListener listener) {
		listeners.remove(ListeFicheFilmListener.class, listener);
	}
	
	public void RaiseFireListeFicheFilmChanged() {
		fireListeFicheFilmChanged();
	}
	
	public void fireListeFicheFilmChanged() {
		ListeFicheFilmListener[] listenerList = (ListeFicheFilmListener[])listeners.getListeners(ListeFicheFilmListener.class);
		
		for (ListeFicheFilmListener listener : listenerList) {
			listener.listeFicheFilmChanged(new ListeFicheFilmEvent(this,this.getLFF()));
		}
	}
	
	public ArrayList<Acteur> getListeActeurFiltre(ArrayList<Acteur> la) {
		ArrayList<Integer> liste_index = new ArrayList<Integer>();
		ArrayList<Acteur> laf = acteurDAO.find(new Acteur());
		for (Acteur a_liste : laf) {
			for (Acteur a_selected : la) {
				if (a_liste.getNom().equals(a_selected.getNom()) && a_liste.getPrenom().equals(a_selected.getPrenom()))
					liste_index.add(laf.indexOf(a_liste));
					
			}
		}
		
		for (@SuppressWarnings("unused") Integer index : liste_index) {
			laf.remove(0);
		}
		
		laf.add(0,new Acteur());
		return laf;
	}
	
	public ArrayList<Realisateur> getListeRealisateurFiltre(ArrayList<Realisateur> lr) {
		ArrayList<Integer> liste_index = new ArrayList<Integer>();
		ArrayList<Realisateur> lrf = realisateurDAO.find(new Realisateur());
		for (Realisateur r_liste : lrf) {
			for (Realisateur r_selected : lr) {
				if (r_liste.getNom().equals(r_selected.getNom()) && r_liste.getPrenom().equals(r_selected.getPrenom()))
					liste_index.add(lrf.indexOf(r_liste));
					
			}
		}
		
		for (@SuppressWarnings("unused") Integer index : liste_index) {
			lrf.remove(0);
		}
		
		lrf.add(0,new Realisateur());
		return lrf;
	}
	
	public ArrayList<Type> getListeTypeFiltre(ArrayList<Type> lg) {
		ArrayList<Integer> liste_index = new ArrayList<Integer>();
		ArrayList<Type> lgf = genreDAO.find(new Type());
		for (Type g_liste : lgf) {
			for (Type g_selected : lg) {
				if (g_liste.getGenre().equals(g_selected.getGenre()))
					liste_index.add(lgf.indexOf(g_liste));
					
			}
		}
		
		for (@SuppressWarnings("unused") Integer index : liste_index) {
			lgf.remove(0);
		}
		
		lgf.add(0,new Type());
		return lgf;
	}
	
	public ArrayList<Nationalite> getListeNationaliteFiltre(ArrayList<Nationalite> ln) {
		ArrayList<Integer> liste_index = new ArrayList<Integer>();
		ArrayList<Nationalite> lnf = nationaliteDAO.find(new Nationalite());
		for (Nationalite n_liste : lnf) {
			for (Nationalite n_selected : ln) {
				if (n_liste.getNation().equals(n_selected.getNation()))
					liste_index.add(lnf.indexOf(n_liste));
					
			}
		}
		
		for (@SuppressWarnings("unused") Integer index : liste_index) {
			lnf.remove(0);
		}
		
		lnf.add(0,new Nationalite());
		return lnf;
	}
	
	public void setActeurSelected(Acteur A) {
		ArrayList<Acteur> la = new ArrayList<Acteur>();
		if (!A.isEmpty()) {
			la.add(A);
			filtre.setListe_acteurs(la);
		}
		else
			filtre.setListe_acteurs(new ArrayList<Acteur>());
			
		setLFF("");
	}
	
	public void setRealisateurSelected(Realisateur R) {
		ArrayList<Realisateur> lr = new ArrayList<Realisateur>();
		if (!R.isEmpty()) {
			lr.add(R);
			filtre.setListe_realisateurs(lr);
		}
		else
			filtre.setListe_realisateurs(new ArrayList<Realisateur>());
			
		setLFF("");
	}
	
	public void setTypeSelected(Type G) {
		ArrayList<Type> lg = new ArrayList<Type>();
		if (!G.isEmpty()) {
			lg.add(G);
			filtre.setListe_genres(lg);
		}
		else
			filtre.setListe_genres(new ArrayList<Type>());
			
		setLFF("");
	}
	
	public void setNationSelected(Nationalite N) {
		ArrayList<Nationalite> ln = new ArrayList<Nationalite>();
		if (!N.isEmpty()) {
			ln.add(N);
			filtre.setListe_nations(ln);
		}
		else
			filtre.setListe_nations(new ArrayList<Nationalite>());
			
		setLFF("");
	}
	
	private int CastBooleanInt(boolean seen) {
		if (seen) return 1;
		else return 0;
	}
	
	// Méthodes pour Realisateur : 
	public void RaiseFireListeRealisateurChanged() {
		fireListeRealisateurChanged();
	}
	
	public ArrayList<Realisateur> getLR() {
		return LR;
	}
	
	public void findLR() {
		LR = realisateurDAO.find(new Realisateur());
		fireListeRealisateurChanged();
	}
	
	public boolean addLR(String nom, String prenom) {
		if (realisateurDAO.create(new Realisateur(nom,prenom))){
			LR = realisateurDAO.find(new Realisateur());
			fireListeRealisateurChanged();
			return true;
		}
		else
			return false;
	}
	
	public void removeR(String nom, String prenom) {
		realisateurDAO.delete(new Realisateur(prenom,nom));
		LR = realisateurDAO.find(new Realisateur());
		fireListeRealisateurChanged();
	}
	
	public boolean editR(Realisateur oldR, Realisateur newR) {
		return realisateurDAO.update(oldR, newR);
	}
	
	public boolean isPresentRealisateur(Realisateur R) {
		return (!realisateurDAO.find(R).isEmpty());
	}
	
	public void addListeRealisateurListener(ListeRealisateurListener listener){
		listeners.add(ListeRealisateurListener.class, listener);
	}
	
	public void removeListeRealisateurListener(ListeRealisateurListener listener) {
		listeners.remove(ListeRealisateurListener.class, listener);
	}
	
	public void fireListeRealisateurChanged() {
		ListeRealisateurListener[] listenerList = (ListeRealisateurListener[])listeners.getListeners(ListeRealisateurListener.class);
		
		for (ListeRealisateurListener listener : listenerList) {
			listener.listeRealisateurChanged(new ListeRealisateurEvent(this,this.getLR()));
		}
	}
	
	// Méthodes pour Acteur :
	
	public void RaiseFireListeActeurChanged() {
		fireListeActeurChanged();
	}
	
	public ArrayList<Acteur> getLA() {
		return LA;
	}
	
	public void findLA() {
		LA = acteurDAO.find(new Acteur());
		fireListeActeurChanged();
	}
	
	public boolean addLA(String nom, String prenom) {
		try {
			if (acteurDAO.create(new Acteur(nom,prenom))){
				LA = acteurDAO.find(new Acteur());
				fireListeActeurChanged();
				return true;
			}
			else
				return false;
		} catch (InvalidName e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void removeA(String nom, String prenom) {
		try {
			acteurDAO.delete(new Acteur(prenom,nom));
			LA = acteurDAO.find(new Acteur());
			fireListeActeurChanged();
		} catch (InvalidName e) {
			e.printStackTrace();
		}
	}
	
	public boolean editA(Acteur oldA, Acteur newA) {
		return acteurDAO.update(oldA, newA);
	}
	
	public void addListeActeurListener(ListeActeurListener listener){
		listeners.add(ListeActeurListener.class, listener);
	}
	
	public boolean isPresentActeur(Acteur A) {
		return (!acteurDAO.find(A).isEmpty());
	}
	
	public void removeActeurListener(ListeActeurListener listener) {
		listeners.remove(ListeActeurListener.class, listener);
	}
	
	public void fireListeActeurChanged() {
		ListeActeurListener[] listenerList = (ListeActeurListener[])listeners.getListeners(ListeActeurListener.class);
		
		for (ListeActeurListener listener : listenerList) {
			listener.listeActeurChanged(new ListeActeurEvent(this,this.getLA()));
		}
	}
	
	// Pour Genre
	
	public void RaiseFireListeGenreChanged() {
		fireListeTypeChanged();
	}
	
	public ArrayList<Type> getLG() {
		return LG;
	}
	
	public void findLG() {
		LG = genreDAO.find(new Type());
		fireListeTypeChanged();
	}
	
	public boolean addLG(String nom) {
		if (genreDAO.create(new Type(nom))){
			LG = genreDAO.find(new Type());
			fireListeTypeChanged();
			return true;
		}
		else
			return false;
	}
	
	public void removeG(String nom) {
		genreDAO.delete(new Type(nom));
		LG = genreDAO.find(new Type());
		fireListeTypeChanged();
	}
	
	public boolean editG(Type oldG, Type newG) {
		return genreDAO.update(oldG, newG);
	}
	
	public boolean isPresentType(Type G) {
		return (!genreDAO.find(G).isEmpty());
	}
	
	public void addListeTypeListener(ListeTypeListener listener){
		listeners.add(ListeTypeListener.class, listener);
	}
	
	public void removeListeTypeListener(ListeTypeListener listener) {
		listeners.remove(ListeTypeListener.class, listener);
	}
	
	public void fireListeTypeChanged() {
		ListeTypeListener[] listenerList = (ListeTypeListener[])listeners.getListeners(ListeTypeListener.class);
		
		for (ListeTypeListener listener : listenerList) {
			listener.listeTypeChanged(new ListeTypeEvent(this,this.getLG()));
		}
	}
	
	// Pour nationalite
	
	public void RaiseFireListeNationaliteChanged() {
		fireListeNationaliteChanged();
	}
	
	public ArrayList<Nationalite> getLN() {
		return LN;
	}
	
	public void findLN() {
		LN = nationaliteDAO.find(new Nationalite());
		fireListeNationaliteChanged();
	}
	
	public boolean addLN(String nom) {
		if (nationaliteDAO.create(new Nationalite(nom))){
			LN = nationaliteDAO.find(new Nationalite());
			fireListeNationaliteChanged();
			return true;
		}
		else
			return false;
	}
	
	public void removeN(String nom) {
		nationaliteDAO.delete(new Nationalite(nom));
		LN = nationaliteDAO.find(new Nationalite());
		fireListeNationaliteChanged();
	}
	
	public boolean editN(Nationalite oldN, Nationalite newN) {
		return nationaliteDAO.update(oldN, newN);
	}
	
	public boolean isPresentNationalite(Nationalite N) {
		return (!nationaliteDAO.find(N).isEmpty());
	}
	
	public void addListeNationaliteListener(ListeNationaliteListener listener){
		listeners.add(ListeNationaliteListener.class, listener);
	}
	
	public void removeListeNationaliteListener(ListeNationaliteListener listener) {
		listeners.remove(ListeNationaliteListener.class, listener);
	}
	
	public void fireListeNationaliteChanged() {
		ListeNationaliteListener[] listenerList = (ListeNationaliteListener[])listeners.getListeners(ListeNationaliteListener.class);
		
		for (ListeNationaliteListener listener : listenerList) {
			listener.listeNationaliteChanged(new ListeNationaliteEvent(this,this.getLN()));
		}
	}
}
