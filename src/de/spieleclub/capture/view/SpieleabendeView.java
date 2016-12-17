package de.spieleclub.capture.view;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.shared.Link;


public interface SpieleabendeView {
  public interface Presenter {
    void onAddButtonClicked();
    void onPeriodChanged(String selectedPeriod);
    void onRecalculateButtonClicked();
  }

  void setPresenter(Presenter presenter);
  void clear();
  void clearData();
  
  void setData(List<Widget> data);
  void setAvailablePeriods(List<String> availablePeriods);
  void setSelectedPeriod(String label);
  
  void setUserLink(Link userLink);
  void setAnalysisLink(String analysisUrl);
  
  void setEnabled(boolean enabled);
  void setEditable(boolean readOnly);
  
  void showAdminConsole();
  void clearAdminConsole();
  void addTextToAdminConsole(String text);
  
  HasWidgets getEditContainer();
  void clearEditContainer();
  
  boolean isRecalculateAutomatically();
  void setRecalculateAutomatically(boolean automatically);
  
  Widget asWidget();
}
