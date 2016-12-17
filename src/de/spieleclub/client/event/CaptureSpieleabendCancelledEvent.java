package de.spieleclub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CaptureSpieleabendCancelledEvent extends
    GwtEvent<CaptureSpieleabendCancelledEventHandler> {
  public static Type<CaptureSpieleabendCancelledEventHandler> TYPE = new Type<CaptureSpieleabendCancelledEventHandler>();


  @Override
  public GwtEvent.Type<CaptureSpieleabendCancelledEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(CaptureSpieleabendCancelledEventHandler handler) {
    handler.onCaptureSpieleabendCancelled(this);
  }

}
