package de.spieleclub.server.service;

import javax.jdo.PersistenceManager;

public class RankingCacheFactory {
  PersistenceManager pm;
  
  public RankingCacheFactory(PersistenceManager pm) {
    this.pm = pm;
  }
  
  
  public RankingCache getRankingCache() {
    return new PersistentRankingCache(pm);
  }

}
