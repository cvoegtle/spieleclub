package de.spieleclub.server.service;

import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.spieleclub.server.persistence.PersistentSpieler;
import de.spieleclub.server.persistence.SpielerAccess;
import de.spieleclub.shared.LoginInfo;
import de.spieleclub.shared.Spieler;

public class UserAuthorisation {
  private static final Logger logger = Logger.getLogger(UserAuthorisation.class.getName());

  private String requestUri;
  private DatastoreService datastore;

  public UserAuthorisation(DatastoreService datastore, String requestUri) {
    this.datastore = datastore;
    this.requestUri = requestUri;
  }

  public LoginInfo checkAuthorisation() {
    LoginInfo loginInfo = new LoginInfo();

    UserService us = UserServiceFactory.getUserService();
    User user = us.getCurrentUser();

    logger.info("checkAuthorisation: user=" + (user != null ? user.getEmail() : "null") + ", requestUri=" + requestUri);

    if (user != null) {
      boolean admin = us.isUserAdmin();
      loginInfo.setAdmin(admin);
      Spieler spieler = readSpielerOrCreateNewSpieler(user);

      loginInfo.setSpieler(spieler);
      loginInfo.setLogoutUrl(us.createLogoutURL(requestUri));
      logger.info("user logged in: " + user.getEmail() + ", isAdmin=" + admin + ", spieler=" + (spieler != null ? spieler.getName() : "null"));
    } else {
      String loginUrl = us.createLoginURL(requestUri);
      loginInfo.setLoginUrl(loginUrl);
      logger.info("user not logged in, loginUrl=" + loginUrl);
    }

    return loginInfo;
  }

  private Spieler readSpielerOrCreateNewSpieler(User user) {
    Spieler spieler = new SpielerAccess(datastore).read(user.getEmail());

    if (spieler == null) {
      PersistentSpieler persistentSpieler = new PersistentSpieler(user.getEmail(), user.getNickname());
      Entity entity = persistentSpieler.toEntity();
      datastore.put(entity);
      persistentSpieler.setKey(entity.getKey());
      spieler = persistentSpieler.getSpieler();
    }
    return spieler;
  }

}
