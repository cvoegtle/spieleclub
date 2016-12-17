package de.spieleclub.capture.view;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.capture.view.helper.ColumnsCookie;
import de.spieleclub.capture.view.helper.SpieleabendeTableBuilder;
import de.spieleclub.client.Global;
import de.spieleclub.shared.Link;

public class SpieleabendeViewImpl extends Composite implements SpieleabendeView {
  
  @UiTemplate("SpieleabendeView.ui.xml")
  interface SpieleabendeViewUiBinder extends UiBinder<Widget, SpieleabendeViewImpl> {}
  private static SpieleabendeViewUiBinder uiBinder = GWT.create(SpieleabendeViewUiBinder.class);

  private Presenter presenter;
  
  Anchor userAnchor;
  Anchor spieleclubAnchor;

  @UiField Anchor analysisAnchor;
  
  
  @UiField Button addButton;
  @UiField Button recalculateButton;
  @UiField CheckBox automaticallyRecalculate;
  @UiField Label yearListDescription;
  @UiField ListBox yearList;
  @UiField Label columnsDescription;
  @UiField ListBox columnsList;
  @UiField FlexTable spieleabendeTable;
  @UiField SimplePanel spieleabendePanel;
  @UiField SimplePanel editPanel;
  
  @UiField HorizontalPanel infoArea;

  private TextArea adminConsole = new TextArea();
  
  public SpieleabendeViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    spieleabendeTable.setBorderWidth(1);
    
    initAnchors();    
    initColumns();
  }

  private void initAnchors() {
    spieleclubAnchor = new Anchor(Global.texte.backToSpieleclub(), "http://www.spieleclub-paderborn.de");
    RootPanel.get("linkToSpieleclub").add(spieleclubAnchor);
    
    userAnchor = new Anchor();
    RootPanel.get("linkToLogin").add(userAnchor);
    
    analysisAnchor.setText(Global.texte.toTheAnalysis());
    yearListDescription.setText(Global.texte.yearListDescription());
  }
  
  private void initColumns() {
    columnsDescription.setText(Global.texte.columns());
    columnsList.clear();
    for (int col = 1; col <= ColumnsCookie.getMaxColumns(); col++) {
      columnsList.addItem(new Integer(col).toString());
    }
    columnsList.setSelectedIndex(ColumnsCookie.readNumberOfColumns()-1);
  }
  

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }
  
  @Override
  public void clear() {
    addButton.setEnabled(true);
    addButton.setVisible(false);
    yearList.clear();
    editPanel.clear();
    spieleabendeTable.removeAllRows();    
  }
  
  @Override
  public void clearData() {
    spieleabendeTable.removeAllRows();
  }
  
  @Override
  public void clearEditContainer() {
    editPanel.clear();
  }
  
  @Override
  public void setData(List<Widget> data) {
    FlexTable newSpieleabendeTable = new FlexTable();
    new SpieleabendeTableBuilder(newSpieleabendeTable).addWidgetsToTable(data.iterator());
    replaceSpieleabendeTable(newSpieleabendeTable);
  }

  @Override
  public void setAvailablePeriods(List<String> availablePeriods) {
    yearList.clear();
    Iterator<String> it = availablePeriods.iterator();
    while (it.hasNext()) {
      yearList.addItem(it.next());      
    }
  }
  
  @Override
  public void setSelectedPeriod(String selectedPeriod) {
    for (int index = 0; index < yearList.getItemCount(); index++) { 
      if (yearList.getItemText(index).equals(selectedPeriod)) {
        yearList.setSelectedIndex(index);
        break;
      }
    }    
  }

  @Override
  public void setUserLink(Link userLink) {
    userAnchor.setText(userLink.getDescription());
    userAnchor.setHref(userLink.getUrl());    
  }
  
  @Override
  public void setAnalysisLink(String analysisUrl) {
    analysisAnchor.setHref(analysisUrl);
  }


  @Override
  public void setEnabled(boolean enabled) {
    addButton.setEnabled(enabled);
    recalculateButton.setEnabled(enabled);
  }

  @Override
  public void setEditable(boolean editable) {
    addButton.setVisible(editable);
    recalculateButton.setVisible(editable);
    automaticallyRecalculate.setVisible(editable);
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public HasWidgets getEditContainer() {
    return editPanel;
  }

  @Override
  public boolean isRecalculateAutomatically() {
    return automaticallyRecalculate.getValue();
  }
  
  @UiHandler("addButton")
  void onAddButtonClicked(ClickEvent event) {
    setEnabled(false);
    if (presenter != null) {
      presenter.onAddButtonClicked();
    }
  }
  
  @UiHandler("recalculateButton")
  void onRecalculateButtonClicked(ClickEvent event) {
    if (presenter != null) {
      presenter.onRecalculateButtonClicked();
    }
  }
  
  @UiHandler("yearList")
  void onPeriodChange(ChangeEvent event) {
     int selectedIndex = yearList.getSelectedIndex();
     if (selectedIndex >= 0) {
       String selectedPeriod = yearList.getItemText(selectedIndex);
       presenter.onPeriodChanged(selectedPeriod);
     }
  }
  
  @UiHandler("columnsList")
  void onColumnsChange(ChangeEvent event) {
    int selectedIndex = columnsList.getSelectedIndex();
    if (selectedIndex >= 0) {
      ColumnsCookie.setColumnsCookie(new Integer(columnsList.getItemText(selectedIndex)));
      rebuildSpieleabendTable();
    }    
  }

  
  private void rebuildSpieleabendTable() {
    
    Iterator<Widget> it = spieleabendeTable.iterator();
    
    SpieleabendeTableBuilder builder = new SpieleabendeTableBuilder();
    builder.addWidgetsToTable(it);
    FlexTable newSpieleabendeTable = builder.getTable();
    
    replaceSpieleabendeTable(newSpieleabendeTable);    
  }
  
  
  private void replaceSpieleabendeTable(FlexTable newSpieleabendeTable) {
    spieleabendePanel.remove(spieleabendeTable);
    spieleabendeTable = newSpieleabendeTable;
    spieleabendePanel.add(spieleabendeTable);
  }

  
  @Override
  public void showAdminConsole() {
    adminConsole.setHeight("20em");
    adminConsole.setWidth("20em");
    adminConsole.setEnabled(false);
    infoArea.add(adminConsole);
  }

  @Override
  public void clearAdminConsole() {
    adminConsole.setText("");
  }

  @Override
  public void addTextToAdminConsole(String text) {
    String adminMessages = adminConsole.getText();
    adminMessages = text + "\n" + adminMessages;
    adminConsole.setText(adminMessages);
  }

  @Override
  public void setRecalculateAutomatically(boolean automatically) {
    automaticallyRecalculate.setValue(automatically);
  }

}
