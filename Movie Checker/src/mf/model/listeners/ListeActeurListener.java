package mf.model.listeners;

import java.util.EventListener;
import mf.model.events.ListeActeurEvent;

public interface ListeActeurListener extends EventListener {
	public void listeActeurChanged(ListeActeurEvent event);
}
