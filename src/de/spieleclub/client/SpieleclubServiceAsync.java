package de.spieleclub.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.spieleclub.shared.LoginInfo;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spiel;
import de.spieleclub.shared.SpieleProfile;
import de.spieleclub.shared.Spieleabend;

public interface SpieleclubServiceAsync {

  void loadSpiele(AsyncCallback<List<Spiel>> callback);

  void saveSpiel(Spiel spiel, AsyncCallback<Spiel> callback);

  void saveSpieleabend(Spieleabend spieleabend, AsyncCallback<Boolean> callback);

  void loadSpieleabende(Date startDate, Date endDate,
      AsyncCallback<List<Spieleabend>> callback);

  void loginSpieler(String requestUri, AsyncCallback<LoginInfo> callback);

  void loadDateOfFirstSpieleabend(AsyncCallback<Date> callback);

  void loadAnalysis(Period period,
      AsyncCallback<Ranking> callback);

  void loadSpieleProfile(String spieleName,
      AsyncCallback<SpieleProfile> callback);

  void clearPrecalculatedData(AsyncCallback<Void> callback);
  void touchRanking(Period period, AsyncCallback<Void> callback);
  void touchDates(AsyncCallback<Void> callback);
}
