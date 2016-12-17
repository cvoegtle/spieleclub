package de.spieleclub.client.event;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class AnchorClickHandlerFactory {
  
  private AnchorClickEventHandler.EventHandler eventHandler;
  
  public AnchorClickHandlerFactory(AnchorClickEventHandler.EventHandler eventHandler) {
    this.eventHandler = eventHandler;
  }
  
  public ClickHandler createClickHandlerForAnchor(Anchor anchor) {
    return new AnchorClickEventHandler(anchor, eventHandler);
  }

}
