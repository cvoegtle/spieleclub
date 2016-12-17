package de.spieleclub.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.server.persistence.PMF;
import de.spieleclub.server.persistence.PersistentSpiel;
import de.spieleclub.server.persistence.PersistentSpieleabend;
import de.spieleclub.server.persistence.SortingOrder;
import de.spieleclub.server.persistence.SpieleabendAccess;
import de.spieleclub.server.service.RankingCacheFactory;
import de.spieleclub.server.service.SpieleProfiler;
import de.spieleclub.server.service.SpieleStatisticCalculator;
import de.spieleclub.server.service.UserAuthorisation;
import de.spieleclub.shared.LoginInfo;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spiel;
import de.spieleclub.shared.SpieleProfile;
import de.spieleclub.shared.Spieleabend;

public class SpieleclubServiceImpl {
  public static List<Spiel> loadSpiele() {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    List<Spiel> spiele = new ArrayList<>();
    try { 
      Query query = pm.newQuery(PersistentSpiel.class);
      query.setOrdering("name ascending");
      @SuppressWarnings("unchecked")
      List<PersistentSpiel> tempSpiele = (List<PersistentSpiel>)query.execute();
      Iterator<PersistentSpiel> it = tempSpiele.iterator();
      while (it.hasNext()) {
        spiele.add(it.next().getSpiel());
      }
    } finally {
      pm.close();
    }
    return spiele;
  }

  public static Spiel saveSpiel(Spiel spiel) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {      
      PersistentSpiel ps = new PersistentSpiel(spiel);
      pm.makePersistent(ps);
      // get the spiel including its key
      spiel = ps.getSpiel();
    } finally {
      pm.close();
    }
    return spiel;
  }

  public static boolean saveSpieleabend(Spieleabend spieleabend) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      if (spieleabend.getWebsafeKey() != null) {
        Key key = KeyFactory.stringToKey(spieleabend.getWebsafeKey());
        PersistentSpieleabend originalSpieleabend = pm.getObjectById(PersistentSpieleabend.class, key);
        pm.deletePersistent(originalSpieleabend);
      }
      pm.makePersistent(new PersistentSpieleabend(spieleabend));
    } finally {
      pm.close();
    }
    return true;
  }

  public static List<Spieleabend> loadSpieleabende(Date startDate, Date endDate) {
    List<Spieleabend> result = new ArrayList<>();
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      result = new SpieleabendAccess(pm).read(startDate, endDate, SortingOrder.DESCENDING);
    } finally {
      pm.close();
    }

    return result;
  }

  public static LoginInfo loginSpieler(String requestUri) {    
    LoginInfo result = new LoginInfo();
    
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      UserAuthorisation userAuthorisation = new UserAuthorisation(pm, requestUri);
      result = userAuthorisation.checkAuthorisation();
    } finally {
      pm.close();
    }
       
    return result;
  }

  public static Date loadDateOfFirstSpieleabend() {
    Date result = new Date();
    
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      result = new SpieleabendAccess(pm).readDateOfFirstSpieleabend(null);
    } finally {
      pm.close();
    }

    return result;
  }

  public static Ranking loadAnalysis(Period period) {
    Ranking result = new Ranking();
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      if (period.isUserCreated()) {
        List<Spieleabend> spieleabende = new SpieleabendAccess(pm).read(period.getStartDate(), period.getEndDate(), SortingOrder.ASCENDING);
        result = new SpieleStatisticCalculator(period.getLabel(), spieleabende).analyse();
      } else {
        result = new RankingCacheFactory(pm).getRankingCache().getRanking(period);
      }
    } finally {
      pm.close();
    }

    return result;
  }

  public static SpieleProfile loadSpieleProfile(String spieleName) {
    SpieleProfile result = new SpieleProfile();
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      SpieleProfiler profiler = new SpieleProfiler(pm, spieleName);
      profiler.generateProfile();
      result = profiler.getSpieleProfile();
    } finally {
      pm.close();
    }

    return result;
  }

  public static void clearPrecalculatedData() {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      new RankingCacheFactory(pm).getRankingCache().clear();
    } finally {
      pm.close();
    }
  }

  public static void touchRanking(Period period) {
    loadAnalysis(period);
  }

  public static void touchDates() {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      new RankingCacheFactory(pm).getRankingCache().getDateOfFirstPlay("Puerto Rico");
    } finally {
      pm.close();
    }
  }


}
