package de.spieleclub.client.event;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class AnchorClickEventHandler implements ClickHandler {
  public interface EventHandler {
    void handleAnchorClickEvent(Anchor clickedAnchor);
  }
  
  private Anchor handleClicksFor;
  private EventHandler eventHandler;
  
  AnchorClickEventHandler(Anchor anchor, EventHandler eventHandler) {
    this.handleClicksFor = anchor;
    this.eventHandler = eventHandler;
  }
  
  @Override
  public void onClick(ClickEvent event) {
    eventHandler.handleAnchorClickEvent(handleClicksFor);
  }

}
