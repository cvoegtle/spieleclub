package de.spieleclub.client.i18n;

import com.google.gwt.i18n.client.Constants;

public interface Texte extends Constants {
  
  String ok();
  String cancel();
  String add();
  String remove();
  String save();
  
  String yearListDescription();
  String toTheAnalysis();
  
  String currentlyPlayed();
  String gamesFrom();
  String takeOver();

  String spiel();
  String rounds();
  String rank();
  String rankOverall();
  
  String all();
  
  String newSpiel();
  String markup();
  String errorSavingSpieleabend();
  String spieleabendSaved();
  String description();
  String logout();
  String login();
  String errorLoggingIn();
  String addInfo();
  String analysis();
  String backToStartpage();
  
  String errorLoadingSpieleabend();
  String errorLoadingDateOfFirstSpieleabend();
  String errorLoadingAnalysis();
  String errorLoadingSpieleDetails();
  String errorClearingPrecalculation();
  String errorRecalculatingPrecalculation();
  
  String backToSpieleclub();
  String firstPlayedOn();
  String notPlayed();
  String played();
  String close();
  String on();
  String detailsAreTransfered();
  String columns();
}
