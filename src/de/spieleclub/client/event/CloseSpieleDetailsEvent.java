package de.spieleclub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CloseSpieleDetailsEvent extends
    GwtEvent<CloseSpieleDetailsEventHandler> {
  public static Type<CloseSpieleDetailsEventHandler> TYPE = new Type<CloseSpieleDetailsEventHandler>();

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<CloseSpieleDetailsEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(CloseSpieleDetailsEventHandler handler) {
    handler.onCloseSpieleDetails(this);
  }
}
