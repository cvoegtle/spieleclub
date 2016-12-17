package de.spieleclub.server.persistence;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.shared.Spieler;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PersistentSpieler {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;

  @Persistent
  private String name;
  
  @Persistent
  private String email;
  
  @Persistent
  private Date creation;

  public PersistentSpieler(String email, String name) {
    creation = new Date();
    this.email = email;
    this.name = name;
  }
  
  public PersistentSpieler(Spieler spieler) {
    if (spieler.getWebsafeKey() != null) {
      key = KeyFactory.stringToKey(spieler.getWebsafeKey());
    }
    this.setCreation(spieler.getCreation());
    this.setEmail(spieler.getEmail());
    this.setName(spieler.getName());
  }
  
  public Spieler getSpieler() {
    Spieler spieler = new Spieler();
    
    spieler.setWebsafeKey(KeyFactory.keyToString(key));
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
