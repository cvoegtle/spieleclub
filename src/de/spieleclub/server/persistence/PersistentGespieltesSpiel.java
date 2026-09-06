package de.spieleclub.server.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.shared.GespieltesSpiel;

public class PersistentGespieltesSpiel implements Comparable<PersistentGespieltesSpiel> {
  public static final String KIND = "PersistentGespieltesSpiel";

  private Key key;
  private Key spielKey;
  private String name;
  private String zusatz;
  private int count;
  
  public PersistentGespieltesSpiel(GespieltesSpiel gespieltesSpiel) {
    if (gespieltesSpiel.getWebsafeKey() != null) {
      key = KeyFactory.stringToKey(gespieltesSpiel.getWebsafeKey());
    }
    if (gespieltesSpiel.getSpielWebsafeKey() != null) {
      spielKey = KeyFactory.stringToKey(gespieltesSpiel.getSpielWebsafeKey());
    }
    name = gespieltesSpiel.getName();
    zusatz = gespieltesSpiel.getZusatz();
    count = gespieltesSpiel.getCount();
  }
  
  public PersistentGespieltesSpiel(String name) {
    this.name = name;
  }

  public PersistentGespieltesSpiel(Entity entity) {
    this.key = entity.getKey();
    this.name = (String) entity.getProperty("name");
    this.zusatz = (String) entity.getProperty("zusatz");
    Object countVal = entity.getProperty("count");
    this.count = countVal != null ? ((Number) countVal).intValue() : 0;
    this.spielKey = (Key) entity.getProperty("spielKey");
  }

  public Entity toEntity(Key parentKey, int index) {
    Entity entity;
    if (key != null) {
      entity = new Entity(key);
    } else {
      entity = new Entity(KIND, parentKey);
    }
    entity.setProperty("name", name);
    entity.setProperty("zusatz", zusatz);
    entity.setProperty("count", (long) count);
    entity.setProperty("gespielteSpiele_INTEGER_IDX", (long) index);
    if (spielKey != null) {
      entity.setProperty("spielKey", spielKey);
    }
    return entity;
  }
  
  public GespieltesSpiel getGespieltesSpiel() {
    GespieltesSpiel gs = new GespieltesSpiel();
    if (key != null) {
      gs.setWebsafeKey(KeyFactory.keyToString(key));
    }
    if (spielKey != null) {
      gs.setSpielWebsafeKey(KeyFactory.keyToString(spielKey));
    }
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
    return name != null && name.equals(spieleName);
  }

  public Key getKey() {
    return key;
  }

  public void setKey(Key key) {
    this.key = key;
  }

  public Key getSpielKey() {
    return spielKey;
  }

  public void setSpielKey(Key spielKey) {
    this.spielKey = spielKey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getZusatz() {
    return zusatz;
  }

  public void setZusatz(String zusatz) {
    this.zusatz = zusatz;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }
}
