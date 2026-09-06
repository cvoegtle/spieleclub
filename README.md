# Das spielt der [Spieleclub](http://http://spieleclub-paderborn.de)
[Verwaltung und Auswertung der Spiele](http://spieleclub-paderborn.appspot.com), die wir bei unseren mittwöchlichen Spieleabenden spielen. 

Erfassen dürfen die Spiele nur ganz wenige, aber lesen darf sie jeder.

## Administration & Datenmigration

Die Administrations-Endpunkte sind über die Google App Engine Authentifizierung abgesichert und erfordern die Admin-Rolle (`<role-name>admin</role-name>`).

### 1. Migration auf technische Schlüssel
Verknüpft historische Einträge (`PersistentGespieltesSpiel`) mit den Stammdaten (`PersistentSpiel`) über deren technischen Datastore-Key (`spielKey`). Fehlende Spiele-Stammdaten werden bei Bedarf automatisch angelegt. Nach Abschluss werden die Ranglisten-Caches automatisch geleert. Der Aufruf ist idempotent und kann gefahrlos wiederholt werden.

* **Pfad:** `/admin/migrate`
* **Test:** `https://spieleclub-paderborn-test.uc.r.appspot.com/admin/migrate`
* **Produktion:** `https://spieleclub-paderborn-hdr.uc.r.appspot.com/admin/migrate`

### 2. Spiel umbenennen / zusammenführen (Rename & Merge)
Benennt ein Spiel um oder führt zwei Spiele zusammen, falls das Zielspiel bereits existiert. Aktualisiert alle Referenzen in historischen Spieleabenden (`spielKey` und denormalisierter Name) und leert die Ranglisten-Caches.

* **Pfad:** `/admin/rename?old=<AlterName>&new=<NeuerName>`
* **Beispiel Test:** `https://spieleclub-paderborn-test.uc.r.appspot.com/admin/rename?old=Die+Siedler+von+Catan&new=Catan`
* **Parameter:**
  * `old`: Name des bisherigen Spiels (URL-encoded)
  * `new`: Neuer Name des Spiels (URL-encoded)
