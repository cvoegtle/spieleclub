package de.spieleclub.capture;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

import de.spieleclub.capture.presenter.SpieleabendePresenter;
import de.spieleclub.capture.view.SpieleabendeViewImpl;
import de.spieleclub.client.Global;
import de.spieleclub.client.data.AvailablePeriodsFactoryClient;
import de.spieleclub.client.data.KnownSpieleFactory;
import de.spieleclub.client.event.AvailablePeriodsUpdatedEvent;
import de.spieleclub.client.event.AvailablePeriodsUpdatedEventHandler;
import de.spieleclub.client.event.CaptureSpieleabendCancelledEvent;
import de.spieleclub.client.event.CaptureSpieleabendCancelledEventHandler;
import de.spieleclub.client.event.SpieleabendeChangedEvent;
import de.spieleclub.client.event.SpieleabendeChangedEventHandler;
import de.spieleclub.client.presenter.Presenter;

public class AppController implements Presenter {
  private HasWidgets container;
  private SpieleabendePresenter spieleabendePresenter;
  
  public AppController(HandlerManager eventBus) {
    bind();
  }

  @Override
  public void go(HasWidgets container) {
    this.container = container;
    spieleabendePresenter = new SpieleabendePresenter(new SpieleabendeViewImpl());

    AvailablePeriodsFactoryClient.makeAvailablePeriods();
    
    if (Global.login.isAdmin()) {
      KnownSpieleFactory.makeKnownSpiele();
    }
  }

  private void bind() {
    createEventHandlerForChangeEvent();
    createEventHandlerForCancelEvent();
    createEventHandlerForAvailablePeriodsChangedEvent();
  }

  private void createEventHandlerForChangeEvent() {
    Global.eventBus.addHandler(SpieleabendeChangedEvent.TYPE, new SpieleabendeChangedEventHandler() {
      @Override
      public void onSpieleabendChanged(SpieleabendeChangedEvent event) {
        updateSpieleabende();        
      }
    });
  }
  
  private void createEventHandlerForCancelEvent() {
    Global.eventBus.addHandler(CaptureSpieleabendCancelledEvent.TYPE, new CaptureSpieleabendCancelledEventHandler() {
      @Override
      public void onCaptureSpieleabendCancelled(CaptureSpieleabendCancelledEvent event) {
        doListSpieleabende();
      }
    });
  }
  
  private void createEventHandlerForAvailablePeriodsChangedEvent() {
    Global.eventBus.addHandler(AvailablePeriodsUpdatedEvent.TYPE, new AvailablePeriodsUpdatedEventHandler() {
      @Override
      public void onAvailablePeriodsChanged(AvailablePeriodsUpdatedEvent event) {
        spieleabendePresenter.go(container);
      }
    });
  }

  private void doListSpieleabende() {
    spieleabendePresenter.cancelEdit();
  }
  
  private void updateSpieleabende() {
    spieleabendePresenter.update();    
    spieleabendePresenter.recalculate(false);
  }

  

}
