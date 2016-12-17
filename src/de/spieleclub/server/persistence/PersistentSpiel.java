package de.spieleclub.server.persistence;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.shared.Spiel;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PersistentSpiel {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;

  @Persistent
  private String name;
  
  @Persistent
  private Date creation;
  
  @Persistent
  private PersistentSpieler creator;

  public Spiel getSpiel() {
    Spiel spiel = new Spiel();
    spiel.setCreation(this.creation);
    if (this.getCreator() != null) {
      spiel.setCreator(this.getCreator().getSpieler());
    }
    spiel.setName(this.getName());
    spiel.setWebsafeKey(KeyFactory.keyToString(key));
    return spiel;
  }
  
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
