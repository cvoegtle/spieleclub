package de.spieleclub.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.spieleclub.shared.helper.ColumnAccess;

public class Ranking implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private String label = "";
  private LinkedList<RankedSpiel> ranking = new LinkedList<RankedSpiel>();
  
  public Ranking(String label, HashMap<String, GespieltesSpiel> gespielteSpiele) {
    this.label = label;
    orderSpieleByCountAndName(gespielteSpiele);
    rankSpieleWithSameCountEqual();
    formatRankAsEmptyWhereEqualsPreviousRank();
  }

  
  public Ranking() {
  }

  
  public Ranking(String label, LinkedList<RankedSpiel> rankedSpiele) {
    this.label = label;
    this.ranking = rankedSpiele;
  }


  public RankedSpiel getSpielByName(String spieleName) {
    Iterator<RankedSpiel> it = ranking.iterator();
    while (it.hasNext()) {
      RankedSpiel spiel = it.next();
      if (spiel.getName().equals(spieleName)) {
        return spiel;
      }
    }
    return null;
  }

  
  private void orderSpieleByCountAndName(HashMap<String, GespieltesSpiel> spiele) {
    Iterator<GespieltesSpiel> it = spiele.values().iterator();
    while (it.hasNext()) {
      GespieltesSpiel spiel = it.next();
      int i = 0;
      for (i = 0; i < ranking.size(); i++) {
        if (ranking.get(i).compareByCountAndName(spiel) < 0) {
          break;
        }
      }
      ranking.add(i, new RankedSpiel(0, spiel));
    }
  }

  
  private void rankSpieleWithSameCountEqual() {    
    Iterator<RankedSpiel> it = ranking.iterator();
    
    if (!it.hasNext()) {
      return;
    }
    
    RankedSpiel previousSpiel = it.next();
    previousSpiel.setRank(1);
    int currentRank = 1;
    int nextRank = 2;
   
    
    while (it.hasNext()) {
      RankedSpiel spiel = it.next();
      if (spiel.getCount() == previousSpiel.getCount()) {
        spiel.setRank(currentRank);
      } else {
        spiel.setRank(nextRank);
        currentRank = nextRank;
      }
      nextRank++;
      previousSpiel = spiel;
    }
  }

  private void formatRankAsEmptyWhereEqualsPreviousRank() {
    Iterator<RankedSpiel> it = ranking.iterator();
    if (!it.hasNext()) {
      return;
    }
    
    RankedSpiel previousSpiel = it.next();
    previousSpiel.formatRanking(-1);
    
    while (it.hasNext()) {
      RankedSpiel spiel = it.next();
      spiel.formatRanking(previousSpiel.getRank());
      previousSpiel = spiel;
    }
  }

  /**
   * Circumvent the compiler problem that we cannot cast directly from
   * List<RankedSpiel> to List<ColumnAccess>
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<ColumnAccess> getColumnAccessList() {
    return (List<ColumnAccess>)getUntypedList();
  }
  
  @SuppressWarnings("rawtypes")
  private List getUntypedList() {
    return ranking;
  }


  public String getLabel() {
    return label;
  }


  public Iterator<RankedSpiel> iterator() {
    return ranking.iterator();
  }
}
