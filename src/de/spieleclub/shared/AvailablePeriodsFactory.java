package de.spieleclub.shared;

import java.util.Date;

public class AvailablePeriodsFactory {
  public static AvailablePeriods makeAvailablePeriods(Date dateOfFirstSpieleabend) {
    return new AvailablePeriods(dateOfFirstSpieleabend, "Alle Jahre");
  }

}
