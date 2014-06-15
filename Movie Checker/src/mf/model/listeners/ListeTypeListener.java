package mf.model.listeners;

import java.util.EventListener;

import mf.model.events.ListeTypeEvent;

public interface ListeTypeListener extends EventListener {
	public void listeTypeChanged(ListeTypeEvent event);
}
