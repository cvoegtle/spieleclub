package de.spieleclub.shared;

import java.io.Serializable;
import de.spieleclub.shared.helper.ColumnAccess;


public class GespieltesSpiel implements Serializable, Cloneable, ColumnAccess, Comparable<GespieltesSpiel> {
  private static final long serialVersionUID = 1L;

  private String websafeKey;
  
  private String name;
  private String zusatz;
  
  private int count = 1;

  public GespieltesSpiel(String name, String zusatz) {
    this.name = name;
    this.zusatz = zusatz;
  }
  
  public GespieltesSpiel() {
  }
  
  public GespieltesSpiel clone() {
    GespieltesSpiel clonedSpiel = new GespieltesSpiel(name, zusatz);
    clonedSpiel.setCount(count);
    return clonedSpiel;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void incrementCount() {
    count++;
  }

  public void decrementCount() {
    count--;
  }

  public String getWebsafeKey() {
    return websafeKey;
  }

  public void setWebsafeKey(String websafeKey) {
    this.websafeKey = websafeKey;
  }

  public String getDisplayName() {
    String displayName = "";
    if (name != null) {
      if (zusatz != null && zusatz.length() > 0) {
        displayName = name + " (" + zusatz + ")";
      } else {
        displayName = name;
      }
    }
    return displayName;
  }
  
  public String getNameWithCount() {
    String nameWithCount = "";
    if (name != null) {
      nameWithCount = getDisplayName();
      if (count > 1) {
        nameWithCount += " (" + count + "x)";
      }
    }
    return nameWithCount;
  }
  
  @Override
  public String getValueFor(int column) {
    if (column == 0) {
      return getDisplayName();
    } else if (column == 1) {
      return Integer.toString(getCount());
    }
    return null;
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

  @Override
  public int compareTo(GespieltesSpiel o) {
    if (o == null) {
      return 1;
    }
    if (name.compareTo(o.name) != 0) {
      return name.compareTo(o.name);
    }
    if (this.zusatz != null) {
      return zusatz.compareTo(o.zusatz);
    }
    if (o.getZusatz() != null) {
      return -1;
    }
    return 0;
  }

  public void incrementCount(int increment) {
    count += increment;    
  }
}
