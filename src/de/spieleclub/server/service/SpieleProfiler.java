package de.spieleclub.server.service;

import java.util.Date;
import java.util.Iterator;

import javax.jdo.PersistenceManager;

import de.spieleclub.shared.AvailablePeriods;
import de.spieleclub.shared.AvailablePeriodsFactory;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.RankedSpiel;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.SpieleProfile;
import de.spieleclub.shared.YearlyRank;

public class SpieleProfiler {
  private SpieleProfile spieleProfile;
  private RankingCache rankingCache;
  
  public SpieleProfiler(PersistenceManager pm, String spieleName) {
    RankingCacheFactory rankingCacheFactory = new RankingCacheFactory(pm);
    rankingCache = rankingCacheFactory.getRankingCache();
    spieleProfile = new SpieleProfile(spieleName);
  }
  
  
  public SpieleProfile getSpieleProfile() {
    return spieleProfile;
  }

  
  public void generateProfile() {
    Date firstPlayedOn = rankingCache.getDateOfFirstPlay(spieleProfile.getName());
    spieleProfile.setFirstTimePlayed(firstPlayedOn);
    
    addRankingToProfile();
  }
  
  
  private void addRankingToProfile() {
    AvailablePeriods availablePeriods = AvailablePeriodsFactory.makeAvailablePeriods(spieleProfile.getFirstTimePlayed());
    Period overallPeriod = availablePeriods.getOverallPeriod();
    Ranking ranking = rankingCache.getRanking(overallPeriod);
    
    RankedSpiel overallRank = ranking.getSpielByName(spieleProfile.getName());
    spieleProfile.setAllTimeRanking(overallRank.getRank());
    spieleProfile.setTimesPlayed(overallRank.getCount());
    
    addYearlyRankingToProfile(spieleProfile.getFirstTimePlayed());
  }

  
  private void addYearlyRankingToProfile(Date startingDate) {
    AvailablePeriods availablePeriods = AvailablePeriodsFactory.makeAvailablePeriods(startingDate);
    availablePeriods.removeOtherThanYearlyPeriods();
    
    Iterator<Period> it = availablePeriods.iterator();
    while (it.hasNext()) {
      addRankingForYear(it.next());
    }   
  }

  
  private void addRankingForYear(Period period) {
    RankedSpiel ranking = rankingCache.getRanking(period).getSpielByName(spieleProfile.getName());
    
    if (ranking != null) {
      spieleProfile.addYearlyRank(new YearlyRank(period.getYear(), ranking.getRank(), ranking.getCount()));      
    } else {
      spieleProfile.addYearlyRank(new YearlyRank(period.getYear()));
    }
  }
}
