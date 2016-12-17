package de.spieleclub.analysis;

import com.google.gwt.user.client.ui.HasWidgets;

import de.spieleclub.analysis.presenter.AnalysisOverviewPresenter;
import de.spieleclub.analysis.view.AnalysisOverviewImpl;
import de.spieleclub.client.Global;
import de.spieleclub.client.data.AvailablePeriodsFactoryClient;
import de.spieleclub.client.event.AvailablePeriodsUpdatedEvent;
import de.spieleclub.client.event.AvailablePeriodsUpdatedEventHandler;
import de.spieleclub.client.presenter.Presenter;

public class AnalysisController  implements Presenter {
  private HasWidgets container;
  private AnalysisOverviewPresenter analysisOverviewPresenter;


  public AnalysisController() {
    bind();    
  }

  @Override
  public void go(HasWidgets container) {
    this.container = container;    
    analysisOverviewPresenter = new AnalysisOverviewPresenter(new AnalysisOverviewImpl());

    AvailablePeriodsFactoryClient.makeAvailablePeriods();    
  }

  private void bind() {
    createEventHandlerForAvailablePeriodsChangedEvent();
  }
  
  private void createEventHandlerForAvailablePeriodsChangedEvent() {
    Global.eventBus.addHandler(AvailablePeriodsUpdatedEvent.TYPE, new AvailablePeriodsUpdatedEventHandler() {
      @Override
      public void onAvailablePeriodsChanged(AvailablePeriodsUpdatedEvent event) {
        analysisOverviewPresenter.go(container);
      }
    });
  }

}
