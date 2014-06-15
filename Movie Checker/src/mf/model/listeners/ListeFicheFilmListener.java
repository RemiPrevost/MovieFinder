package mf.model.listeners;

import java.util.EventListener;

import mf.model.events.ListeFicheFilmEvent;

public interface ListeFicheFilmListener extends EventListener {
	public void listeFicheFilmChanged(ListeFicheFilmEvent event);
}
