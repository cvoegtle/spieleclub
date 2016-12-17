package de.spieleclub.server.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.Spieleabend;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PersistentSpieleabend {
  
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;

  @Persistent
  private Date date;
  
  @Persistent
  private String description;
  
  @Persistent
  private Date creation;
  
  @Persistent
  private PersistentSpieler creator;
  
  @Persistent
  private ArrayList<PersistentGespieltesSpiel> gespielteSpiele = new ArrayList<PersistentGespieltesSpiel>();

  public PersistentSpieleabend(Spieleabend spieleabend) {
    if (spieleabend.getWebsafeKey() != null) {
      setKey(KeyFactory.stringToKey(spieleabend.getWebsafeKey()));
    }
    setDate(spieleabend.getDate());
    setDescription(spieleabend.getDescription());
    setCreation(spieleabend.getCreation());
    
    @SuppressWarnings("unchecked")
    Iterator<GespieltesSpiel> it = spieleabend.getGespielteSpiele().iterator();
    while (it.hasNext()) {
      gespielteSpiele.add(new PersistentGespieltesSpiel(it.next()));
    }
  }
  
  public Spieleabend getSpieleabend() {
    Spieleabend spieleabend = new Spieleabend();
    
    spieleabend.setCreation(this.getCreation());
    if (this.getCreator() != null) {
      spieleabend.setCreator(this.getCreator().getSpieler());
    }
    spieleabend.setDate(this.getDate());
    spieleabend.setDescription(this.getDescription());
    
    if (gespielteSpiele != null) {
      Iterator<PersistentGespieltesSpiel> it = gespielteSpiele.iterator();
      while (it.hasNext()) {
        spieleabend.add(it.next().getGespieltesSpiel());
      }
    }
    spieleabend.setWebsafeKey(KeyFactory.keyToString(key));
    
    return spieleabend;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
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

  public PersistentSpieler getCreator() {
    return creator;
  }

  public void setCreator(PersistentSpieler creator) {
    this.creator = creator;
  }

  public ArrayList<PersistentGespieltesSpiel> getGespielteSpiele() {
    return gespielteSpiele;
  }

  public void setGespielteSpiele(ArrayList<PersistentGespieltesSpiel> gespielteSpiele) {
    this.gespielteSpiele = gespielteSpiele;
  }

  public boolean hasSpielBeenPlayed(String spieleName) {
    Iterator<PersistentGespieltesSpiel> it = gespielteSpiele.iterator();
    while (it.hasNext()) {
      PersistentGespieltesSpiel spiel = it.next();
      if (spiel.hasName(spieleName)) {
        return true;
      }
    }
    return false;
  }
}
