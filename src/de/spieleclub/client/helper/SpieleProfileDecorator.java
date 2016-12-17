package de.spieleclub.client.helper;

import java.util.Iterator;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.spieleclub.client.Global;
import de.spieleclub.shared.SpieleProfile;
import de.spieleclub.shared.YearlyRank;

public class SpieleProfileDecorator {
  private SpieleProfile profile;

  public SpieleProfileDecorator(SpieleProfile profile) {
    this.profile = profile;
  }
  
  public String getSpieleName() {
    return profile.getName();
  }
  
  public String getFirstTimePlayed() {
    String formattedDate = DateTimeFormat.getFormat("dd.MM.yyyy").format(profile.getFirstTimePlayed());
    return Global.texte.on() + " " + formattedDate + " " + Global.texte.firstPlayedOn();
  }
  
  public String getRank() {
    StringBuffer html = new StringBuffer();
    html.append("<b>");
    html.append(profile.getAllTimeRanking());
    html.append(". ");
    html.append(Global.texte.rankOverall());
    html.append("</b>");
    return html.toString();
  }
  
  public String getTimesPlayed() {
    StringBuffer html = new StringBuffer();
    html.append(profile.getTimesPlayed() + "x " + Global.texte.played());
    return html.toString();
  }
  
  public String getYearlyStatistic() {
    StringBuffer html = new StringBuffer();
    html.append("<ul>");
    
    Iterator<YearlyRank> it = profile.iterator();
    while (it.hasNext()) {
      YearlyRank yearStatistic = it.next();
      html.append("<li>");
      html.append(formatYearStatistic(yearStatistic));
      html.append("</li>");
    }
   
    html.append("</ul>");
    return html.toString();
  }
  
  private String formatYearStatistic(YearlyRank yearStatistic) {
    StringBuffer formattedYear = new StringBuffer();
    if (yearStatistic.hasBeenPlayedThisYear()) {
      formattedYear.append("<b>"+ Integer.toString(yearStatistic.getYear()) +  "</b> ");
      formattedYear.append(Global.texte.rank() + " " + yearStatistic.getRank());
      formattedYear.append(" (" + yearStatistic.getTimesPlayed() + "x "+ Global.texte.played() + ")");       
    } else {
      formattedYear.append(Integer.toString(yearStatistic.getYear()) + " " + Global.texte.notPlayed());
    }
    return formattedYear.toString();
  }

}
