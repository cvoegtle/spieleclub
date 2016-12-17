package de.spieleclub.capture.presenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.capture.view.CaptureSpieleabendViewImpl;
import de.spieleclub.capture.view.SpieleabendView;
import de.spieleclub.capture.view.SpieleabendViewImpl;
import de.spieleclub.capture.view.SpieleabendeView;
import de.spieleclub.client.Global;
import de.spieleclub.client.event.CaptureSpieleabendEvent;
import de.spieleclub.client.event.CaptureSpieleabendEventHandler;
import de.spieleclub.client.helper.MessageHandler;
import de.spieleclub.client.helper.Recalculator;
import de.spieleclub.client.presenter.Presenter;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.Spieleabend;

public class SpieleabendePresenter implements Presenter, SpieleabendeView.Presenter, MessageHandler {
  private SpieleabendeView view;

  private List<Spieleabend> spieleabende = new ArrayList<Spieleabend>();
  private Period selectedPeriod = Global.availablePeriods.getCurrentPeriod();
  
  public SpieleabendePresenter(SpieleabendeView view) {
    view.setPresenter(this);
    this.view = view;    
    
    bind();
  }

  private void bind() {
    Global.eventBus.addHandler(CaptureSpieleabendEvent.TYPE, new CaptureSpieleabendEventHandler() {
      @Override
      public void onCaptureSpieleabend(CaptureSpieleabendEvent event) {
        view.setEnabled(false);
      }
    });
  }


  @Override
  public void go(HasWidgets container) {
    
    view.clear();
    view.setUserLink(Global.login.getLink(Global.texte.login(), Global.texte.logout()));
    view.setEditable(Global.login.isAdmin());

    view.setAvailablePeriods(Global.availablePeriods.getAvailablePeriodsAsStrings());
    readSelectedPeriodFromUrl();
    view.setSelectedPeriod(selectedPeriod.getLabel());
    view.setAnalysisLink(createAnalysisLinkForPeriod(selectedPeriod.getLabel()));
    
    view.setRecalculateAutomatically(true);
    
    container.clear();
    container.add(view.asWidget());
    
    loadSpieleabende();
  }

  
  @Override
  public void onAddButtonClicked() {
    CaptureSpieleabendViewImpl captureSpieleabendView = new CaptureSpieleabendViewImpl();
    new CaptureSpieleabendPresenter(new Spieleabend(Global.login.getSpieler()), captureSpieleabendView).go(view.getEditContainer());
  }
  
  @Override
  public void onRecalculateButtonClicked() {
    recalculate(true);
  }

  @Override
  public void onPeriodChanged(String selectedPeriod) {
    this.selectedPeriod = Global.availablePeriods.getPeriodByLabel(selectedPeriod);
    view.setAnalysisLink(createAnalysisLinkForPeriod(selectedPeriod));
    loadSpieleabende();
  }

  public void update() {
    view.clearEditContainer();
    view.setEnabled(true);
    loadSpieleabende();
  }
  
  public void cancelEdit() {
    view.clearEditContainer();
    view.setEnabled(true);
    view.setData(getSpieleabendWidgets());
  }
  
  public void recalculate(boolean forceRecalculation) {
    view.clearAdminConsole();
    if (view.isRecalculateAutomatically() || forceRecalculation) {
      view.showAdminConsole();
      new Recalculator(this).clearAndRecalculate();
    }
  }

  private void loadSpieleabende() {
    view.clearData();
    Global.spieleclubService.loadSpieleabende(selectedPeriod.getStartDate(), selectedPeriod.getEndDate(), new AsyncCallback<List<Spieleabend>>() {
      @Override
      public void onFailure(Throwable caught) {
        Window.alert(Global.texte.errorLoadingSpieleabend());
      }

      @Override
      public void onSuccess(List<Spieleabend> result) {
        spieleabende = result;
        view.setData(getSpieleabendWidgets());
      }
    });    
  }

  private ArrayList<Widget> getSpieleabendWidgets() {
    ArrayList<Widget> widgets = new ArrayList<Widget>();
    Iterator<Spieleabend> it = spieleabende.iterator();
    while (it.hasNext()) {
      SpieleabendView sv = new SpieleabendViewImpl(); 
      SpieleabendPresenter sp = new SpieleabendPresenter(it.next(), sv);
      sp.go(null);
      widgets.add(sv.asWidget());
    }
    return widgets;
  }
  
  private void readSelectedPeriodFromUrl() {
    String period = Window.Location.getParameter("period");
    if (period != null && period.length() > 0) {
      selectedPeriod = Global.availablePeriods.getPeriodByLabel(period);
    }
  }
  
  private String createAnalysisLinkForPeriod(String period) {
    return "analysis.html?period="+period;
  }

  @Override
  public void handleMessage(String text) {
    view.addTextToAdminConsole(text);
  }

}
