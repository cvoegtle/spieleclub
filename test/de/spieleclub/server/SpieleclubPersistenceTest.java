package de.spieleclub.server;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import de.spieleclub.server.persistence.PersistentSpieler;
import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.LoginInfo;
import de.spieleclub.shared.Period;
import de.spieleclub.shared.RankedSpiel;
import de.spieleclub.shared.Ranking;
import de.spieleclub.shared.Spiel;
import de.spieleclub.shared.SpieleProfile;
import de.spieleclub.shared.Spieleabend;
import de.spieleclub.shared.Spieler;

public class SpieleclubPersistenceTest {

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test
  public void testSaveAndLoadSpiele() {
    Spiel spiel1 = new Spiel();
    spiel1.setName("Terraforming Mars");
    spiel1.setCreation(new Date());

    Spiel spiel2 = new Spiel();
    spiel2.setName("Catan");
    spiel2.setCreation(new Date());

    SpieleclubServiceImpl.saveSpiel(spiel1);
    SpieleclubServiceImpl.saveSpiel(spiel2);

    List<Spiel> spiele = SpieleclubServiceImpl.loadSpiele();
    assertEquals(2, spiele.size());
    // Should be sorted alphabetically
    assertEquals("Catan", spiele.get(0).getName());
    assertEquals("Terraforming Mars", spiele.get(1).getName());
    assertNotNull(spiele.get(0).getWebsafeKey());
  }

  @Test
  public void testSaveAndLoadSpieleabend() {
    Calendar cal = Calendar.getInstance();
    cal.set(2024, Calendar.JANUARY, 10, 19, 0, 0);
    Date date1 = cal.getTime();

    Spieleabend sa = new Spieleabend();
    sa.setDate(date1);
    sa.setDescription("Ein schöner Spieleabend");

    GespieltesSpiel g1 = new GespieltesSpiel();
    g1.setName("Catan");
    g1.setCount(1);
    g1.setZusatz("mit Seefahrer");
    sa.add(g1);

    GespieltesSpiel g2 = new GespieltesSpiel();
    g2.setName("Azul");
    g2.setCount(2);
    sa.add(g2);

    assertTrue(SpieleclubServiceImpl.saveSpieleabend(sa));

    List<Spieleabend> abende = SpieleclubServiceImpl.loadSpieleabende(null, null);
    assertEquals(1, abende.size());
    Spieleabend loaded = abende.get(0);
    assertEquals("Ein schöner Spieleabend", loaded.getDescription());
    assertNotNull(loaded.getWebsafeKey());
    assertEquals(2, loaded.getGespielteSpiele().size());

    GespieltesSpiel loadedG1 = (GespieltesSpiel) loaded.getGespielteSpiele().get(0);
    assertEquals("Catan", loadedG1.getName());
    assertEquals("mit Seefahrer", loadedG1.getZusatz());
    assertEquals(1, loadedG1.getCount());

    GespieltesSpiel loadedG2 = (GespieltesSpiel) loaded.getGespielteSpiele().get(1);
    assertEquals("Azul", loadedG2.getName());
    assertEquals(2, loadedG2.getCount());

    // Update the spieleabend
    loaded.setDescription("Aktualisierte Beschreibung");
    assertTrue(SpieleclubServiceImpl.saveSpieleabend(loaded));

    List<Spieleabend> updatedAbende = SpieleclubServiceImpl.loadSpieleabende(null, null);
    assertEquals(1, updatedAbende.size());
    assertEquals("Aktualisierte Beschreibung", updatedAbende.get(0).getDescription());
    assertEquals(2, updatedAbende.get(0).getGespielteSpiele().size());
  }

  @Test
  public void testRankingAndProfile() {
    Calendar cal = Calendar.getInstance();
    cal.set(2024, Calendar.MARCH, 1, 19, 0, 0);

    Spieleabend sa1 = new Spieleabend();
    sa1.setDate(cal.getTime());
    sa1.setDescription("Abend 1");
    GespieltesSpiel g1 = new GespieltesSpiel();
    g1.setName("Catan");
    g1.setCount(2);
    sa1.add(g1);
    SpieleclubServiceImpl.saveSpieleabend(sa1);

    cal.set(2024, Calendar.MARCH, 8, 19, 0, 0);
    Spieleabend sa2 = new Spieleabend();
    sa2.setDate(cal.getTime());
    sa2.setDescription("Abend 2");
    GespieltesSpiel g2 = new GespieltesSpiel();
    g2.setName("Catan");
    g2.setCount(1);
    sa2.add(g2);
    GespieltesSpiel g3 = new GespieltesSpiel();
    g3.setName("Carcassonne");
    g3.setCount(1);
    sa2.add(g3);
    SpieleclubServiceImpl.saveSpieleabend(sa2);

    cal.set(2024, Calendar.JANUARY, 1);
    Date start2024 = cal.getTime();
    cal.set(2024, Calendar.DECEMBER, 31);
    Date end2024 = cal.getTime();

    Period period = new Period(start2024, end2024);
    Ranking ranking = SpieleclubServiceImpl.loadAnalysis(period);
    assertNotNull(ranking);

    RankedSpiel catanRank = ranking.getSpielByName("Catan");
    assertNotNull(catanRank);
    assertEquals(3, catanRank.getCount());

    SpieleProfile profile = SpieleclubServiceImpl.loadSpieleProfile("Catan");
    assertNotNull(profile);
    assertEquals("Catan", profile.getName());
    assertEquals(3, profile.getTimesPlayed());

    SpieleclubServiceImpl.clearPrecalculatedData();
  }

  @Test
  public void testLoginWithMultipleSpielerEntities() {
    helper.setEnvIsLoggedIn(true);
    helper.setEnvEmail("cvoegtle@gmail.com");
    helper.setEnvAuthDomain("gmail.com");
    helper.setEnvIsAdmin(true);

    com.google.appengine.api.datastore.DatastoreService datastore =
        com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService();

    // Create a parent Spiel entity
    com.google.appengine.api.datastore.Entity spielEntity =
        new com.google.appengine.api.datastore.Entity("PersistentSpiel");
    spielEntity.setProperty("name", "Test Game");
    datastore.put(spielEntity);

    // Create a child PersistentSpieler under the Spiel
    PersistentSpieler childSpieler = new PersistentSpieler("cvoegtle@gmail.com", "cvoegtle");
    com.google.appengine.api.datastore.Entity childEntity = childSpieler.toEntity(spielEntity.getKey());
    datastore.put(childEntity);

    // Create a root PersistentSpieler
    PersistentSpieler rootSpieler = new PersistentSpieler("cvoegtle@gmail.com", "cvoegtle");
    com.google.appengine.api.datastore.Entity rootEntity = rootSpieler.toEntity();
    datastore.put(rootEntity);

    // Now call loginSpieler - this previously threw TooManyResultsException!
    LoginInfo loginInfo = SpieleclubServiceImpl.loginSpieler("https://localhost/capture.html");
    assertNotNull(loginInfo);
    assertTrue(loginInfo.isLoggedIn());
    assertTrue(loginInfo.isAdmin());
    assertNotNull(loginInfo.getSpieler());
    assertEquals("cvoegtle@gmail.com", loginInfo.getSpieler().getEmail());
    assertEquals("cvoegtle", loginInfo.getSpieler().getName());
  }
}
