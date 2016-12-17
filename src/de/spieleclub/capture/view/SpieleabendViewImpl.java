package de.spieleclub.capture.view;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SpieleabendViewImpl extends Composite implements SpieleabendView {

  @UiTemplate("SpieleabendView.ui.xml")
  interface SpieleabendViewUiBinder extends UiBinder<Widget, SpieleabendViewImpl> {}
  private static SpieleabendViewUiBinder uiBinder = GWT.create(SpieleabendViewUiBinder.class);
  
  @UiField VerticalPanel outerPanel;
  
  @UiField HorizontalPanel datePanel;
  @UiField Label dateLabel;
  @UiField Button editButton;
  @UiField Label description;
  @UiField HTML spieleList;  
  
  private Presenter presenter;
  
  public SpieleabendViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    datePanel.setCellVerticalAlignment(dateLabel, HasVerticalAlignment.ALIGN_MIDDLE);
    datePanel.setCellVerticalAlignment(editButton, HasVerticalAlignment.ALIGN_MIDDLE);
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void setEnabled(boolean enabled) {
    editButton.setEnabled(enabled);
  }

  @Override
  public void setEditable(boolean editable) {
    editButton.setVisible(editable);    
  }

  @Override
  public HasWidgets getEditContainer() {
    return outerPanel;
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public void setLabelText(String text) {
    dateLabel.setText(text);    
  }
  
  @Override
  public void setDescription(String text) {
    description.setText(text);
  }

  @Override
  public void setListData(List<String> data) {
    String html = "<ul>";
    Iterator<String> it = data.iterator();
    while (it.hasNext()) {
      html += "<li>" + it.next() + "</li>";
    }    
    html += "</ul>";
    spieleList.setHTML(html);
  }

  @UiHandler("editButton")
  void onEditButtonClicked(ClickEvent event) {
    if (presenter != null) {
      presenter.onEditButtonClicked();
    }
  }

}
