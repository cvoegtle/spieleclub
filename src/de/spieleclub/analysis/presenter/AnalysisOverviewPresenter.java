package de.spieleclub.analysis.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;

import de.spieleclub.analysis.view.AnalysisOverview;
import de.spieleclub.analysis.view.AnalysisViewImpl;
import de.spieleclub.client.presenter.Presenter;

public class AnalysisOverviewPresenter  implements Presenter, AnalysisOverview.Presenter {
  private AnalysisOverview view;
  
  public AnalysisOverviewPresenter(AnalysisOverview view) {
    view.setPresenter(this);
    this.view = view;
  }

  @Override
  public void go(HasWidgets container) {
    addAnalysisView();
    container.add(view.asWidget());
  }

  private void addAnalysisView() {
    SimplePanel panel = new SimplePanel();

    AnalysisPresenter analysisPresenter = new AnalysisPresenter(new AnalysisViewImpl());
    analysisPresenter.go(panel);

    view.addWidget(panel);
  }

  @Override
  public void onAddColumnClicked() {
    addAnalysisView();
  }  
}
