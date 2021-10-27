# 40. Bundeswettbewerb für Informatik - Aufgabe 3 (Wortsuche)

## Aufgabenstellung

Schreibe ein Programm, das eine gegebene Wortliste und die Größe des Rechtecks einliest und daraus Buchstabenrechtecke entsprechend deiner Schwierigkeitsgrade erzeugt.

## Lösungsidee

Das Hauptziel des Algorithmuses ist, dass verschiedene Wortfelder nach verschiedenen Schwierigkeitsgraden generiert werden sollen.

### Schwierigkeitsgrad **LEICHT**

Beim einfachsten Schwierigkeitsgrad des Algorithmuses können Wörter nur horizontal und vertikal positioniert werden und dürfen sich dabei nicht überschneiden. Des Weiteren werden die übrigen Stellen mit zufälligen Buchstaben aufgefüllt.

### Schwierigkeitsgrad **MITTEL**

Bei diesem Schwierigkeitsgrad des Algorithmuses können Wörter sowohl horizontal, als auch vertikal und diagonal (auch überschneidend) platziert werden. Die übrigen Stellen werden hierbei nur mit zufälligen Buchstaben aufgefüllt, welche in den Wörtern der Wortliste enthalten sind (können auch alle sein).

### Schwierigkeitsgrad **SCHWER**

Beim letzten Schwierigkeitsgrad des Algorithmuses können Wörter horizontal, vertikal und diagonal platziert werden und können sich dabei auch überschneiden. Außerdem werden die Leerstellen mit Fragment der Wörter der Wortliste aufgefüllt (keine zufälligen Buchstaben).

## Verwendung des Programmes

Das Programm zur Lösung dieses Problemes befindet sich in der Datei "Aufgabe_3.jar". Das Programm kann mit der Befehlszeile (CMD auf Windows bzw. Temrinal auf MacOS) aufgeführt werden.
Dafür navigiert man zuerst in den Ordner der JAR-Datei (hier Aufgabe 3). Anschließend führt man den Befehl "java -jar "Aufgabe_3.jar \<Eingabedatei> \<Ausgabedatei>" aus.
Die Ausgabedatei ist optional. Wenn keine Ausgabedatei angegeben ist, dann wird das Ergebnis als "output.txt" in dem Ordner der JAR-Datei gespeichert.

## Implementierung

### Einlesen der Beispieldateien

### Schwierigkeitsgrad: Leicht

### Schwierigkeitsgrad: Mittel

### Schwierigkeitsgrad: Schwer

## Beispiele

### Beispiel 1 (worte0.txt)