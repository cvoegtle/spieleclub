package de.spieleclub.server.persistence;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.spieleclub.shared.RankedSpiel;

public class PersistentRankedSpiel {
  public static final String KIND = "PersistentRankedSpiel";

  private Key key;
  private Key spielKey;
  private int rank;
  private String formattedRank = "";
  private String name = "";
  private int count = 0;

  public PersistentRankedSpiel(RankedSpiel rankedSpiel) {
    if (rankedSpiel.getSpielWebsafeKey() != null) {
      spielKey = KeyFactory.stringToKey(rankedSpiel.getSpielWebsafeKey());
    }
    rank = rankedSpiel.getRank();
    formattedRank = rankedSpiel.getFormattedRank();
    name = rankedSpiel.getName();
    count = rankedSpiel.getCount();
  }

  public PersistentRankedSpiel(Entity entity) {
    this.key = entity.getKey();
    this.spielKey = (Key) entity.getProperty("spielKey");
    Object r = entity.getProperty("rank");
    this.rank = r != null ? ((Number) r).intValue() : 0;
    this.formattedRank = (String) entity.getProperty("formattedRank");
    this.name = (String) entity.getProperty("name");
    Object c = entity.getProperty("count");
    this.count = c != null ? ((Number) c).intValue() : 0;
  }

  public Entity toEntity(Key parentKey, int index) {
    Entity entity;
    if (key != null) {
      entity = new Entity(key);
    } else {
      entity = new Entity(KIND, parentKey);
    }
    entity.setProperty("rank", (long) rank);
    entity.setProperty("formattedRank", formattedRank);
    entity.setProperty("name", name);
    entity.setProperty("count", (long) count);
    entity.setProperty("rankedSpiele_INTEGER_IDX", (long) index);
    if (spielKey != null) {
      entity.setProperty("spielKey", spielKey);
    }
    return entity;
  }

  public RankedSpiel getRankedSpiel() {
    RankedSpiel rankedSpiel = new RankedSpiel();
    rankedSpiel.setRank(rank);
    rankedSpiel.setFormattedRank(formattedRank);
    rankedSpiel.setName(name);
    rankedSpiel.setCount(count);
    if (spielKey != null) {
      rankedSpiel.setSpielWebsafeKey(KeyFactory.keyToString(spielKey));
    }
    return rankedSpiel;
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
}
