package de.spieleclub.shared;

import java.io.Serializable;

public class LoginInfo implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private Spieler spieler;
  private String loginUrl;
  private String logoutUrl;
  private boolean admin = false;
  
  public Spieler getSpieler() {
    return spieler;
  }
  public void setSpieler(Spieler spieler) {
    this.spieler = spieler;
  }
  public String getLoginUrl() {
    return loginUrl;
  }
  public void setLoginUrl(String loginUrl) {
    this.loginUrl = loginUrl;
  }
  public String getLogoutUrl() {
    return logoutUrl;
  }
  public void setLogoutUrl(String logoutUrl) {
    this.logoutUrl = logoutUrl;
  }
  
  public boolean isLoggedIn() {
    return spieler != null;
  }
  public boolean isAdmin() {
    return admin;
  }
  public void setAdmin(boolean admin) {
    this.admin = admin;
  }
  
  public Link getLink(String login, String logout) {
    if (isLoggedIn()) {
      return new Link(logout + " " + spieler.getName(), logoutUrl);
    } else {
      return new Link(login, loginUrl);
    }
  }
}
