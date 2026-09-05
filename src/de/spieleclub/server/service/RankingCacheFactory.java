package de.spieleclub.server.service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

public class RankingCacheFactory {
  private DatastoreService datastore;
  
  public RankingCacheFactory(DatastoreService datastore) {
    this.datastore = datastore;
  }

  public RankingCacheFactory() {
    this.datastore = DatastoreServiceFactory.getDatastoreService();
  }
  
  public RankingCache getRankingCache() {
    return new PersistentRankingCache(datastore != null ? datastore : DatastoreServiceFactory.getDatastoreService());
  }
}
