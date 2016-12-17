package de.spieleclub.shared;

import java.io.Serializable;

public class YearlyRank implements Serializable {
  private static final long serialVersionUID = 1L;

  private int year;
  private Integer rank = null;
  private Integer timesPlayed = null;

  public YearlyRank() {    
  }
  
  public YearlyRank(int year, Integer rank, Integer timesPlayed) {
    this.year = year;
    this.rank = rank;
    this.timesPlayed = timesPlayed;
  }

  public YearlyRank(int year) {
    this.year = year;
  }

  public int getYear() {
    return year;
  }

  public Integer getRank() {
    return rank;
  }

  public Integer getTimesPlayed() {
    return timesPlayed;
  }

  public boolean hasBeenPlayedThisYear() {
    return rank != null;
  }
}
