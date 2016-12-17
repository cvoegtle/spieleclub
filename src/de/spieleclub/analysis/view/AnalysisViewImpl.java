package de.spieleclub.analysis.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.spieleclub.client.Global;
import de.spieleclub.client.event.AnchorClickEventHandler;
import de.spieleclub.client.event.AnchorClickHandlerFactory;
import de.spieleclub.client.helper.ClickableColumnDefinition;
import de.spieleclub.client.helper.ColumnDefinition;
import de.spieleclub.client.helper.SpieleDetailsDialog;
import de.spieleclub.client.helper.TableWithHeader;
import de.spieleclub.shared.helper.ColumnAccess;

public class AnalysisViewImpl extends Composite implements AnalysisView {

  @UiTemplate("AnalysisView.ui.xml")
  interface AnalysisViewUiBinder extends UiBinder<Widget, AnalysisViewImpl> {
  }

  private static AnalysisViewUiBinder uiBinder = GWT.create(AnalysisViewUiBinder.class);

  private Presenter presenter;

  @UiField  Anchor listAnchor;
  @UiField  Label yearListDescription;
  @UiField  ListBox yearList;
  @UiField  HorizontalPanel analysisPanel;

  @UiField  CheckBox checkBoxCustomPeriod;
  @UiField  DateBox startDate;
  @UiField  DateBox endDate;

  private TableWithHeader analysisTable;

  public AnalysisViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));

    initCustomPeriod();
    initLabels();
    initAnalysisTable();
  }

  private void initCustomPeriod() {
    startDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
    endDate.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("dd.MM.yyyy")));
    
    // Workaround um einen Fehler in der DateBox. €nderungen Ÿber die Texteingabe 
    // werden ignoriert
    startDate.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {
      
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getValue() == null || event.getValue().equals("")) {
          presenter.onCustomPeriodChanged();
        }
      }
    });
    
    endDate.getTextBox().addValueChangeHandler(new ValueChangeHandler<String>() {
      
      @Override
      public void onValueChange(ValueChangeEvent<String> event) {
        if (event.getValue() == null || event.getValue().equals("")) {
          presenter.onCustomPeriodChanged();
        }
      }
    });
    // endOfWorkaround
  }

  private void initAnalysisTable() {
    List<ColumnDefinition> columnDefinitions = new ArrayList<ColumnDefinition>();
    columnDefinitions.add(new ColumnDefinition(Global.texte.rank(), 50, HasHorizontalAlignment.ALIGN_RIGHT));
    AnchorClickHandlerFactory clickHandlerFactory = new AnchorClickHandlerFactory(new AnchorClickEventHandler.EventHandler() {
      private Anchor spielAnchor = new Anchor();
      private String nameOfTheClickedSpiel = "";

      @Override
      public void handleAnchorClickEvent(Anchor clickedAnchor) {
        // we know that the text is the name of the Spiel
        nameOfTheClickedSpiel = clickedAnchor.getText();
        this.spielAnchor = clickedAnchor;

        SpieleDetailsDialog spieleDetailsDialog = new SpieleDetailsDialog(nameOfTheClickedSpiel);
        spieleDetailsDialog.setPopupPosition(spielAnchor.getAbsoluteLeft() + 10, spielAnchor.getAbsoluteTop() + 10);
        spieleDetailsDialog.show();
      }
    });
    columnDefinitions.add(new ClickableColumnDefinition(Global.texte.spiel(), 180, HasHorizontalAlignment.ALIGN_LEFT, clickHandlerFactory));
    columnDefinitions.add(new ColumnDefinition(Global.texte.rounds(), 70, HasHorizontalAlignment.ALIGN_CENTER));

    analysisTable = new TableWithHeader(columnDefinitions);

    analysisPanel.add(analysisTable);
  }

  private void initLabels() {
    listAnchor.setHTML(Global.texte.backToStartpage());
    yearListDescription.setText(Global.texte.yearListDescription());
  }

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void clear() {
    yearList.clear();
    analysisTable.clear();
  }

  @Override
  public void clearAnalysis() {
    analysisTable.clear();
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
  public void setSpieleabendeLink(String spieleabendeLink) {
    listAnchor.setHref(spieleabendeLink);
  }

  @Override
  public void setAnalysisData(List<ColumnAccess> analysisData) {
    analysisTable.setValues(analysisData);
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @UiHandler("yearList")
  void onPeriodChange(ChangeEvent event) {
    int selectedIndex = yearList.getSelectedIndex();
    if (selectedIndex >= 0) {
      String selectedPeriod = yearList.getItemText(selectedIndex);
      presenter.onPeriodChanged(selectedPeriod);
    }
  }

  @UiHandler("checkBoxCustomPeriod")
  void onCustomPeriodChanged(ClickEvent event) {
    presenter.onCheckBoxCustomPeriodChanged();
  }

  @Override
  public Date getStartDate() {
    return startDate.getValue();
  }

  @Override
  public Date getEndDate() {
    return endDate.getValue();
  }

  @UiHandler("startDate")
  void onStartDateChange(ValueChangeEvent<Date> event) {
    presenter.onCustomPeriodChanged();
  }

  @UiHandler("endDate")
  void onEndDateChange(ValueChangeEvent<Date> event) {
    presenter.onCustomPeriodChanged();
  }


  @Override
  public String getSelectedPeriod() {
    String selectedPeriod = null;
    
    int selectedIndex = yearList.getSelectedIndex();
    if (selectedIndex >= 0) {
      selectedPeriod = yearList.getItemText(selectedIndex);
    }
    
    return selectedPeriod;
  }

  @Override
  public void setCustomPeriod(boolean checked) {
    checkBoxCustomPeriod.setValue(checked);
  }

  @Override
  public boolean isCustomPeriod() {
    return checkBoxCustomPeriod.getValue();
  }
}
