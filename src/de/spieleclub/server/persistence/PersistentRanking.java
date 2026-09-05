package de.spieleclub.server.persistence;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import de.spieleclub.shared.RankedSpiel;
import de.spieleclub.shared.Ranking;

public class PersistentRanking {
  public static final String KIND = "PersistentRanking";

  private String label;
  private LinkedList<PersistentRankedSpiel> rankedSpiele = new LinkedList<>();
  
  public PersistentRanking(Ranking ranking) {
    this.label = ranking.getLabel();
    Iterator<RankedSpiel> it = ranking.iterator();
    while (it.hasNext()) {
      rankedSpiele.add(new PersistentRankedSpiel(it.next()));
    }
  }

  public PersistentRanking(Entity entity, DatastoreService datastore) {
    this.label = (entity.getKey().getName() != null) ? entity.getKey().getName() : (String) entity.getProperty("label");
    if (datastore != null && entity.getKey() != null) {
      Query childQuery = new Query(PersistentRankedSpiel.KIND)
          .setAncestor(entity.getKey())
          .addSort("rankedSpiele_INTEGER_IDX", Query.SortDirection.ASCENDING);
      for (Entity child : datastore.prepare(childQuery).asIterable()) {
        rankedSpiele.add(new PersistentRankedSpiel(child));
      }
    }
  }

  public Entity toEntity() {
    Key key = KeyFactory.createKey(KIND, label);
    Entity entity = new Entity(key);
    entity.setProperty("label", label);
    return entity;
  }

  public List<PersistentRankedSpiel> getRankedSpiele() {
    return rankedSpiele;
  }
  
  public Ranking getRanking() {
    LinkedList<RankedSpiel> list = new LinkedList<>();
    for (PersistentRankedSpiel aRankedSpiele : this.rankedSpiele) {
      list.add(aRankedSpiele.getRankedSpiel());
    }
    return new Ranking(label, list);    
  }

  public String getLabel() {
    return label;
  }
}
