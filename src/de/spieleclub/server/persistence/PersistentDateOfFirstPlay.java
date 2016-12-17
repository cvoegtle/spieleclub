package de.spieleclub.server.persistence;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PersistentDateOfFirstPlay {
  
  @PrimaryKey
  private String spielename;
  
  @Persistent
  private Date dateOfPlay;

  public PersistentDateOfFirstPlay(String spielename, Date dateOfFirstPlay) {
    this.spielename = spielename;
    this.dateOfPlay = dateOfFirstPlay;
  }

  public String getSpielename() {
    return spielename;
  }

  public Date getDateOfPlay() {
    return dateOfPlay;
  }
}
