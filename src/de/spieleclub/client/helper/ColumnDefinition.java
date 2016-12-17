package de.spieleclub.client.helper;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class ColumnDefinition {
  private String caption;
  private int width;
  private HorizontalAlignmentConstant alignment;
  
  public ColumnDefinition(String caption, int width, HorizontalAlignmentConstant alignment) {
    this.caption = caption;
    this.width = width;
    this.alignment = alignment;
  }

  public String getCaption() {
    return caption;
  }

  public int getWidth() {
    return width;
  }
  
  public String getWidthAsString() {
    return Integer.toString(width)+"px";
  }

  public HorizontalAlignmentConstant getAlignment() {
    return alignment;
  }
  
  public Widget getWidget(String text) {
    return new Label(text);
  }
}
