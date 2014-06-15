package mf.model;

import java.util.ArrayList;
import java.util.Calendar;

public class IntervalleDate {
	private int date_down = -1;
	private int date_up = -1;
	
	public IntervalleDate() {}
	
	public IntervalleDate(int date_down, int date_up) {
		this.date_down = date_down;
		this.date_up = date_up;
	}

	public int getDate1() {
		return date_down;
	}

	public int getDate2() {
		return date_up;
	}
	
	public boolean isIn(int date) {
		return (date <= date_up && date >= date_down);
	}
	
	public String toString() {
		if (this.isNull())
			return ("");
		else
			return (date_down + " - " + date_up);
	}
	
	public static ArrayList<IntervalleDate> createListIntervalleDate(int date_debut, int date_fin) {
		ArrayList<IntervalleDate> list_date = new ArrayList<IntervalleDate>();
		
		// in case of a date_debut superior of date_fin
		if (date_debut > date_fin) {
			int temp = date_fin;
			date_fin = date_debut;
			date_debut = temp;
		}
		
		// truncate date_debut
		date_debut = (int)((float)date_debut * 0.1)*10;
		
		// add the empty interval for the comboBox
		list_date.add(new IntervalleDate());
		
		// add the intervalleDate needed until the last date
		while (date_debut <= date_fin) {
			list_date.add(new IntervalleDate(date_debut,date_debut+19));
			date_debut += 20;
		}
		
		//if the last day is over he current year, we change it
		if (list_date.get(list_date.size()-1).getDate2() > Calendar.getInstance().get(Calendar.YEAR)) {
			list_date.remove(list_date.size()-1);
			list_date.add(new IntervalleDate(list_date.get(list_date.size()-1).getDate1(),Calendar.getInstance().get(Calendar.YEAR)));
		}
		
		return list_date;
	}
	
	public boolean isNull() {
		return (date_down == -1 || date_up == -1);
	}
	
}
