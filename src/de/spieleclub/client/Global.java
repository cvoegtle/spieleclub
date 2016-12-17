package de.spieleclub.client;

import com.google.gwt.event.shared.HandlerManager;

import de.spieleclub.client.data.KnownSpiele;
import de.spieleclub.client.i18n.Texte;
import de.spieleclub.shared.AvailablePeriods;
import de.spieleclub.shared.LoginInfo;

public class Global {
  
  public static SpieleclubServiceAsync spieleclubService;
  
  public static LoginInfo login = new LoginInfo();
  public static KnownSpiele knownSpiele = new KnownSpiele();
  public static AvailablePeriods availablePeriods = new AvailablePeriods();
  
  public static final HandlerManager eventBus = new HandlerManager(null);
    
  public static Texte texte; 
  
}
