package de.spieleclub.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SpieleProfile implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String name;
  private Date firstTimePlayed;
  private int allTimeRanking;
  private int timesPlayed;
  private List<YearlyRank> yearlyRanking = new ArrayList<YearlyRank>();

  
  public SpieleProfile() {    
  }

  public SpieleProfile(String spieleName) {
    this.name = spieleName;
  }

  public String getName() {
    return name;
  }

  public void setFirstTimePlayed(Date firstTimePlayed) {
    this.firstTimePlayed = firstTimePlayed;
  }

  public void setAllTimeRanking(int allTimeRanking) {
    this.allTimeRanking = allTimeRanking;
  }

  public void setTimesPlayed(int timesPlayed) {
    this.timesPlayed = timesPlayed;
  }

  public Date getFirstTimePlayed() {
    return firstTimePlayed;
  }

  public int getAllTimeRanking() {
    return allTimeRanking;
  }

  public int getTimesPlayed() {
    return timesPlayed;
  }

  public List<YearlyRank> getYearlyRanking() {
    return yearlyRanking;
  }

  public void addYearlyRank(YearlyRank yearStatistic) {
    yearlyRanking.add(yearStatistic);
  }

  public Iterator<YearlyRank> iterator() {
    return yearlyRanking.iterator();
  }
  
  public boolean isEmpty() {
    return firstTimePlayed == null;
  }
}
