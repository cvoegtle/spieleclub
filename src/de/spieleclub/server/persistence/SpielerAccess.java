package de.spieleclub.server.persistence;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

import de.spieleclub.shared.Spieler;

public class SpielerAccess {
  private DatastoreService datastore;
  
  public SpielerAccess(DatastoreService datastore) {
    this.datastore = datastore;
  }
  
  public Spieler read(String emailAddress) {
    Query query = new Query(PersistentSpieler.KIND)
        .setFilter(new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, emailAddress));
    
    Entity matched = null;
    for (Entity entity : datastore.prepare(query).asIterable()) {
      if (matched == null) {
        matched = entity;
      }
      if (entity.getKey().getParent() == null) {
        matched = entity;
        break;
      }
    }
    if (matched != null) {
      return new PersistentSpieler(matched).getSpieler();
    }
    return null;
  }
}
