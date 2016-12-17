package de.spieleclub.server.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.spieleclub.server.persistence.SortingOrder;
import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.RankedSpiel;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spieleabend;

public class SpieleStatisticCalculator {
  List<Spieleabend> spieleabende;
  String nameOfPeriod = "";
  
  public SpieleStatisticCalculator(String nameOfPeriod, List<Spieleabend> spieleabende) {
    this.nameOfPeriod = nameOfPeriod;
    this.spieleabende = spieleabende;
  }

  public RankedSpiel getRanking(String spieleName) {
    Ranking ranking = analyse();
    return ranking.getSpielByName(spieleName);
  }
  
  public Ranking analyse() {
    HashMap<String, GespieltesSpiel> gespielteSpiele = countHowManyTimesPlayed();
    return new Ranking(nameOfPeriod, gespielteSpiele);
  }
  

  /**
   * Assumption: The list is ordered in on or the other direction
   * @return
   */
  public Spieleabend getFirstSpieleabend() {
    Spieleabend firstSpieleabend = new Spieleabend();

    if (spieleabende.size() > 0) {
      if (determinSortingOrder() == SortingOrder.ASCENDING) {
        firstSpieleabend = spieleabende.get(0);
      } else {
        firstSpieleabend = spieleabende.get(spieleabende.size()-1);
      }
    }
    return firstSpieleabend;
  }
  
  public Spieleabend getFirstSpieleabend(String spieleName) {
    Spieleabend result = new Spieleabend();
    
    if (determinSortingOrder() == SortingOrder.ASCENDING) {
      Iterator<Spieleabend> it = spieleabende.iterator();
      while (it.hasNext()) {
        Spieleabend spieleabend = it.next();
        if (spieleabend.hasSpielBeenPlayed(spieleName)) {
          result = spieleabend;
          break;
        }
      }
      
    } else {
      for (int i = spieleabende.size()-1; i >= 0; i++) {
        Spieleabend spieleabend = spieleabende.get(i);
        if (spieleabend.hasSpielBeenPlayed(spieleName)) {
          result = spieleabend;
          break;
        }
      }
    }
    
    return result;
  }
  
  @SuppressWarnings("unchecked")
  private HashMap<String, GespieltesSpiel> countHowManyTimesPlayed() {
    HashMap<String, GespieltesSpiel> gespielteSpiele= new HashMap<String, GespieltesSpiel>();
    
    Iterator<Spieleabend> it = spieleabende.iterator();
    while (it.hasNext()) {
      Spieleabend spieleabend = it.next();
      Iterator<GespieltesSpiel> spieleIt = spieleabend.getGespielteSpiele().iterator();
      while (spieleIt.hasNext()) {
        GespieltesSpiel spiel = spieleIt.next();
        GespieltesSpiel knownSpiel = gespielteSpiele.get(spiel.getName());
        if (knownSpiel == null) {
          knownSpiel = spiel.clone();
          gespielteSpiele.put(knownSpiel.getName(), knownSpiel);
        } else {
          knownSpiel.incrementCount(spiel.getCount());
        }
      }
    }

    return gespielteSpiele;
  }

  private SortingOrder determinSortingOrder() {
    SortingOrder order = SortingOrder.ASCENDING;
    if (spieleabende.size() > 0) {
      Spieleabend firstSpieleabendInTheList = spieleabende.get(0);
      Spieleabend lastSpieleabendInTheList = spieleabende.get(spieleabende.size()-1);
      if (firstSpieleabendInTheList.getDate().before(lastSpieleabendInTheList.getDate())) {
        order = SortingOrder.ASCENDING;
      } else {
        order = SortingOrder.DESCENDING;
      }
    }
    return order;
  }
}
