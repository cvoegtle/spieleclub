package de.spieleclub.client.helper;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.client.event.AnchorClickHandlerFactory;

public class ClickableColumnDefinition extends ColumnDefinition {
  private AnchorClickHandlerFactory clickHandlerFactory;
  
  public ClickableColumnDefinition(String caption, int width,
      HorizontalAlignmentConstant alignment, AnchorClickHandlerFactory clickHandlerFactory) {
    super(caption, width, alignment);
    this.clickHandlerFactory = clickHandlerFactory;
  }

  @Override
  public Widget getWidget(String text) {
    Anchor anchor = new Anchor(text);
    anchor.addClickHandler(clickHandlerFactory.createClickHandlerForAnchor(anchor));
    return anchor;
  }
}
