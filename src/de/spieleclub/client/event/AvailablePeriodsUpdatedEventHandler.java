package de.spieleclub.client.event;

import com.google.gwt.event.shared.EventHandler;


public interface AvailablePeriodsUpdatedEventHandler extends EventHandler {
  public void onAvailablePeriodsChanged(AvailablePeriodsUpdatedEvent event);
}
