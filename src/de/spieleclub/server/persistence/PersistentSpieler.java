package de.spieleclub.server.persistence;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.shared.Spieler;

public class PersistentSpieler {
  public static final String KIND = "PersistentSpieler";

  private Key key;
  private String name;
  private String email;
  private Date creation;

  public PersistentSpieler(String email, String name) {
    this.creation = new Date();
    this.email = email;
    this.name = name;
  }
  
  public PersistentSpieler(Spieler spieler) {
    if (spieler.getWebsafeKey() != null) {
      this.key = KeyFactory.stringToKey(spieler.getWebsafeKey());
    }
    this.setCreation(spieler.getCreation());
    this.setEmail(spieler.getEmail());
    this.setName(spieler.getName());
  }

  public PersistentSpieler(Entity entity) {
    this.key = entity.getKey();
    this.name = (String) entity.getProperty("name");
    this.email = (String) entity.getProperty("email");
    this.creation = (Date) entity.getProperty("creation");
  }

  public Entity toEntity() {
    return toEntity(null);
  }

  public Entity toEntity(Key parentKey) {
    Entity entity;
    if (key != null) {
      entity = new Entity(key);
    } else if (parentKey != null) {
      entity = new Entity(KIND, parentKey);
    } else {
      entity = new Entity(KIND);
    }
    entity.setProperty("name", name);
    entity.setProperty("email", email);
    entity.setProperty("creation", creation);
    return entity;
  }
  
  public Spieler getSpieler() {
    Spieler spieler = new Spieler();
    if (key != null) {
      spieler.setWebsafeKey(KeyFactory.keyToString(key));
    }
    spieler.setName(name);
    spieler.setEmail(email);
    spieler.setCreation(creation);
    return spieler;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreation() {
    return creation;
  }

  public void setCreation(Date creation) {
    this.creation = creation;
  }
}
