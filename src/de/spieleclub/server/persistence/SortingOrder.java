package de.spieleclub.server.persistence;

public enum SortingOrder {
  ASCENDING ("ascending"), DESCENDING ("descending");

  private final String order;
  
  SortingOrder(String order) {
    this.order = order;
  }
  
  public String order() {
    return order;
  }
}
