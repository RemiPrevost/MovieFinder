package mf.model.listeners;

import java.util.EventListener;
import mf.model.events.ListeRealisateurEvent;

public interface ListeRealisateurListener extends EventListener {
	public void listeRealisateurChanged(ListeRealisateurEvent event);
}
