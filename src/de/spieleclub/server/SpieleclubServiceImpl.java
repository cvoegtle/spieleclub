package de.spieleclub.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (gs.getSpielKey() == null && gs.getName() != null) {
          Key key = resolveOrCreateSpielKey(datastore, gs.getName());
          gs.setSpielKey(key);
        }
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

  public static Key resolveOrCreateSpielKey(DatastoreService datastore, String name) {
    if (name == null || name.trim().isEmpty()) {
      return null;
    }
    name = name.trim();
    Query query = new Query(PersistentSpiel.KIND)
        .setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, name));
    for (Entity entity : datastore.prepare(query).asIterable()) {
      return entity.getKey();
    }
    Entity newSpiel = new Entity(PersistentSpiel.KIND);
    newSpiel.setProperty("name", name);
    newSpiel.setProperty("creation", new Date());
    datastore.put(newSpiel);
    return newSpiel.getKey();
  }

  public static String migrateGespielteSpieleToTechnicalKeys() {
    DatastoreService datastore = getDatastore();

    // 1. Alle PersistentSpiel in Map laden: Name -> Key
    Query spielQuery = new Query(PersistentSpiel.KIND);
    Map<String, Key> nameToKeyMap = new HashMap<>();
    for (Entity spiel : datastore.prepare(spielQuery).asIterable()) {
      String name = (String) spiel.getProperty("name");
      if (name != null) {
        nameToKeyMap.put(name, spiel.getKey());
      }
    }

    // 2. Alle PersistentGespieltesSpiel prüfen
    Query gsQuery = new Query(PersistentGespieltesSpiel.KIND);
    List<Entity> toUpdate = new ArrayList<>();
    int totalCount = 0;
    int updatedCount = 0;
    int newlyCreatedSpiele = 0;

    for (Entity gs : datastore.prepare(gsQuery).asIterable()) {
      totalCount++;
      if (!gs.hasProperty("spielKey") || gs.getProperty("spielKey") == null) {
        String name = (String) gs.getProperty("name");
        if (name != null && !name.trim().isEmpty()) {
          name = name.trim();
          Key spielKey = nameToKeyMap.get(name);
          if (spielKey == null) {
            Entity newSpiel = new Entity(PersistentSpiel.KIND);
            newSpiel.setProperty("name", name);
            newSpiel.setProperty("creation", new Date());
            datastore.put(newSpiel);
            spielKey = newSpiel.getKey();
            nameToKeyMap.put(name, spielKey);
            newlyCreatedSpiele++;
          }
          gs.setProperty("spielKey", spielKey);
          toUpdate.add(gs);
          updatedCount++;

          if (toUpdate.size() >= 400) {
            datastore.put(toUpdate);
            toUpdate.clear();
          }
        }
      }
    }

    if (!toUpdate.isEmpty()) {
      datastore.put(toUpdate);
    }

    clearPrecalculatedData();

    return "Migration erfolgreich: " + totalCount + " Partien geprüft, " 
        + updatedCount + " mit technischem Key aktualisiert, " 
        + newlyCreatedSpiele + " neue Spiele im Katalog angelegt.";
  }

  public static boolean renameSpiel(String oldName, String newName) {
    if (oldName == null || newName == null || oldName.trim().isEmpty() || newName.trim().isEmpty()) {
      return false;
    }
    oldName = oldName.trim();
    newName = newName.trim();
    if (oldName.equals(newName)) {
      return true;
    }

    DatastoreService datastore = getDatastore();

    // 1. Quell-Spiel(e) finden
    Query oldQuery = new Query(PersistentSpiel.KIND)
        .setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, oldName));
    List<Entity> oldSpiele = new ArrayList<>();
    for (Entity entity : datastore.prepare(oldQuery).asIterable()) {
      oldSpiele.add(entity);
    }
    if (oldSpiele.isEmpty()) {
      return false;
    }

    // 2. Prüfen, ob newName bereits existiert (Merge-Fall)
    Query newQuery = new Query(PersistentSpiel.KIND)
        .setFilter(new Query.FilterPredicate("name", Query.FilterOperator.EQUAL, newName));
    Entity targetSpiel = null;
    for (Entity entity : datastore.prepare(newQuery).asIterable()) {
      targetSpiel = entity;
      break;
    }

    Key targetKey;
    if (targetSpiel != null) {
      // Merge: targetSpiel existiert bereits, alte Einträge löschen
      targetKey = targetSpiel.getKey();
      List<Key> keysToDelete = new ArrayList<>();
      for (Entity oldEntity : oldSpiele) {
        keysToDelete.add(oldEntity.getKey());
      }
      datastore.delete(keysToDelete);
    } else {
      // Normales Umbenennen: ersten alten Eintrag umbenennen, Duplikate löschen
      Entity primaryOld = oldSpiele.get(0);
      primaryOld.setProperty("name", newName);
      datastore.put(primaryOld);
      targetKey = primaryOld.getKey();
      if (oldSpiele.size() > 1) {
        List<Key> dupes = new ArrayList<>();
        for (int i = 1; i < oldSpiele.size(); i++) {
          dupes.add(oldSpiele.get(i).getKey());
        }
        datastore.delete(dupes);
      }
    }

    // 3. Alle PersistentGespieltesSpiel aktualisieren (auf targetKey und newName)
    List<Key> sourceKeys = new ArrayList<>();
    for (Entity oldEntity : oldSpiele) {
      sourceKeys.add(oldEntity.getKey());
    }

    Query gsQuery = new Query(PersistentGespieltesSpiel.KIND);
    List<Entity> gsToUpdate = new ArrayList<>();
    for (Entity gs : datastore.prepare(gsQuery).asIterable()) {
      Key gsSpielKey = (Key) gs.getProperty("spielKey");
      String gsName = (String) gs.getProperty("name");
      boolean matches = (gsSpielKey != null && sourceKeys.contains(gsSpielKey))
          || (oldName.equals(gsName));
      if (matches) {
        gs.setProperty("spielKey", targetKey);
        gs.setProperty("name", newName);
        gsToUpdate.add(gs);
        if (gsToUpdate.size() >= 400) {
          datastore.put(gsToUpdate);
          gsToUpdate.clear();
        }
      }
    }
    if (!gsToUpdate.isEmpty()) {
      datastore.put(gsToUpdate);
    }

    clearPrecalculatedData();
    return true;
  }
}
