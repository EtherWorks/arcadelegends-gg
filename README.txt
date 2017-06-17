Projekt Setup:
1. Projekt in IntelliJ öffnen
2. Gradle updaten: View --> Tool Windows --> Gradle --> auf den Update Button klicken
3. Ressourcen:  Rechtsklick auf Projekt --> Open Module Settings --> Modules --> desktop_main --> auf das grüne + klicken
		        --> JARs or directories --> resources Ordner auswählen --> OK --> Classes auswählen und OK
		        --> den Scope von Compile auf Runtime ändern --> mit OK bestätigen
4. Fertig

Projekt starten --> Main Klasse: desktop/src/gg.al.desktop/launcher/DesktopLauncher.java

Default-Steuerung:

Funktion        Key
--------------------
Up              W
Down            S
Left            A
Right           D
Fähigkeit 1     1
Fähigkeit 2     2
Fähigkeit 3     3
Fähigkeit 4     4

Fähigkeiten kann man mit Strg+[Fähigkeit-Key] aufleveln --> z.B.: Strg + 1
Man kann auch mit der linken Maustaste bestimmen wo sich der eigene Charakter hinbewegt.
Mit der rechten Maustaste kann man Gegner mit normalen Angriffen angreifen.
Mit dem Mausrad kann man rein und raus zoomen.
Wenn man das Mausrad gedrückt hält, kann man die Kamera frei bewegen.
Wenn man die Leertaste gedrückt hält, folgt die Kamera dem Charakter.