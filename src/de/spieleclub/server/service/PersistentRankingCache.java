package de.spieleclub.server.service;

import de.spieleclub.server.persistence.PersistentDateOfFirstPlay;
import de.spieleclub.server.persistence.PersistentRanking;
import de.spieleclub.server.persistence.SortingOrder;
import de.spieleclub.server.persistence.SpieleabendAccess;
import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spieleabend;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.*;
import java.util.logging.Logger;

public class PersistentRankingCache implements RankingCache {
  PersistenceManager pm;
  private Logger logger = Logger.getLogger(PersistentRankingCache.class.getName());


  public PersistentRankingCache(PersistenceManager pm) {
    this.pm = pm;
  }


  @SuppressWarnings("unchecked")
  @Override
  public Ranking getRanking(Period period) {
    Ranking ranking = new Ranking();

    Query query = createQueryForPersistentRankingByPeriod();
    List<PersistentRanking> persistentRankings = (List<PersistentRanking>) query.execute(period.getLabel());

    if (persistentRankings.isEmpty()) {
      refreshRanking(period);
      persistentRankings = (List<PersistentRanking>) query.execute(period.getLabel());
    }

    if (!persistentRankings.isEmpty()) {
      ranking = persistentRankings.get(0).getRanking();
    }

    return ranking;
  }


  @SuppressWarnings("unchecked")
  @Override
  public Date getDateOfFirstPlay(String spielename) {
    Date firstPlay = null;

    Query query = pm.newQuery(PersistentDateOfFirstPlay.class, "spielename == spielenameParameter");
    query.declareParameters("String spielenameParameter");
    List<PersistentDateOfFirstPlay> firstDates = (List<PersistentDateOfFirstPlay>) query.execute(spielename);
    if (firstDates.isEmpty()) {
      refreshDates();
      firstDates = (List<PersistentDateOfFirstPlay>) query.execute(spielename);
    }

    if (!firstDates.isEmpty()) {
      firstPlay = firstDates.get(0).getDateOfPlay();
    }

    return firstPlay;
  }


  @Override
  public void clear() {
    clearRankings();
    clearDates();
  }

  @SuppressWarnings("unchecked")
  private void clearRankings() {
    logger.info("clearRankings() - ENTRY");
    Query query = pm.newQuery(PersistentRanking.class);
    List<PersistentRanking> rankingsToDelete = (List<PersistentRanking>) query.execute();

    List<PersistentRanking> shortenedList = new ArrayList<>(rankingsToDelete.size());
    for (PersistentRanking ranking : rankingsToDelete) {
      if (isThisOrLastYear(ranking) || isOverallRanking(ranking)) {
        logger.info("clearRankings() - Label: " + ranking.getLabel());
        shortenedList.add(ranking);
      }
    }
    pm.deletePersistentAll(shortenedList);
    logger.info("clearRankings() - EXIT");
  }

  private boolean isOverallRanking(PersistentRanking ranking) {
    return ranking.getLabel().startsWith("Alle");
  }

  private boolean isThisOrLastYear(PersistentRanking ranking) {
    Calendar cal = Calendar.getInstance();
    Integer currentYear = cal.get(Calendar.YEAR);
    Integer previousYear = currentYear-1;
    return (currentYear.toString().equals(ranking.getLabel()) || previousYear.toString().equals(ranking.getLabel()));
  }

  @SuppressWarnings("unchecked")
  private void clearDates() {
    logger.info("clearDates() - ENTRY");
    Query query = pm.newQuery(PersistentDateOfFirstPlay.class);
    List<PersistentDateOfFirstPlay> datesToDelete = (List<PersistentDateOfFirstPlay>) query.execute();

    List<PersistentDateOfFirstPlay> realList = new ArrayList<>(datesToDelete.size());
    for (PersistentDateOfFirstPlay aDatesToDelete : datesToDelete) {
      realList.add(aDatesToDelete);
    }
    pm.deletePersistentAll(realList);
    logger.info("clearDates() - EXIT");
  }

  private void refreshRanking(Period period) {
    SpieleabendAccess spieleabendAccess = new SpieleabendAccess(pm);
    List<Spieleabend> spieleabende = spieleabendAccess.read(period.getStartDate(), period.getEndDate(), SortingOrder.ASCENDING);

    Ranking ranking = new SpieleStatisticCalculator(period.getLabel(), spieleabende).analyse();

    pm.makePersistent(new PersistentRanking(ranking));
  }

  private void refreshDates() {
    SpieleabendAccess spieleabendAccess = new SpieleabendAccess(pm);
    List<Spieleabend> spieleabende = spieleabendAccess.read(null, null, SortingOrder.DESCENDING);

    HashMap<String, PersistentDateOfFirstPlay> firstDates = new HashMap<String, PersistentDateOfFirstPlay>();

    for (Spieleabend spieleabend : spieleabende) {
      for (GespieltesSpiel spiel : (Iterable<GespieltesSpiel>) spieleabend.getGespielteSpiele()) {
        firstDates.put(spiel.getName(), new PersistentDateOfFirstPlay(spiel.getName(), spieleabend.getDate()));
      }
    }

    pm.makePersistentAll(firstDates.values());
  }


  private Query createQueryForPersistentRankingByPeriod() {
    Query query = pm.newQuery(PersistentRanking.class);
    query.setFilter("label == labelOfSelectedPeriod");
    query.declareParameters("String labelOfSelectedPeriod");
    return query;
  }


}
