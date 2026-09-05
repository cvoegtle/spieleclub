package de.spieleclub.server.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.Spieleabend;

public class PersistentSpieleabend {
  public static final String KIND = "PersistentSpieleabend";
  
  private Key key;
  private Date date;
  private String description;
  private Date creation;
  private PersistentSpieler creator;
  private ArrayList<PersistentGespieltesSpiel> gespielteSpiele = new ArrayList<>();

  public PersistentSpieleabend(Spieleabend spieleabend) {
    if (spieleabend.getWebsafeKey() != null) {
      setKey(KeyFactory.stringToKey(spieleabend.getWebsafeKey()));
    }
    setDate(spieleabend.getDate());
    setDescription(spieleabend.getDescription());
    setCreation(spieleabend.getCreation());
    
    if (spieleabend.getGespielteSpiele() != null) {
      for (Object gs : spieleabend.getGespielteSpiele()) {
        gespielteSpiele.add(new PersistentGespieltesSpiel((GespieltesSpiel) gs));
      }
    }
  }

  public PersistentSpieleabend(Entity entity, DatastoreService datastore) {
    this.key = entity.getKey();
    this.date = (Date) entity.getProperty("date");
    this.description = (String) entity.getProperty("description");
    this.creation = (Date) entity.getProperty("creation");

    if (datastore != null && this.key != null) {
      Query creatorQuery = new Query(PersistentSpieler.KIND).setAncestor(this.key);
      Iterator<Entity> it = datastore.prepare(creatorQuery).asIterator();
      if (it.hasNext()) {
        this.creator = new PersistentSpieler(it.next());
      }

      Query gamesQuery = new Query(PersistentGespieltesSpiel.KIND)
          .setAncestor(this.key)
          .addSort("gespielteSpiele_INTEGER_IDX", Query.SortDirection.ASCENDING);
      for (Entity child : datastore.prepare(gamesQuery).asIterable()) {
        this.gespielteSpiele.add(new PersistentGespieltesSpiel(child));
      }
    }
  }

  public Entity toEntity() {
    Entity entity;
    if (key != null) {
      entity = new Entity(key);
    } else {
      entity = new Entity(KIND);
    }
    entity.setProperty("date", date);
    entity.setProperty("description", description);
    entity.setProperty("creation", creation != null ? creation : new Date());
    return entity;
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
      for (PersistentGespieltesSpiel gs : gespielteSpiele) {
        spieleabend.add(gs.getGespieltesSpiel());
      }
    }
    if (key != null) {
      spieleabend.setWebsafeKey(KeyFactory.keyToString(key));
    }
    
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
    for (PersistentGespieltesSpiel spiel : gespielteSpiele) {
      if (spiel.hasName(spieleName)) {
        return true;
      }
    }
    return false;
  }
}
