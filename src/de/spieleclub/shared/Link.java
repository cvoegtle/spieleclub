package de.spieleclub.shared;

public class Link {
  private String description;
  private String url;
  
  protected Link(String description, String url) {
    this.description = description;
    this.url = url;
  }
  
  public String getDescription() {
    return description;
  }
  public String getUrl() {
    return url;
  }
  
}
