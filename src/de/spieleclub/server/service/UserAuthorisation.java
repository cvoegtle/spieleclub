package de.spieleclub.server.service;

import javax.jdo.PersistenceManager;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.spieleclub.server.persistence.PersistentSpieler;
import de.spieleclub.server.persistence.SpielerAccess;
import de.spieleclub.shared.LoginInfo;
import de.spieleclub.shared.Spieler;

public class UserAuthorisation {
  private String requestUri;
  private PersistenceManager pm;

  public UserAuthorisation(PersistenceManager pm, String requestUri) {
    this.pm = pm;
    this.requestUri = requestUri;
  }

  public LoginInfo checkAuthorisation() {
    LoginInfo loginInfo = new LoginInfo();

    UserService us = UserServiceFactory.getUserService();
    User user = us.getCurrentUser();

    if (user != null) {
      loginInfo.setAdmin(us.isUserAdmin());
      Spieler spieler = readSpielerOrCreateNewSpieler(user);

      loginInfo.setSpieler(spieler);
      loginInfo.setLogoutUrl(us.createLogoutURL(requestUri));
    } else {
      loginInfo.setLoginUrl(us.createLoginURL(requestUri));
    }

    return loginInfo;
  }

  private Spieler readSpielerOrCreateNewSpieler(User user) {
    Spieler spieler = new SpielerAccess(pm).read(user.getEmail());

    if (spieler == null) {
      PersistentSpieler persistentSpieler = new PersistentSpieler(user.getEmail(), user.getNickname());
      pm.makePersistent(persistentSpieler);
      spieler = persistentSpieler.getSpieler();
    }
    return spieler;
  }

}
