package de.spieleclub.shared;

import java.io.Serializable;
import java.util.Date;

public class Spiel implements Serializable {
  private static final long serialVersionUID = 1L;

  private String websafeKey;
  private String name;
  private Date creation;
  private Spieler creator;

  
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

  public Spieler getCreator() {
    return creator;
  }

  public void setCreator(Spieler creator) {
    this.creator = creator;
  }

  public String getWebsafeKey() {
    return websafeKey;
  }

  public void setWebsafeKey(String websafeKey) {
    this.websafeKey = websafeKey;
  }
}
