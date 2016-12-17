package de.spieleclub.client.helper;

import java.util.Iterator;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.Spieleabend;

public class SpieleabendMarkup {
  private Spieleabend spieleabend;
  
  public SpieleabendMarkup(Spieleabend spieleabend) {
    this.spieleabend = spieleabend;
  }
  
  public String getMarkup() {
    StringBuffer markup = new StringBuffer();
    
    markup.append(getDateLine());
    markup.append(getDescriptionLine());
    markup.append(getSpieleLines());
    
    return markup.toString();
  }

  private String getDateLine() {
    StringBuffer markup = new StringBuffer();
    
    markup.append("'''");
    markup.append(DateTimeFormat.getFormat("EEEE, dd.MM.yyyy").format(spieleabend.getDate()));
    markup.append("'''");
    markup.append("<br/>\n");
        
    return markup.toString();    
  }
  private String getDescriptionLine() {
    StringBuffer markup = new StringBuffer();

    if (spieleabend.getDescription() != null && spieleabend.getDescription().length() != 0) {
      markup.append("(");
      markup.append(spieleabend.getDescription());
      markup.append(")");
      markup.append("<br/>\n");
    }
    
    return markup.toString();    
  }

  @SuppressWarnings("unchecked")
  private String getSpieleLines() {
    StringBuffer markup = new StringBuffer();

    Iterator<GespieltesSpiel> it = spieleabend.getGespielteSpiele().iterator();
    while (it.hasNext()) {
      GespieltesSpiel spiel = it.next();
      markup.append("* ");
      markup.append(spiel.getNameWithCount());
      markup.append("\n");
    }
    
    return markup.toString();    
  }


}
