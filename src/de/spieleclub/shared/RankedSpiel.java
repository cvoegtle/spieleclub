package de.spieleclub.shared;

import java.io.Serializable;

import de.spieleclub.shared.helper.ColumnAccess;

public class RankedSpiel implements Serializable, ColumnAccess {
  private static final long serialVersionUID = 1L;
  
  private int rank;
  private String formattedRank = "";
  private String name = "";
  int count = 0;
  
  public RankedSpiel(int rank, GespieltesSpiel spiel) {
    this.rank = rank;
    this.name = spiel.getName();
    this.count = spiel.getCount();
  }

  public RankedSpiel() {    
  }
  
  /**
   * 
   * @param compareTo
   * @return 1: this > compareTo
   *         0: this == compareTo
   *        -1: this < compareTo 
   */
  public int compareByCountAndName(GespieltesSpiel compareTo) {
    if (this.getCount() >  compareTo.getCount()) {
      return 1;
    }
    
    if (this.getCount() ==  compareTo.getCount()) {
      // inverse alphabetical order: A is bigger than B
      return -1 * this.getName().compareTo(compareTo.getName());
    }
    
    return -1;
  }


  @Override
  public String getValueFor(int column) {
    if (column == 0) {
      return formattedRank;
    } else if (column == 1) {
      return name;
    } else if (column == 2) {
      return Integer.toString(count);
    } 
    return "";
  }
  
  public String getName() {
    return name;
  }
  
  public int getCount() {
    return count;
  }
  
  public int getRank() {
    return rank;
  }

  public void setRank(int i) {
    this.rank = i;
  }

  public void formatRanking(int rankOfPreviousSpiel) {
    if (rankOfPreviousSpiel != rank) {
      formattedRank = Integer.toString(rank) + ".";
    }
  }

  public String getFormattedRank() {
    return formattedRank;
  }

  public void setFormattedRank(String formattedRank) {
    this.formattedRank = formattedRank;    
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
