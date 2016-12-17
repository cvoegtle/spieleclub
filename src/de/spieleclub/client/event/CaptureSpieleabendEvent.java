package de.spieleclub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CaptureSpieleabendEvent extends GwtEvent<CaptureSpieleabendEventHandler> {
  public static Type<CaptureSpieleabendEventHandler> TYPE = new Type<CaptureSpieleabendEventHandler>();

  @Override
  public GwtEvent.Type<CaptureSpieleabendEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(CaptureSpieleabendEventHandler handler) {
    handler.onCaptureSpieleabend(this);
  }

}
