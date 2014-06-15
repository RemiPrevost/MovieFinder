package mf.model.listeners;

import java.util.EventListener;

import mf.model.events.ListeNationaliteEvent;

public interface ListeNationaliteListener extends EventListener {
	public void listeNationaliteChanged(ListeNationaliteEvent event);
}
