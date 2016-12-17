package de.spieleclub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AvailablePeriodsUpdatedEvent extends GwtEvent<AvailablePeriodsUpdatedEventHandler> {
  public static Type<AvailablePeriodsUpdatedEventHandler> TYPE = new Type<AvailablePeriodsUpdatedEventHandler>();

  @Override
  public Type<AvailablePeriodsUpdatedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(AvailablePeriodsUpdatedEventHandler handler) {
    handler.onAvailablePeriodsChanged(this);
  }

}
