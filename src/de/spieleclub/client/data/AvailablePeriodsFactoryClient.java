package de.spieleclub.client.data;

import java.util.Date;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.spieleclub.client.Global;
import de.spieleclub.client.event.AvailablePeriodsUpdatedEvent;
import de.spieleclub.shared.AvailablePeriods;

public class AvailablePeriodsFactoryClient {
  public static void makeAvailablePeriods() {
    Global.spieleclubService.loadDateOfFirstSpieleabend(new AsyncCallback<Date>() {
      @Override
      public void onFailure(Throwable caught) {
        Window.alert(Global.texte.errorLoadingDateOfFirstSpieleabend());
      }

      @Override
      public void onSuccess(Date dateOfFirstSpieleabend) {
        Global.availablePeriods = new AvailablePeriods(dateOfFirstSpieleabend, Global.texte.all());
        Global.eventBus.fireEvent(new AvailablePeriodsUpdatedEvent());
      }
    });

  }
}
