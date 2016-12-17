package de.spieleclub.server.service;

import java.util.Date;

import de.spieleclub.shared.Period;
import de.spieleclub.shared.Ranking;

public interface RankingCache {

  Ranking getRanking(Period period);
  Date getDateOfFirstPlay(String spielename);
  
  void clear();
}