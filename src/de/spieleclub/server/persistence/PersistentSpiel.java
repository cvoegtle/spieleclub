package de.spieleclub.server.persistence;

import java.util.Date;
import java.util.Iterator;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import de.spieleclub.shared.Spiel;

public class PersistentSpiel {
  public static final String KIND = "PersistentSpiel";

  private Key key;
  private String name;
  private Date creation;
  private PersistentSpieler creator;

  public PersistentSpiel(Spiel spiel) {
    this.setName(spiel.getName());
    this.setCreation(spiel.getCreation());
    if (spiel.getCreator() != null) {
      this.setCreator(new PersistentSpieler(spiel.getCreator()));
    }
    if (spiel.getWebsafeKey() != null) {
      this.setKey(KeyFactory.stringToKey(spiel.getWebsafeKey()));
    }
  }

  public PersistentSpiel(Entity entity, DatastoreService datastore) {
    this.key = entity.getKey();
    this.name = (String) entity.getProperty("name");
    this.creation = (Date) entity.getProperty("creation");

    if (datastore != null && this.key != null) {
      Query q = new Query(PersistentSpieler.KIND).setAncestor(this.key);
      Iterator<Entity> it = datastore.prepare(q).asIterator();
      if (it.hasNext()) {
        this.creator = new PersistentSpieler(it.next());
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
    entity.setProperty("name", name);
    entity.setProperty("creation", creation);
    return entity;
  }

  public Spiel getSpiel() {
    Spiel spiel = new Spiel();
    spiel.setCreation(this.creation);
    if (this.getCreator() != null) {
      spiel.setCreator(this.getCreator().getSpieler());
    }
    spiel.setName(this.getName());
    if (key != null) {
      spiel.setWebsafeKey(KeyFactory.keyToString(key));
    }
    return spiel;
  }
  
  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
}
