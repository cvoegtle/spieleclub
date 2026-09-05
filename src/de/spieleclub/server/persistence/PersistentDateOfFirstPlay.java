package de.spieleclub.server.persistence;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PersistentDateOfFirstPlay {
  public static final String KIND = "PersistentDateOfFirstPlay";
  
  private String spielename;
  private Date dateOfPlay;

  public PersistentDateOfFirstPlay(String spielename, Date dateOfFirstPlay) {
    this.spielename = spielename;
    this.dateOfPlay = dateOfFirstPlay;
  }

  public PersistentDateOfFirstPlay(Entity entity) {
    this.spielename = (entity.getKey().getName() != null) ? entity.getKey().getName() : (String) entity.getProperty("spielename");
    this.dateOfPlay = (Date) entity.getProperty("dateOfPlay");
  }

  public Entity toEntity() {
    Key key = KeyFactory.createKey(KIND, spielename);
    Entity entity = new Entity(key);
    entity.setProperty("spielename", spielename);
    entity.setProperty("dateOfPlay", dateOfPlay);
    return entity;
  }

  public String getSpielename() {
    return spielename;
  }

  public Date getDateOfPlay() {
    return dateOfPlay;
  }
}
