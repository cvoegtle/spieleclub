package de.spieleclub.client.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.spieleclub.shared.Spiel;

public class KnownSpiele {
  public List<Spiel> spiele = new ArrayList<Spiel>();

  public KnownSpiele() {    
  }
  
  protected KnownSpiele(List<Spiel> spiele) {
    if (spiele != null) {
      this.spiele = spiele;
    }
  }
  
  public void addSpiel(Spiel newSpiel) {
    spiele.add(newSpiel);
  }
  
  public void removeLastSpiel() {
    int size = spiele.size();
    if (size > 0) {
      spiele.remove(size-1);
    }
  }
  
  public Spiel getLastSpiel() {
    int size = spiele.size();
    if (size > 0) {
      return spiele.get(size-1);
    }
    return null;
  }

  public boolean isNewSpiel(String name) {
    Iterator<Spiel> it = spiele.iterator();
    while (it.hasNext()) {
      if (it.next().getName().equals(name)) {
        return false;
      }
    }
    return true;
  }

  public Spiel getSpielForName(String name) {
    Iterator<Spiel> it = spiele.iterator();
    while (it.hasNext()) {
      Spiel current = it.next();
      if (current.getName() != null && name.equalsIgnoreCase(current.getName())) {
        return current;
      }
    }  
    return null;
  }

  public List<String> getSuggestions() {
    ArrayList<String> suggestions = new ArrayList<String>();
    Iterator<Spiel> it = spiele.iterator();
    while (it.hasNext()) {
      suggestions.add(it.next().getName());
    }
    return suggestions;
  }  
}
