package de.spieleclub.server.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

import de.spieleclub.shared.Spieleabend;

public class SpieleabendAccess {
  private DatastoreService datastore;
  
  public SpieleabendAccess(DatastoreService datastore) {
    this.datastore = datastore;
  }
  
  public List<Spieleabend> read(Date startDate, Date endDate, SortingOrder sortingOrder) {
    Query query = new Query(PersistentSpieleabend.KIND);
    
    List<Query.Filter> filters = new ArrayList<>();
    if (startDate != null) {
      filters.add(new Query.FilterPredicate("date", Query.FilterOperator.GREATER_THAN_OR_EQUAL, startDate));
    }
    if (endDate != null) {
      filters.add(new Query.FilterPredicate("date", Query.FilterOperator.LESS_THAN_OR_EQUAL, endDate));
    }
    if (filters.size() == 1) {
      query.setFilter(filters.get(0));
    } else if (filters.size() > 1) {
      query.setFilter(Query.CompositeFilterOperator.and(filters));
    }

    query.addSort("date", sortingOrder == SortingOrder.ASCENDING ? Query.SortDirection.ASCENDING : Query.SortDirection.DESCENDING);

    List<Spieleabend> result = new ArrayList<>();
    for (Entity entity : datastore.prepare(query).asIterable()) {
      PersistentSpieleabend psa = new PersistentSpieleabend(entity, datastore);
      result.add(psa.getSpieleabend());
    }

    return result;
  }
  
  public Date readDateOfFirstSpieleabend(String spieleName) {
    Date dateOfFirstSpieleabend = new Date();
    
    Query query = new Query(PersistentSpieleabend.KIND);
    query.addSort("date", Query.SortDirection.ASCENDING);
    
    for (Entity entity : datastore.prepare(query).asIterable()) {
      PersistentSpieleabend spieleabend = new PersistentSpieleabend(entity, datastore);
      if (spieleName == null || spieleName.equals("") || spieleabend.hasSpielBeenPlayed(spieleName)) {
        dateOfFirstSpieleabend = spieleabend.getDate();
        break;
      }
    }
    
    return dateOfFirstSpieleabend;
  }

  public List<Spieleabend> readAllSpieleabendeAscending() {
    return read(null, null, SortingOrder.ASCENDING);
  }
}
