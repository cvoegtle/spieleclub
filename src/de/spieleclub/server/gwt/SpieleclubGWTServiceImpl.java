package de.spieleclub.server.gwt;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.spieleclub.client.SpieleclubService;
import de.spieleclub.server.SpieleclubServiceImpl;
import de.spieleclub.shared.LoginInfo;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spiel;
import de.spieleclub.shared.SpieleProfile;
import de.spieleclub.shared.Spieleabend;

@SuppressWarnings("serial")
public class SpieleclubGWTServiceImpl extends RemoteServiceServlet implements
    SpieleclubService {

  @Override
  public List<Spiel> loadSpiele() {
    return SpieleclubServiceImpl.loadSpiele();
  }

  @Override
  public Spiel saveSpiel(Spiel spiel) {
    return SpieleclubServiceImpl.saveSpiel(spiel);
  }

  @Override
  public boolean saveSpieleabend(Spieleabend spieleabend) {
    return SpieleclubServiceImpl.saveSpieleabend(spieleabend);  
  }

  @Override
  public List<Spieleabend> loadSpieleabende(Date startDate, Date endDate) {
    return SpieleclubServiceImpl.loadSpieleabende(startDate, endDate);
  }

  @Override
  public LoginInfo loginSpieler(String requestUri) {    
    return SpieleclubServiceImpl.loginSpieler(requestUri);
  }

  @Override
  public Date loadDateOfFirstSpieleabend() {
    return SpieleclubServiceImpl.loadDateOfFirstSpieleabend();
  }

  @Override
  public Ranking loadAnalysis(Period period) {
    return SpieleclubServiceImpl.loadAnalysis(period);
  }

  @Override
  public SpieleProfile loadSpieleProfile(String spieleName) {
    return SpieleclubServiceImpl.loadSpieleProfile(spieleName);
  }

  @Override
  public void clearPrecalculatedData() {
    SpieleclubServiceImpl.clearPrecalculatedData();
  }

  @Override
  public void touchRanking(Period period) {
    SpieleclubServiceImpl.touchRanking(period);
  }

  @Override
  public void touchDates() {
    SpieleclubServiceImpl.touchDates();
  }
}
