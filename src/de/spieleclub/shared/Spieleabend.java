package de.spieleclub.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Spieleabend implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;
  private String websafeKey;
  private Date date = new Date();
  private String description;
  private Date creation = new Date();
  private Spieler creator;
  private List<GespieltesSpiel> gespielteSpiele = new ArrayList<GespieltesSpiel>();

  
  public Spieleabend() {    
  }
  
  public Spieleabend(Spieler creator) {
    this.creator = creator;
  }
  
  
  public boolean after(Date when) {
    return this.date.after(when);
  }
  
  
  public boolean sameDate(Date when) {
    return this.date.equals(when);
  }

  public boolean hasSpielBeenPlayed(String spieleName) {
    Iterator<GespieltesSpiel> it = gespielteSpiele.iterator();
    while (it.hasNext()) {
      if (it.next().getName().equals(spieleName) ) {
        return true;
      }
    }
    return false;
  }


  public void add(Spiel spiel, String addInfo) {
    GespieltesSpiel newSpiel = new GespieltesSpiel(spiel.getName(), addInfo);
    // pr�fen, ob wir das Spiel schon in der Liste haben, dann nur count erh�hen
    Iterator<GespieltesSpiel> it = gespielteSpiele.iterator();
    while (it.hasNext()) {
      GespieltesSpiel gs = it.next();
      if (gs.compareTo(newSpiel) == 0) {
        gs.incrementCount();
        return;
      }
    }
    
    gespielteSpiele.add(newSpiel);
  }

  public void add(GespieltesSpiel gs) {
    gespielteSpiele.add(gs);
  }

  @SuppressWarnings("rawtypes")
  public List getGespielteSpiele() {
    return gespielteSpiele;
  }

  public String getWebsafeKey() {
    return websafeKey;
  }

  public void setWebsafeKey(String websafeKey) {
    this.websafeKey = websafeKey;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreation() {
    return creation;
  }

  public void setCreation(Date creation) {
    this.creation = creation;
  }

  public Spieler getCreator() {
    return creator;
  }

  public void setCreator(Spieler creator) {
    this.creator = creator;
  }
  
  public Spieleabend clone() {
    Spieleabend clonedSpieleabend = new Spieleabend();
    clonedSpieleabend.websafeKey = this.websafeKey;
    clonedSpieleabend.date = this.date;
    clonedSpieleabend.description = this.description;
    clonedSpieleabend.creation = this.creation;
    clonedSpieleabend.creator = this.creator;

    for (GespieltesSpiel aGespielteSpiele : gespielteSpiele) {
      clonedSpieleabend.gespielteSpiele.add(aGespielteSpiele.clone());
    }
    
    return clonedSpieleabend;
  }
}
