package de.spieleclub.client.data;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.spieleclub.client.Global;
import de.spieleclub.shared.Spiel;

public class KnownSpieleFactory {
  public static void makeKnownSpiele() {
    Global.spieleclubService.loadSpiele(new AsyncCallback<List<Spiel>>() {      
      @Override
      public void onSuccess(List<Spiel> result) {
        Global.knownSpiele = new KnownSpiele(result);
      }
      
      @Override
      public void onFailure(Throwable caught) {
      }
    });    
  }
  
}
