package de.spieleclub.client.helper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.client.Global;
import de.spieleclub.client.event.AddSpielEvent;

public class NonExistingSpielDialog extends DialogBox {

  interface Binder extends UiBinder<Widget, NonExistingSpielDialog> { }
  private static final Binder binder = GWT.create(Binder.class);
  
  @UiField Button createButton;
  @UiField Button cancelButton;
  
  @UiField SpanElement spieleName;
  
  public NonExistingSpielDialog(String spieleName) {
    setWidget(binder.createAndBindUi(this));

    setHTML(Global.texte.newSpiel());
    this.spieleName.setInnerText(spieleName);
    
    setAnimationEnabled(true);
    setGlassEnabled(true);
  }
  
  @Override
  protected void onLoad() {
  }
  
  @Override
  public void center() {
    super.show();
    createButton.setFocus(true);     
  }
  
  @Override
  protected void onPreviewNativeEvent(NativePreviewEvent preview) {
    super.onPreviewNativeEvent(preview);

    NativeEvent evt = preview.getNativeEvent();
    if (evt.getType().equals("keydown")) {
      // Use the popup's key preview hooks to close the dialog when
      // escape is pressed.
      switch (evt.getKeyCode()) {
      
        case KeyCodes.KEY_ESCAPE:
          hide();
          break;
      }
    }
  }

  @UiHandler("createButton")
  void createClicked(ClickEvent event) {
    Global.eventBus.fireEvent(new AddSpielEvent(spieleName.getInnerText()));
    hide();
  }
  
  @UiHandler("cancelButton")
  void cancelClicked(ClickEvent event) {
    hide();
  }
}
