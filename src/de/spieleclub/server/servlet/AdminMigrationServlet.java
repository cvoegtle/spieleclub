package de.spieleclub.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.spieleclub.server.SpieleclubServiceImpl;

public class AdminMigrationServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/plain; charset=UTF-8");
    PrintWriter out = resp.getWriter();

    String path = req.getPathInfo();
    if (path == null) {
      path = "";
    }
    if (path.endsWith("/") && path.length() > 1) {
      path = path.substring(0, path.length() - 1);
    }

    if (path.equals("/rename")) {
      String oldName = req.getParameter("old");
      String newName = req.getParameter("new");
      if (oldName == null || newName == null || oldName.trim().isEmpty() || newName.trim().isEmpty()) {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.println("Fehler: Parameter 'old' und 'new' sind erforderlich. Beispiel: /admin/rename?old=AlterName&new=NeuerName");
        return;
      }
      boolean success = SpieleclubServiceImpl.renameSpiel(oldName, newName);
      if (success) {
        out.println("Erfolg: Spiel '" + oldName + "' wurde erfolgreich umbenannt zu '" + newName + "'. Caches wurden geleert.");
      } else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        out.println("Fehler: Spiel '" + oldName + "' konnte nicht gefunden oder umbenannt werden.");
      }
      return;
    }

    if (path.equals("/migrate")) {
      out.println("Starte Migration der gespielten Spiele auf technische Keys...");
      String result = SpieleclubServiceImpl.migrateGespielteSpieleToTechnicalKeys();
      out.println(result);
      return;
    }

    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    out.println("Unbekannter Pfad: " + req.getRequestURI());
    out.println();
    out.println("Verfügbare Endpunkte:");
    out.println(" - /admin/migrate : Migration der gespielten Spiele auf technische Keys");
    out.println(" - /admin/rename?old=<AlterName>&new=<NeuerName> : Spiel umbenennen bzw. zusammenführen");
  }
}
