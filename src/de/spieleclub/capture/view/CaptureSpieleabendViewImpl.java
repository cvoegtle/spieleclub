package de.spieleclub.capture.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.spieleclub.client.Global;
import de.spieleclub.client.helper.ColumnDefinition;
import de.spieleclub.client.helper.SelectableList;
import de.spieleclub.shared.helper.ColumnAccess;

public class CaptureSpieleabendViewImpl extends Composite implements CaptureSpieleabendView {

  @UiTemplate("CaptureSpieleabendView.ui.xml")
  interface CaptureSpieleabendViewUiBinder extends UiBinder<Widget, CaptureSpieleabendViewImpl> {}
  private static CaptureSpieleabendViewUiBinder uiBinder = GWT.create(CaptureSpieleabendViewUiBinder.class);

  private Presenter presenter;
  
  @UiField Label dateLabel;
  @UiField DateBox dateBox;
  
  @UiField Label descriptionLabel;
  @UiField TextBox description;
  
  @UiField Label spielLabel;
  @UiField SuggestBox suggestSpiele;
  @UiField Label addInfoLabel;
  @UiField TextBox addInfo;
  
  @UiField Button markupButton;
  @UiField Button addButton;
  @UiField Button removeButton;
  @UiField Button saveButton;
  @UiField Button cancelButton;
  
  @UiField HorizontalPanel selectedPanel;
  private SelectableList selectedSpiele;
  
  public CaptureSpieleabendViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));

    dateLabel.setText(Global.texte.gamesFrom());
    dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));

    descriptionLabel.setText(Global.texte.description());
    
    spielLabel.setText(Global.texte.spiel());
    addInfoLabel.setText(Global.texte.addInfo());
    addButton.setHTML(Global.texte.add());
    removeButton.setHTML(Global.texte.remove());
    
    selectedPanel.setSize("280px", "10em");
    ArrayList<ColumnDefinition> colDefs = new ArrayList<ColumnDefinition>();
    colDefs.add(new ColumnDefinition(Global.texte.spiel(), 150, HasHorizontalAlignment.ALIGN_LEFT));
    colDefs.add(new ColumnDefinition(Global.texte.rounds(), 80, HasHorizontalAlignment.ALIGN_CENTER));
    selectedSpiele = new SelectableList(colDefs);
    selectedPanel.add(selectedSpiele);
    
    saveButton.setHTML(Global.texte.save());
    cancelButton.setHTML(Global.texte.cancel());
  }

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }
  
  @Override
  public void clear() {
    dateBox.setValue(new Date());
    addButton.setEnabled(false);
    
    selectedSpiele.setValues(new ArrayList<ColumnAccess>());
    
    ((MultiWordSuggestOracle)suggestSpiele.getSuggestOracle()).clear();
    suggestSpiele.setValue("");
    suggestSpiele.setFocus(true);    
    addInfo.setValue("");
  }
  
  @Override
  public void init() {
    dateBox.getTextBox().selectAll();
    dateBox.setFocus(true);
  }


  @Override
  public void addSuggestion(String suggestion) {
    if (suggestion != null && suggestion.length() > 0) {
      ((MultiWordSuggestOracle)suggestSpiele.getSuggestOracle()).add(suggestion);
    }
  }
  
  @Override
  public void addMultipleSuggestions(List<String> suggestions) {
    Iterator<String> it = suggestions.iterator();
    while (it.hasNext()) {
      addSuggestion(it.next());
    }
  }

  @Override
  public void setColumnValues(List<ColumnAccess> values) {
    selectedSpiele.setValues(values);
    selectedSpiele.update();
  }

  @Override
  public void update() {
    selectedSpiele.update();
  }
  
  @Override
  public void setFocusToAddButton() {
    addButton.setFocus(true);
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public HasValue<Date> getDate() {
    return dateBox;
  }

  @Override
  public HasValue<String> getDescription() {
    return description;
  }
  
  @Override
  public HasValue<String> getSuggestSpiel() {
    return suggestSpiele;
  }
  
  @Override
  public HasValue<String> getAddInfo() {
    return addInfo;
  }

  @Override
  public HasValue<Integer> getSelectedSpiele() {
    return selectedSpiele;
  }

  @UiHandler("dateBox")
  public void onDateChange(ValueChangeEvent<Date> event) {
    presenter.onDateChanged(event.getValue());
  }

  @UiHandler("description")
  public void onDescriptionChange(ValueChangeEvent<String> event) {
    presenter.onDescriptionChanged(event.getValue());
  }
  
  
  @UiHandler("suggestSpiele")
  public void onValueChange(ValueChangeEvent<String> event) {
    addButton.setFocus(true);
  }

  @UiHandler("suggestSpiele")
  public void onKeyPress(KeyPressEvent event) {
    addInfo.setValue("");
    String text = suggestSpiele.getText();
    addButton.setEnabled(text != null && text.length() > 0);        
  }
  
  @UiHandler("markupButton")
  void onMarkupButtonClicked(ClickEvent event) {
    presenter.onMarkupButtonClicked();
  }

  @UiHandler("addButton")
  void onAddButtonClicked(ClickEvent event) {
    presenter.onAddButtonClicked();
  }
  
  @UiHandler("removeButton")
  void onRemoveButtonClicked(ClickEvent event) {
    presenter.onRemoveButtonClicked();
  }

  @UiHandler("saveButton")
  void onSaveButtonClicked(ClickEvent event) {
    presenter.onSaveButtonClicked();
  }

  @UiHandler("cancelButton")
  void onCancelButtonClicked(ClickEvent event) {
    presenter.onCancelButtonClicked();
  }
}
