package de.spieleclub.server.persistence;

import java.util.Iterator;
import java.util.LinkedList;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import de.spieleclub.shared.RankedSpiel;
import de.spieleclub.shared.Ranking;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
public class PersistentRanking {

  @PrimaryKey
  @Persistent
  private String label;
  
  @Persistent
  private LinkedList<PersistentRankedSpiel> rankedSpiele = new LinkedList<>();
  
  public PersistentRanking(Ranking ranking) {
    this.label = ranking.getLabel();
    Iterator<RankedSpiel> it = ranking.iterator();
    while (it.hasNext()) {
      rankedSpiele.add(new PersistentRankedSpiel(it.next()));
    }
  }
  
  public Ranking getRanking() {
    LinkedList<RankedSpiel> rankedSpiele = new LinkedList<>();
    for (PersistentRankedSpiel aRankedSpiele : this.rankedSpiele) {
      rankedSpiele.add(aRankedSpiele.getRankedSpiel());
    }
    
    return new Ranking(label, rankedSpiele);    
  }

  public String getLabel() {
    return label;
  }
}
