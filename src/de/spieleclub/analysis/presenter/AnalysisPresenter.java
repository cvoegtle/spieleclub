package de.spieleclub.analysis.presenter;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import de.spieleclub.analysis.view.AnalysisView;
import de.spieleclub.client.Global;
import de.spieleclub.client.presenter.Presenter;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.Ranking;

public class AnalysisPresenter implements Presenter, AnalysisView.Presenter {
  private AnalysisView view;

  private Period selectedPeriod = Global.availablePeriods.getCurrentPeriod();

  public AnalysisPresenter(AnalysisView view) {
    view.setPresenter(this);
    this.view = view;
  }

  @Override
  public void go(HasWidgets container) {
    view.clear();
    view.setAvailablePeriods(Global.availablePeriods.getAvailablePeriodsAsStrings());
    readSelectedPeriodFromUrl();
    view.setSelectedPeriod(selectedPeriod.getLabel());
    view.setSpieleabendeLink(createSpieleabendeLinkForPeriod(selectedPeriod.getLabel()));

    container.clear();
    container.add(view.asWidget());

    loadAnalysis();
  }

  @Override
  public void onPeriodChanged(String selectedPeriod) {
    view.clearAnalysis();
    view.setCustomPeriod(false);
    this.selectedPeriod = Global.availablePeriods.getPeriodByLabel(selectedPeriod);
    view.setSpieleabendeLink(createSpieleabendeLinkForPeriod(selectedPeriod));
    loadAnalysis();
  }

  @Override
  public void onCheckBoxCustomPeriodChanged() {
    if (view.isCustomPeriod()) {
      view.clearAnalysis();
      this.selectedPeriod = new Period(view.getStartDate(), view.getEndDate());
      loadAnalysis();
    } else {
      String periodLabel = view.getSelectedPeriod();
      onPeriodChanged(periodLabel);
    }    
  }
  
  @Override
  public void onCustomPeriodChanged() {
    view.clearAnalysis();
    view.setCustomPeriod(true);
    this.selectedPeriod = new Period(view.getStartDate(), view.getEndDate());
    loadAnalysis();
  }
  
  private void loadAnalysis() {
    Global.spieleclubService.loadAnalysis(selectedPeriod, new AsyncCallback<Ranking>() {
      @Override
      public void onFailure(Throwable caught) {
        Window.alert(Global.texte.errorLoadingAnalysis());
      }

      @Override
      public void onSuccess(Ranking result) {
        view.setAnalysisData(result.getColumnAccessList());
      }
      
    });    
  }

  private void readSelectedPeriodFromUrl() {
    String period = Window.Location.getParameter("period");
    if (period != null && period.length() > 0) {
      selectedPeriod = Global.availablePeriods.getPeriodByLabel(period);
    }
  }
  
  private String createSpieleabendeLinkForPeriod(String period) {
    return "spieleclub.html?period="+period;
  }

}
