package de.spieleclub.analysis.view;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.shared.helper.ColumnAccess;

public interface AnalysisView {
  public interface Presenter {
    void onPeriodChanged(String selectedPeriod);
    void onCheckBoxCustomPeriodChanged();
    void onCustomPeriodChanged();
  }

  void setPresenter(Presenter presenter);
  void clear();
  void clearAnalysis();
  
  void setAvailablePeriods(List<String> availablePeriods);
  void setSelectedPeriod(String selectedPeriod);
  String getSelectedPeriod();
  void setSpieleabendeLink(String spieleabendeLink);

  void setAnalysisData(List<ColumnAccess> analysisData);
  
  void setCustomPeriod(boolean checked);
  
  boolean isCustomPeriod();
  Date getStartDate();
  Date getEndDate();
  
  Widget asWidget();
}
