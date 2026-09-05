package de.spieleclub.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import de.spieleclub.server.persistence.PersistentGespieltesSpiel;
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

  private static DatastoreService getDatastore() {
    return DatastoreServiceFactory.getDatastoreService();
  }

  public static List<Spiel> loadSpiele() {
    DatastoreService datastore = getDatastore();
    Query query = new Query(PersistentSpiel.KIND).addSort("name", Query.SortDirection.ASCENDING);
    List<Spiel> spiele = new ArrayList<>();
    for (Entity entity : datastore.prepare(query).asIterable()) {
      PersistentSpiel ps = new PersistentSpiel(entity, datastore);
      spiele.add(ps.getSpiel());
    }
    return spiele;
  }

  public static Spiel saveSpiel(Spiel spiel) {
    DatastoreService datastore = getDatastore();
    PersistentSpiel ps = new PersistentSpiel(spiel);
    Entity entity = ps.toEntity();
    datastore.put(entity);
    ps.setKey(entity.getKey());
    
    if (ps.getCreator() != null) {
      Entity creatorEntity = ps.getCreator().toEntity(entity.getKey());
      datastore.put(creatorEntity);
    }
    
    return ps.getSpiel();
  }

  public static boolean saveSpieleabend(Spieleabend spieleabend) {
    DatastoreService datastore = getDatastore();
    if (spieleabend.getWebsafeKey() != null) {
      Key oldKey = KeyFactory.stringToKey(spieleabend.getWebsafeKey());
      Query childQuery = new Query(PersistentGespieltesSpiel.KIND).setAncestor(oldKey).setKeysOnly();
      List<Key> keysToDelete = new ArrayList<>();
      for (Entity child : datastore.prepare(childQuery).asIterable()) {
        keysToDelete.add(child.getKey());
      }
      keysToDelete.add(oldKey);
      datastore.delete(keysToDelete);
    }

    PersistentSpieleabend psa = new PersistentSpieleabend(spieleabend);
    Entity saEntity = psa.toEntity();
    datastore.put(saEntity);
    psa.setKey(saEntity.getKey());

    if (psa.getCreator() != null) {
      Entity creatorEntity = psa.getCreator().toEntity(saEntity.getKey());
      datastore.put(creatorEntity);
    }

    if (psa.getGespielteSpiele() != null) {
      List<Entity> children = new ArrayList<>();
      int idx = 0;
      for (PersistentGespieltesSpiel gs : psa.getGespielteSpiele()) {
        children.add(gs.toEntity(saEntity.getKey(), idx++));
      }
      if (!children.isEmpty()) {
        datastore.put(children);
      }
    }

    return true;
  }

  public static List<Spieleabend> loadSpieleabende(Date startDate, Date endDate) {
    DatastoreService datastore = getDatastore();
    return new SpieleabendAccess(datastore).read(startDate, endDate, SortingOrder.DESCENDING);
  }

  public static LoginInfo loginSpieler(String requestUri) {
    DatastoreService datastore = getDatastore();
    UserAuthorisation userAuthorisation = new UserAuthorisation(datastore, requestUri);
    return userAuthorisation.checkAuthorisation();
  }

  public static Date loadDateOfFirstSpieleabend() {
    DatastoreService datastore = getDatastore();
    return new SpieleabendAccess(datastore).readDateOfFirstSpieleabend(null);
  }

  public static Ranking loadAnalysis(Period period) {
    Ranking result;
    DatastoreService datastore = getDatastore();
    if (period.isUserCreated()) {
      List<Spieleabend> spieleabende = new SpieleabendAccess(datastore).read(period.getStartDate(), period.getEndDate(), SortingOrder.ASCENDING);
      result = new SpieleStatisticCalculator(period.getLabel(), spieleabende).analyse();
    } else {
      result = new RankingCacheFactory(datastore).getRankingCache().getRanking(period);
    }
    return result;
  }

  public static SpieleProfile loadSpieleProfile(String spieleName) {
    DatastoreService datastore = getDatastore();
    SpieleProfiler profiler = new SpieleProfiler(datastore, spieleName);
    profiler.generateProfile();
    return profiler.getSpieleProfile();
  }

  public static void clearPrecalculatedData() {
    DatastoreService datastore = getDatastore();
    new RankingCacheFactory(datastore).getRankingCache().clear();
  }

  public static void touchRanking(Period period) {
    loadAnalysis(period);
  }

  public static void touchDates() {
    DatastoreService datastore = getDatastore();
    new RankingCacheFactory(datastore).getRankingCache().getDateOfFirstPlay("Puerto Rico");
  }
}
