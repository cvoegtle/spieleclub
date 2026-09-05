package de.spieleclub.server.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import de.spieleclub.server.persistence.PersistentDateOfFirstPlay;
import de.spieleclub.server.persistence.PersistentRankedSpiel;
import de.spieleclub.server.persistence.PersistentRanking;
import de.spieleclub.server.persistence.SortingOrder;
import de.spieleclub.server.persistence.SpieleabendAccess;
import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.RankedSpiel;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spieleabend;

public class PersistentRankingCache implements RankingCache {
  private DatastoreService datastore;
  private Logger logger = Logger.getLogger(PersistentRankingCache.class.getName());

  public PersistentRankingCache(DatastoreService datastore) {
    this.datastore = datastore;
  }

  @Override
  public Ranking getRanking(Period period) {
    Ranking ranking = new Ranking();
    Key key = KeyFactory.createKey(PersistentRanking.KIND, period.getLabel());

    Entity entity = null;
    try {
      entity = datastore.get(key);
    } catch (EntityNotFoundException e) {
      refreshRanking(period);
      try {
        entity = datastore.get(key);
      } catch (EntityNotFoundException ex) {
        logger.warning("Could not load ranking after refresh for: " + period.getLabel());
      }
    }

    if (entity != null) {
      PersistentRanking persistentRanking = new PersistentRanking(entity, datastore);
      ranking = persistentRanking.getRanking();
    }

    return ranking;
  }

  @Override
  public Date getDateOfFirstPlay(String spielename) {
    Date firstPlay = null;
    Key key = KeyFactory.createKey(PersistentDateOfFirstPlay.KIND, spielename);

    Entity entity = null;
    try {
      entity = datastore.get(key);
    } catch (EntityNotFoundException e) {
      refreshDates();
      try {
        entity = datastore.get(key);
      } catch (EntityNotFoundException ex) {
        logger.warning("Could not load date of first play after refresh for: " + spielename);
      }
    }

    if (entity != null) {
      firstPlay = (Date) entity.getProperty("dateOfPlay");
    }

    return firstPlay;
  }

  @Override
  public void clear() {
    clearRankings();
    clearDates();
  }

  private void clearRankings() {
    logger.info("clearRankings() - ENTRY");
    Query query = new Query(PersistentRanking.KIND);
    List<Key> rankingsToDelete = new ArrayList<>();
    List<Key> childrenToDelete = new ArrayList<>();

    for (Entity entity : datastore.prepare(query).asIterable()) {
      String label = entity.getKey().getName();
      if (label == null) {
        label = (String) entity.getProperty("label");
      }
      if (label != null && (isThisOrLastYear(label) || isOverallRanking(label))) {
        logger.info("clearRankings() - Label: " + label);
        rankingsToDelete.add(entity.getKey());

        Query childQuery = new Query(PersistentRankedSpiel.KIND)
            .setAncestor(entity.getKey())
            .setKeysOnly();
        for (Entity child : datastore.prepare(childQuery).asIterable()) {
          childrenToDelete.add(child.getKey());
        }
      }
    }

    if (!childrenToDelete.isEmpty()) {
      datastore.delete(childrenToDelete);
    }
    if (!rankingsToDelete.isEmpty()) {
      datastore.delete(rankingsToDelete);
    }
    logger.info("clearRankings() - EXIT");
  }

  private boolean isOverallRanking(String label) {
    return label != null && label.startsWith("Alle");
  }

  private boolean isThisOrLastYear(String label) {
    if (label == null) return false;
    Calendar cal = Calendar.getInstance();
    Integer currentYear = cal.get(Calendar.YEAR);
    Integer previousYear = currentYear - 1;
    return currentYear.toString().equals(label) || previousYear.toString().equals(label);
  }

  private void clearDates() {
    logger.info("clearDates() - ENTRY");
    Query query = new Query(PersistentDateOfFirstPlay.KIND).setKeysOnly();
    List<Key> datesToDelete = new ArrayList<>();
    for (Entity entity : datastore.prepare(query).asIterable()) {
      datesToDelete.add(entity.getKey());
    }
    if (!datesToDelete.isEmpty()) {
      datastore.delete(datesToDelete);
    }
    logger.info("clearDates() - EXIT");
  }

  private void refreshRanking(Period period) {
    SpieleabendAccess spieleabendAccess = new SpieleabendAccess(datastore);
    List<Spieleabend> spieleabende = spieleabendAccess.read(period.getStartDate(), period.getEndDate(), SortingOrder.ASCENDING);

    Ranking ranking = new SpieleStatisticCalculator(period.getLabel(), spieleabende).analyse();
    PersistentRanking persistentRanking = new PersistentRanking(ranking);

    Entity rankingEntity = persistentRanking.toEntity();
    datastore.put(rankingEntity);

    List<Entity> children = new ArrayList<>();
    int idx = 0;
    for (PersistentRankedSpiel prs : persistentRanking.getRankedSpiele()) {
      children.add(prs.toEntity(rankingEntity.getKey(), idx++));
    }
    if (!children.isEmpty()) {
      datastore.put(children);
    }
  }

  private void refreshDates() {
    SpieleabendAccess spieleabendAccess = new SpieleabendAccess(datastore);
    List<Spieleabend> spieleabende = spieleabendAccess.read(null, null, SortingOrder.DESCENDING);

    HashMap<String, PersistentDateOfFirstPlay> firstDates = new HashMap<>();

    for (Spieleabend spieleabend : spieleabende) {
      if (spieleabend.getGespielteSpiele() != null) {
        for (GespieltesSpiel spiel : (Iterable<GespieltesSpiel>) spieleabend.getGespielteSpiele()) {
          firstDates.put(spiel.getName(), new PersistentDateOfFirstPlay(spiel.getName(), spieleabend.getDate()));
        }
      }
    }

    List<Entity> entitiesToPut = new ArrayList<>();
    for (PersistentDateOfFirstPlay dateEntity : firstDates.values()) {
      entitiesToPut.add(dateEntity.toEntity());
    }
    if (!entitiesToPut.isEmpty()) {
      datastore.put(entitiesToPut);
    }
  }
}
