package de.spieleclub.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddSpielEvent extends GwtEvent<AddSpielEventHandler> {
  public static Type<AddSpielEventHandler> TYPE = new Type<AddSpielEventHandler>();

  private String spiel;
  
  public AddSpielEvent(String spiel) {
    this.spiel = spiel;
  }
  
  @Override
  public GwtEvent.Type<AddSpielEventHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(AddSpielEventHandler handler) {
    handler.onAddSpiel(this);
  }

  public String getSpiel() {
    return spiel;
  }

}
