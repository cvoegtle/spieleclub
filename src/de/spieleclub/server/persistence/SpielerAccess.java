package de.spieleclub.server.persistence;

import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import de.spieleclub.shared.Spieler;

public class SpielerAccess {
  private PersistenceManager pm;
  
  public SpielerAccess(PersistenceManager pm) {
    this.pm = pm;
  }
  
  public Spieler read(String emailAddress) {
    Query query = pm.newQuery(PersistentSpieler.class, "email == users_email");
    query.declareImports("import java.lang.String");
    query.declareParameters("String users_email");
    
    @SuppressWarnings("unchecked")
    List<PersistentSpieler> queryResult = (List<PersistentSpieler>)query.execute(emailAddress);
    Iterator<PersistentSpieler> it = queryResult.iterator();
    
    if (it.hasNext()) {
      return it.next().getSpieler();
    }    
    return null;
  }

}
