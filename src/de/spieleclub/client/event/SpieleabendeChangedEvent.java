package de.spieleclub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class SpieleabendeChangedEvent extends GwtEvent<SpieleabendeChangedEventHandler> {
  public static Type<SpieleabendeChangedEventHandler> TYPE = new Type<SpieleabendeChangedEventHandler>();


  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<SpieleabendeChangedEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(SpieleabendeChangedEventHandler handler) {
    handler.onSpieleabendChanged(this);
  }

}
