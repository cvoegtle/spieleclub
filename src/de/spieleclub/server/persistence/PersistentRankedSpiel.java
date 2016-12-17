package de.spieleclub.server.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

import de.spieleclub.shared.RankedSpiel;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PersistentRankedSpiel {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;

  @Persistent
  private int rank;
  
  @Persistent
  private String formattedRank = "";
  
  @Persistent
  private String name = "";
  
  @Persistent
  int count = 0;

  public PersistentRankedSpiel(RankedSpiel rankedSpiel) {
    rank = rankedSpiel.getRank();
    formattedRank = rankedSpiel.getFormattedRank();
    name = rankedSpiel.getName();
    count = rankedSpiel.getCount();
  }
  
  public RankedSpiel getRankedSpiel() {
    RankedSpiel rankedSpiel = new RankedSpiel();
    rankedSpiel.setRank(rank);
    rankedSpiel.setFormattedRank(formattedRank);
    rankedSpiel.setName(name);
    rankedSpiel.setCount(count);
    
    return rankedSpiel;
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

}
