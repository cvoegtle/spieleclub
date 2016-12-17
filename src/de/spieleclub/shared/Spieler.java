package de.spieleclub.shared;

import java.io.Serializable;
import java.util.Date;

public class Spieler implements Serializable{
  private static final long serialVersionUID = 1L;
  private String websafeKey;
  private String name;
  private String email;
  private Date creation;

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

  public String getWebsafeKey() {
    return websafeKey;
  }

  public void setWebsafeKey(String websafeKey) {
    this.websafeKey = websafeKey;
  }
}
