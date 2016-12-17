package de.spieleclub.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.spieleclub.shared.LoginInfo;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spiel;
import de.spieleclub.shared.SpieleProfile;
import de.spieleclub.shared.Spieleabend;

@RemoteServiceRelativePath("service")
public interface SpieleclubService extends RemoteService {
  
  LoginInfo loginSpieler(String requestUri); 
  
  List<Spiel> loadSpiele();
  Ranking loadAnalysis(Period period);
  SpieleProfile loadSpieleProfile(String spieleName);
  Spiel saveSpiel(Spiel spiel);
  
  boolean saveSpieleabend(Spieleabend spieleabend);
  List<Spieleabend> loadSpieleabende(Date startDate, Date endDate);  
  
  Date loadDateOfFirstSpieleabend();
  
  void clearPrecalculatedData();
  void touchRanking(Period period);
  void touchDates();
}
