package de.spieleclub.analysis.view;

import com.google.gwt.user.client.ui.Widget;

public interface AnalysisOverview {
  public interface Presenter {
    void onAddColumnClicked();
  }
  
  void setPresenter(Presenter presenter);
  
  void addWidget(Widget widget);
  Widget asWidget();
}
