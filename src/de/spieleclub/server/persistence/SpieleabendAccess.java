package de.spieleclub.server.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import de.spieleclub.shared.Spieleabend;

public class SpieleabendAccess {
  private PersistenceManager pm;
  
  public SpieleabendAccess(PersistenceManager pm) {
    this.pm = pm;
  }
  
  @SuppressWarnings("unchecked")
  public List<Spieleabend> read(Date startDate, Date endDate, SortingOrder sortingOrder) {
    List<PersistentSpieleabend> queryResult; 
    
    if (startDate == null && endDate == null) {
      Query query = pm.newQuery(PersistentSpieleabend.class);
      query.setOrdering("date " + sortingOrder);
      queryResult = (List<PersistentSpieleabend>)query.execute();        
    } else if (startDate != null && endDate == null) {
      Query query = pm.newQuery(PersistentSpieleabend.class, "date >= start_date");
      query.declareImports("import java.util.Date");
      query.declareParameters("Date start_date");
      query.setOrdering("date " + sortingOrder);
      queryResult = (List<PersistentSpieleabend>)query.execute(startDate);        
    } else if (startDate == null && endDate != null) {
      Query query = pm.newQuery(PersistentSpieleabend.class, "date <= end_date");
      query.declareImports("import java.util.Date");
      query.declareParameters("Date end_date");
      query.setOrdering("date " + sortingOrder);
      queryResult = (List<PersistentSpieleabend>)query.execute(endDate);        
    } else {
      Query query = pm.newQuery(PersistentSpieleabend.class, "date >= start_date && date <= end_date");
      query.declareImports("import java.util.Date");
      query.declareParameters("Date start_date, Date end_date");
      query.setOrdering("date " + sortingOrder);
      queryResult = (List<PersistentSpieleabend>)query.execute(startDate, endDate);        
    }

    return convertToSpieleabende(queryResult);
  }
  
  
  @SuppressWarnings("unchecked")
  public Date readDateOfFirstSpieleabend(String spieleName) {
    Date dateOfFirstSpieleabend = new Date();
    
    Query query = pm.newQuery(PersistentSpieleabend.class);
    query.setOrdering("date asc");
    
    List<PersistentSpieleabend> queryResult = (List<PersistentSpieleabend>)query.execute(new PersistentGespieltesSpiel(spieleName));

    for (PersistentSpieleabend spieleabend : queryResult) {
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

  private List<Spieleabend> convertToSpieleabende(
      List<PersistentSpieleabend> queryResult) {
    List<Spieleabend> result = new ArrayList<Spieleabend>();
    Iterator<PersistentSpieleabend> it = queryResult.iterator();
    while (it.hasNext()) {
      result.add(it.next().getSpieleabend());
    }
    return result;
  }
  
}
