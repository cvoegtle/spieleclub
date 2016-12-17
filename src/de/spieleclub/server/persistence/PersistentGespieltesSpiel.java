package de.spieleclub.server.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.shared.GespieltesSpiel;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PersistentGespieltesSpiel implements Comparable<PersistentGespieltesSpiel> {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  private Key key;

  @Persistent
  private String name;
  
  @Persistent
  private String zusatz;
  
  @Persistent
  private int count;
  
  public PersistentGespieltesSpiel(GespieltesSpiel gespieltesSpiel) {
    if (gespieltesSpiel.getWebsafeKey() != null) {
      key = KeyFactory.stringToKey(gespieltesSpiel.getWebsafeKey());
    }
    name = gespieltesSpiel.getName();
    zusatz = gespieltesSpiel.getZusatz();
    count = gespieltesSpiel.getCount();
  }
  
  public PersistentGespieltesSpiel(String name) {
    this.name = name;
  }
  
  public GespieltesSpiel getGespieltesSpiel() {
    GespieltesSpiel gs = new GespieltesSpiel();
    
    gs.setWebsafeKey(KeyFactory.keyToString(key));
    gs.setName(name);
    gs.setZusatz(zusatz);
    gs.setCount(count);
    
    return gs;
  }
  
  boolean equals(PersistentGespieltesSpiel compareTo) {
    if (name == null || compareTo == null) {
      return false;
    }
    return name.equals(compareTo.name);
  }

  boolean equals(String compareTo) {
    if (name == null || compareTo == null) {
      return false;
    }
    return name.equals(compareTo);
  }

  @Override
  public int compareTo(PersistentGespieltesSpiel o) {
    return name.compareTo(o.name);
  }

  public boolean hasName(String spieleName) {
    return name.equals(spieleName);
  }

}
