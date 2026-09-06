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

    if (path.equals("/rename") || req.getServletPath().equals("/admin/rename")) {
      String oldName = req.getParameter("old");
      String newName = req.getParameter("new");
      if (oldName == null || newName == null || oldName.trim().isEmpty() || newName.trim().isEmpty()) {
        out.println("Fehler: Parameter 'old' und 'new' sind erforderlich. Beispiel: /admin/rename?old=AlterName&new=NeuerName");
        return;
      }
      boolean success = SpieleclubServiceImpl.renameSpiel(oldName, newName);
      if (success) {
        out.println("Erfolg: Spiel '" + oldName + "' wurde erfolgreich umbenannt zu '" + newName + "'. Caches wurden geleert.");
      } else {
        out.println("Fehler: Spiel '" + oldName + "' konnte nicht gefunden oder umbenannt werden.");
      }
      return;
    }

    // Default: Migration
    out.println("Starte Migration der gespielten Spiele auf technische Keys...");
    String result = SpieleclubServiceImpl.migrateGespielteSpieleToTechnicalKeys();
    out.println(result);
  }
}
