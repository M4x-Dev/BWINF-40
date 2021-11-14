# 40. Bundeswettbewerb für Informatik - Aufgabe 5 (Marktwaage)

## Aufgabenstellung

Schreibe ein Programm, das solche Fragestellungen beantwortet. Dein Programm soll eine Liste von Gewichten der Gewichtsstücke einlesen und in 10g-Schritten alle Gewichte zwischen 10g und 10kg untersuchen. Für jedes untersuchte Gewicht soll das Programm ausgeben, ob sich das Gewicht mit den vorhandenen Gewichtsstücken erzielen lässt. Falls ja, soll außerdem ausgegeben werden, welche Gewichtsstücke dabei auf welche Seite der Waage gestellt werden müssen, wenn die zu wiegende Waage immer auf die linke Seite gelegt wird. Falls nein, soll ausgegeben werden, wie nahe an das Zielgewicht man mit den vorhandenen Gewichtsstücken kommen kann.

## Lösungsidee

Das Hauptziel des Algorithmuses ist, anhand eines gegebenen Gewichtes auf der linken Seite einer Waage und einer Liste an verfügbaren Gewichten eine Marktwaage auszubalancieren. Dabei dürfen Gewichte sowohl auf die linke, als auch auf die rechte Seite gelegt werden. Der Vorgang wird für alle Gewichte zwischen 10g und 10kg in 10g-Schritten ausgeführt.

## Verwendung des Programmes

Das Programm zur Lösung dieses Problemes befindet sich in der Datei "Aufgabe_5.jar". Das Programm kann mit der Befehlszeile (CMD auf Windows bzw. Terminal auf MacOS) ausgeführt werden. Dafür navigiert man zuerst in den Ordner der JAR-Datei (hier Aufgabe 5).
Anschließend führt man den Befehl "java -jar Aufgabe_5.jar \<Eingabedatei> \<Ausgabedatei>" aus.
Die Ausgabedatei ist optional. Wenn keine Ausgabedatei angegeben ist, dann wird das Ergebnis als "output.txt" in dem Ordner der JAR-Datei gespeichert.

## Implementierung

### Einlesen der Beispieldateien

```java
//Auslesen der Inhalte der Datei
String line;
while((line = fileReader.readLine()) != null) contentBuilder.append(line).append("\n");
String[] fileContent = contentBuilder.toString().split("\n");

//Interpretieren der Daten
int weightAmount = Integer.parseInt(fileContent[0]);

//Verfügbaren Gewichte werden der Liste hinzugefügt
for(int i = 0; i < weightAmount; i++) {
    String[] splitContent = fileContent[1 + i].split(" ");

    for(int a = 0; a < Integer.parseInt(splitContent[1]); a++) {
        //Eigentliches Gewicht hinzufügen
        availableWeights.add(new ScaleWeight(Integer.parseInt(splitContent[0])));
    }
}
```

Die Beispieldateien werden durch die Klasse "ScaleEvaluator" eingelesen. Dabei werden die verfügbaren Gewichte in einer Liste gespeichert.
Dabei stellt die erste Teile der Datei die Gesamtanzahl an Gewichten dar.
Die folgenden Zeilen beeinhalten die einzelnen Gewichte, wobei hier die erste Zahl das Gewicht in Gramm und die zweite Zahl die Menge des Gewichtes angibt.

### Ablauf der Auswertung

```java
public EvaluationResult evaluate() {
    EvaluationResult result = new EvaluationResult(); //Instanz, welche das Ergebnis beeinhalten wird

    //Schleife, welche alle Gewichte zwischen 10g und 10kg ausprobiert
    for(int i = 1; i <= 10000 / 10; i++) {
        int targetWeight = 10 * i; //Zielgewicht
        Scale.ScaleState scaleResult = new Scale().balance(targetWeight, new ArrayList<>(availableWeights));
        result.resultEntries.add(new EvaluationResultEntry(
                //Gewichte auf der linken Seite
                scaleResult.leftWeights(),
                //Gewichte auf der rechten Seite
                scaleResult.rightWeights(),
                //Summe der Gewichte auf der rechten Seite
                Scale.sumWeights(scaleResult.rightWeights()),
                //Differenz zum Zielgewicht, falls dieses nicht erreicht werden konnte
                Scale.sumWeights(scaleResult.rightWeights()) - Scale.sumWeights(scaleResult.leftWeights()),
                //Zielgewicht
                targetWeight,
                //Konnte das Zielgewicht erreicht werden?
                scaleResult.balanced()
        ));
    }

    return result; //Rückgabe des Ergebnisses
}
```

Die Funktion EvaluationResult#evaluate stellt die Hauptfunktion des Algorithmuses zur Lösung des gegebenen Problemes dar. Dazu erstellt der Algorithmus eine neue Instanz der "EvaluationResult"-Klasse, in welcher die Ergebnisse für alle Testgewichte gespeichert werden.
Anschließend durchläuft eine Schleife alle Gewichte von 10g bis 10kg in 10g-Schritten. Diese Schleife verwendet ein "Scale"-Objekt zur Simulation des Problemes. Diese Klasse stellt eine Waage dar, welche ausbalanciert wird. Anschließend wird das Ergebnis der Simulation in dem Ergebnis-Objekt gespeichert.
Zum Schluss wird dieses Objekt zurückgegeben.

### Ablauf der eigentlichen Simulation

```java
//Differenz der beiden Seiten der Waage
int difference = getPlatformDifference(initialWeight);

//Wenn das Zielgewicht größer ist, als die Summe der verfügbaren Gewichte, kann die Waage nicht ausbalanciert werden
if(initialWeight > sumWeights(availableWeights) && rightWeights.size() == 0) return new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(availableWeights), false);

if(bestState == null || (Math.abs(difference) < Math.abs(bestState.difference(initialWeight)))) bestState = new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), false);
else negativeRecursions++;

//Rückgabe des Zustandes, wenn die Waage nicht weiter ausbalanciert werden kann.
if(negativeRecursions > NEGATIVE_RECURSIONS_MAX) return bestState;
```

Der eigentliche Algorithmus zum Ausbalancieren der Marktwaage besteht aus einer Funktion, welche rekursiv ausgeführt wird, bis entweder die Waage erfolgreich ausbalanciert werden konnte, oder eine bestimmte Abbruchbedingung erfüllt wurde, damit der Algorithmus nicht unendlich lange läuft.  
Zu Beginn dieser Funktion werden diese Abbruchbedingungen überprüft. Dafür wird zunächst die Differenz der beiden Seiten der Waage ausgerechnet. Dazu wird die Summe aller Gewichte von der linken Seite von der Summe aller Gewichte auf der rechten Seite subtrahiert.  
Anschließend wird die Abbruchbedingung überprüft. Der Algorithmus wird abgebrochen, wenn die Funktion 10-mal durchlaufen wurde und das Ergebnis, also die erzielte Differenz zum Zielgewicht dabei größer geworden ist. Daraus lässt sich schließen, dass die Waage für dieses Zielgewicht nicht ausbalanciert werden kann.  
Des Weiteren wird der Algorithmus sofort abgebrochen, wenn das Zielgewicht größer ist, als die Summe aller verfügbaren Gewichte. In diesem Fall kann die Waage auch nicht ausbalanciert werden. Als Ergebnis werden alle verfügbaren Gewichte auf die rechte Seite gelegt, da dies am nähesten an das Zielgewicht heranführt.

Danach überprüft der Algorithmus die Differenz der beiden Seiten, welche zuvor berechnet wurde.

```java
//Rechte Seite ist schwerer
ScaleWeight nearestWeightAdd = getNearestWeight(availableWeights, difference); //Gewicht, welches hinzugefügt werden könnte
ScaleWeight nearestWeightRemove = getNearestWeight(rightWeights, difference); //Gewicht, welches heruntergenommen werden könnte
ScaleWeight nearestWeightSwap = getNearestWeight(rightWeights, difference / 2); //Gewicht, welches verschoben werden könnte

//Berechnen der optimalsten Aktion, welche die Waage am weitesten ausbalanciert
switch (getBestAction(nearestWeightAdd, nearestWeightRemove, nearestWeightSwap, initialWeight, false)) {
    case Add -> {
        //Das Gewicht wird auf die linke Seite der Waage gestellt
        availableWeights.remove(nearestWeightAdd);
        leftWeights.add(nearestWeightAdd);
        lastMovedWeight = nearestWeightAdd;
    }
    case Remove -> {
        //Das Gewicht wird von der rechten Seite der Waage heruntergenommen
        rightWeights.remove(nearestWeightRemove);
        availableWeights.add(nearestWeightRemove);
        lastMovedWeight = nearestWeightRemove;
    }
    case Swap -> {
        //Das Gewicht wird von der rechten Seite auf die linke Seite der Waage gestellt
        rightWeights.remove(nearestWeightSwap);
        leftWeights.add(nearestWeightSwap);
        lastMovedWeight = nearestWeightSwap;
    }
}

//Funktion wird rekursiv aufgerufen
return balanceInternal(initialWeight, availableWeights, negativeRecursions);
```

Falls die Different kleiner als 0 ist, dann muss die rechte Seite der Waage schwerer sein. Als nächstes überprüft der Algorithmus alle möglichen Züge, welche er nun ausführen könnte und wählt daraus die Möglichkeit aus, welche die Differenz am kleinsten werden lässt. Um den besten Zug zu ermitteln wird die Funktion Scale#getBestAction verwendet.

```java
public Action getBestAction(ScaleWeight possibleAdd, ScaleWeight possibleRemove, ScaleWeight possibleSwap, int targetWeight, boolean leftHeavier) {
    //Berechnen der neuen Differenz, wenn das Gewicht der Waage hinzugefügt wird
    ArrayList<ScaleWeight> dummyLeftWeights = new ArrayList<>(leftWeights);
    ArrayList<ScaleWeight> dummyRightWeights = new ArrayList<>(rightWeights);

    if(leftHeavier) dummyRightWeights.add(possibleAdd);
    else dummyLeftWeights.add(possibleAdd);
    int differenceAdd = possibleAdd.value != -1 ? Math.abs(getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight)) : 1000;

    //Berechnen der neuen Differenz, wenn das Gewicht von der Waage entfernt wird
    dummyLeftWeights = new ArrayList<>(leftWeights);
    dummyRightWeights = new ArrayList<>(rightWeights);

    if(leftHeavier) dummyLeftWeights.remove(possibleRemove);
    else dummyRightWeights.remove(possibleRemove);
    int differenceRemove = possibleRemove.value != -1 ? Math.abs(getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight)) : 1000;

    //Berechnen der neuen Differenz, wenn das Gewicht von der einen Seite auf die andere Seite der Waage verschoben wird
    dummyLeftWeights = new ArrayList<>(leftWeights);
    dummyRightWeights = new ArrayList<>(rightWeights);

    if(leftHeavier) dummyLeftWeights.remove(possibleSwap);
    else dummyRightWeights.remove(possibleSwap);
    if(leftHeavier) dummyRightWeights.add(possibleSwap);
    else dummyLeftWeights.add(possibleSwap);

    int differenceSwap = possibleSwap.value != -1 ? Math.abs(getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight)) : 1000;

    //Herausfinden der besten Differenz
    int bestDifference = differenceAdd;
    if(differenceRemove < bestDifference) bestDifference = differenceRemove;
    if(differenceSwap < bestDifference) bestDifference = differenceSwap;

    //Abbruchbedingungen
    if(possibleRemove.value == -1 && possibleAdd.value == -1 && possibleSwap.value == -1) {
        //Keine Zugmöglichkeit
        return Action.None;
    }

    //Abbruchbedingungen
    if(possibleRemove.value == -1) return Action.Add;
    if(possibleAdd.value == -1) bestDifference = differenceRemove;

    //Zurückgeben der besten nächsten Aktion
    if(bestDifference == differenceAdd) return Action.Add;
    else if(bestDifference == differenceRemove) return Action.Remove;
    else return Action.Swap;
}
```

Diese Funktion überprüft alle Zugmöglichkeiten vom momentanen Zustand aus und gibt den Zug zurück, welcher die Waage am weitesten ausbalanciert und die Differenz somit am kleinsten werden lässt.  
Die folgenden Zugmöglichkeiten stehen zur Auswahl:
 - eines der verfügbaren Gewichte wird auf die leichtere Seite der Waage gestellt (Add - Hinzufügen)
 - ein Gewicht wird von der schwereren Seite entfernt (Remove - Entfernen)
 - ein Gewicht wird von der schwereren Seite auf die leichtere Seite gestellt (Swap - Tauschen)

Um die beste Möglichkeit zu ermitteln, werden alle genannten Möglichkeiten mithilfe einer Kopie der Waage ausprobiert und zwischengespeichert. Zum Schluss wird dann der Zug zurückgegeben, welcher die Differenz am kleinsten werden lassen hat. Dieser Zug wird dann durch den Algorithmus auf der eigentlichen Waage ausgeführt.

```java
ArrayList<ScaleWeight> workingSource = new ArrayList<>(source); //Kopieren der Liste

//Entfernen des Gewichtes, welches zuletzt bewegt wurde, damit dieses nicht erneut verwendet werden kann
if(workingSource.contains(lastMovedWeight) && workingSource.size() > 1) workingSource.remove(lastMovedWeight);

//Initialisieren der lokalen Variablen
ScaleWeight bestWeight = workingSource.size() > 0 ? workingSource.get(0) : new ScaleWeight(-1);
int bestDiff = workingSource.size() > 0 ? Math.abs(workingSource.get(0).value - Math.abs(difference)) : -1;

//Ermitteln des besten Gewichtes
for(ScaleWeight weight : workingSource) {
    int diff = Math.abs(weight.value - Math.abs(difference));
    if(diff < bestDiff) {
        bestWeight = weight;
        bestDiff = diff;
    }
}

//Rückgabe des besten Gewichtes
return bestWeight;
```

Die Funktion Scale#getBestMove verwendet eine Hilfsfunktion, um das Gewicht zu ermitteln, welches die Differenz am weitesten ausgleichen kann. Wenn die Differenz also -100g ist und damit die rechte Seite schwerer ist, dann wird das Gewicht 100g ausgewählt, falls dies in der Liste der verfügbaren Gewichte ist. Wenn dieses Gewicht nicht existiert, dann wird das Gewicht ausgewählt, welches am nächsten an diesen 100g ist.

```java
//Links Seite ist schwerer
ScaleWeight nearestWeightAdd = getNearestWeight(availableWeights, difference);
ScaleWeight nearestWeightRemove = getNearestWeight(leftWeights, difference);
ScaleWeight nearestWeightSwap = getNearestWeight(leftWeights, difference / 2);

//Berechnen der optimalsten Aktion, welche die Waage am weitesten ausbalanciert
switch (getBestAction(nearestWeightAdd, nearestWeightRemove, nearestWeightSwap, initialWeight, true)) {
    case Add -> {
        //Das Gewicht wird auf die rechte Seite der Waage gestellt
        availableWeights.remove(nearestWeightAdd);
        rightWeights.add(nearestWeightAdd);
        lastMovedWeight = nearestWeightAdd;
    }
    case Remove -> {
        //Das Gewicht wird von der linken Seite der Waage heruntergenommen
        leftWeights.remove(nearestWeightRemove);
        availableWeights.add(nearestWeightRemove);
        lastMovedWeight = nearestWeightRemove;
    }
    case Swap -> {
        //Das Gewicht wird von der rechten Seite auf die linke Seite der Waage gestellt
        leftWeights.remove(nearestWeightSwap);
        rightWeights.add(nearestWeightSwap);
        lastMovedWeight = nearestWeightSwap;
    }
}

//Funktion wird rekursiv aufgerufen
return balanceInternal(initialWeight, availableWeights, negativeRecursions);
```

Der Ablauf des Algorithmuses, wenn die Differenz größer als 0 ist funktioniert analog zum Ablauf, wenn die Differenz kleiner als 0 ist. In diesem Fall ist jedoch die linke Seite der Waage schwerer und alle Aktionen werden folglich umgekehrt ausgeführt. Auch hier wird wieder die beste Zugmöglichkeit durch die Funktion Scale#getBestAction berechnet.

Wenn die Differenz der beiden Seiten gleich null ist, dann bedeutet dies, dass die Waage ausbalanciert ist. Folglich wird der momentane Zustand der Waage zurückgegeben und die Hauptschleife des Algorithmuses kann fortfahren.

## Beispiele

### Beispiel 1 (gewichtsstuecke0.txt)

10g: [10g] --- [10g]  
20g: [20g] --- [10g, 10g]  
30g: [30g] --- [10g, 10g, 10g]  
40g: [40g, 10g] --- [50g]  
50g: [50g] --- [50g]  
60g: [60g] --- [50g, 10g]  
70g: [70g] --- [50g, 10g, 10g]  
80g: [80g, 10g, 10g] --- [100g]  
90g: [90g, 10g] --- [100g]  
100g: [100g] --- [100g]  
110g: [110g] --- [100g, 10g]  
120g: [120g] --- [100g, 10g, 10g]  
130g: [130g] --- [100g, 10g, 10g, 10g]  
140g: [140g, 10g] --- [100g, 50g]  
150g: [150g] --- [100g, 50g]  
160g: [160g] --- [100g, 50g, 10g]  
170g: [170g] --- [100g, 50g, 10g, 10g]  
180g: [180g, 10g, 10g] --- [100g, 100g]  
190g: [190g, 10g] --- [100g, 100g]  
200g: [200g] --- [100g, 100g]  
210g: [210g] --- [100g, 100g, 10g]  
220g: [220g] --- [100g, 100g, 10g, 10g]  
230g: [230g] --- [100g, 100g, 10g, 10g, 10g]  
240g: [240g, 10g] --- [100g, 100g, 50g]  
250g: [250g] --- [100g, 100g, 50g]  
260g: [260g] --- [100g, 100g, 50g, 10g]  
270g: [270g] --- [100g, 100g, 50g, 10g, 10g]  
280g: [280g, 10g, 10g] --- [100g, 100g, 100g]  
290g: [290g, 10g] --- [100g, 100g, 100g]  
300g: [300g] --- [100g, 100g, 100g]  
310g: [310g, 100g, 100g] --- [500g, 10g]  
320g: [320g, 100g, 100g] --- [500g, 10g, 10g]  
330g: [330g, 100g, 50g, 10g, 10g] --- [500g]  
340g: [340g, 100g, 50g, 10g] --- [500g]  
350g: [350g, 100g, 50g] --- [500g]  
360g: [360g, 100g, 50g] --- [500g, 10g]  
370g: [370g, 100g, 10g, 10g, 10g] --- [500g]  
380g: [380g, 100g, 10g, 10g] --- [500g]  
390g: [390g, 100g, 10g] --- [500g]  
400g: [400g, 100g] --- [500g]  
410g: [410g, 100g] --- [500g, 10g]  
420g: [420g, 100g] --- [500g, 10g, 10g]  
430g: [430g, 50g, 10g, 10g] --- [500g]  
440g: [440g, 50g, 10g] --- [500g]  
450g: [450g, 50g] --- [500g]  
460g: [460g, 50g] --- [500g, 10g]  
470g: [470g, 10g, 10g, 10g] --- [500g]  
480g: [480g, 10g, 10g] --- [500g]  
490g: [490g, 10g] --- [500g]  
500g: [500g] --- [500g]    
9500g: [9500g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g]  
9510g: [9510g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 10g]  
9520g: [9520g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 10g, 10g]  
9530g: [9530g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 10g, 10g, 10g]  
9540g: [9540g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 50g]  
9550g: [9550g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 50g]  
9560g: [9560g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 50g, 10g]  
9570g: [9570g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 50g, 10g, 10g]  
9580g: [9580g, 10g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g]  
9590g: [9590g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g]  
9600g: [9600g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g]  
9610g: [9610g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 10g]  
9620g: [9620g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 10g, 10g]  
9630g: [9630g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 10g, 10g, 10g]  
9640g: [9640g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 50g]  
9650g: [9650g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 50g]  
9660g: [9660g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 50g, 10g]  
9670g: [9670g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 50g, 10g, 10g]  
9680g: [9680g, 10g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g]  
9690g: [9690g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g]  
9700g: [9700g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g]  
9710g: [9710g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 10g]  
9720g: [9720g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 10g, 10g]  
9730g: [9730g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 10g, 10g, 10g]  
9740g: [9740g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 50g]  
9750g: [9750g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 50g]  
9760g: [9760g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 50g, 10g]  
9770g: [9770g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 50g, 10g, 10g]  
9780g: [9780g, 10g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g]  
9790g: [9790g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g]  
9800g: [9800g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g]  
9810g: [9810g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 10g]  
9820g: [9820g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 10g, 10g]  
9830g: [9830g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 10g, 10g, 10g]  
9840g: [9840g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g]  
9850g: [9850g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g]  
9860g: [9860g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 10g]  
9870g: [9870g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 10g, 10g]  
9880g: [9880g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 10g, 10g, 10g]  
9890g: [9890g, 10g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 50g]  
9900g: [9900g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 50g]  
9910g: [9910g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 50g, 10g]  
9920g: [9920g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 50g, 10g, 10g]  
9930g: [9930g] --- [5000g, 1000g, 1000g, 1000g, 500g, 500g, 500g, 100g, 100g, 100g, 50g, 50g, 10g, 10g, 10g]  
9930g/9940g: [9940g] --- [10g, 10g, 10g, 50g, 50g, 100g, 100g, 100g, 500g, 500g, 500g, 1000g, 1000g, 1000g, 5000g]  
9930g/9950g: [9950g] --- [10g, 10g, 10g, 50g, 50g, 100g, 100g, 100g, 500g, 500g, 500g, 1000g, 1000g, 1000g, 5000g]  
9930g/9960g: [9960g] --- [10g, 10g, 10g, 50g, 50g, 100g, 100g, 100g, 500g, 500g, 500g, 1000g, 1000g, 1000g, 5000g]  
9930g/9970g: [9970g] --- [10g, 10g, 10g, 50g, 50g, 100g, 100g, 100g, 500g, 500g, 500g, 1000g, 1000g, 1000g, 5000g]  
9930g/9980g: [9980g] --- [10g, 10g, 10g, 50g, 50g, 100g, 100g, 100g, 500g, 500g, 500g, 1000g, 1000g, 1000g, 5000g]  
9930g/9990g: [9990g] --- [10g, 10g, 10g, 50g, 50g, 100g, 100g, 100g, 500g, 500g, 500g, 1000g, 1000g, 1000g, 5000g]  
9930g/10000g: [10000g] --- [10g, 10g, 10g, 50g, 50g, 100g, 100g, 100g, 500g, 500g, 500g, 1000g, 1000g, 1000g, 5000g]  

### Beispiel 2 (gewichtsstuecke1.txt)

0g/10g: [10g] --- []  
0g/20g: [20g] --- []  
42g/30g: [30g] --- [42g]  
42g/40g: [40g] --- [42g]  
42g/50g: [50g] --- [42g]  
42g/60g: [60g] --- [42g]  
84g/70g: [70g] --- [42g, 42g]  
84g/80g: [80g] --- [42g, 42g]  
127g/90g: [90g, 42g] --- [127g]  
127g/100g: [100g, 42g] --- [127g]  
127g/110g: [110g] --- [127g]  
127g/120g: [120g] --- [127g]  
127g/130g: [130g] --- [127g]  
127g/140g: [140g] --- [127g]  
169g/150g: [150g] --- [127g, 42g]  
169g/160g: [160g] --- [127g, 42g]  
169g/170g: [170g] --- [127g, 42g]  
169g/180g: [180g] --- [127g, 42g]  
169g/190g: [190g] --- [127g, 42g]  
211g/200g: [200g] --- [127g, 42g, 42g]  
211g/210g: [210g] --- [127g, 42g, 42g]  
254g/220g: [220g, 42g] --- [127g, 127g]  
254g/230g: [230g, 42g] --- [127g, 127g]  
254g/240g: [240g] --- [127g, 127g]  
371g/250g: [250g, 127g] --- [371g]  
371g/260g: [260g, 127g] --- [371g]  
413g/270g: [270g, 127g] --- [371g, 42g]  
413g/280g: [280g, 127g] --- [371g, 42g]  
371g/290g: [290g, 42g, 42g] --- [371g]  
371g/300g: [300g, 42g, 42g] --- [371g]  
371g/310g: [310g, 42g] --- [371g]  
371g/320g: [320g, 42g] --- [371g]  
371g/330g: [330g, 42g] --- [371g]  
371g/340g: [340g, 42g] --- [371g]  
371g/350g: [350g] --- [371g]  
371g/360g: [360g] --- [371g]  
371g/370g: [370g] --- [371g]  
371g/380g: [380g] --- [371g]  
371g/390g: [390g] --- [371g]  
413g/400g: [400g] --- [371g, 42g]  
413g/410g: [410g] --- [371g, 42g]  
413g/420g: [420g] --- [371g, 42g]  
413g/430g: [430g] --- [371g, 42g]  
455g/440g: [440g] --- [371g, 42g, 42g]  
455g/450g: [450g] --- [371g, 42g, 42g]  
498g/460g: [460g, 42g] --- [371g, 127g]  
498g/470g: [470g, 42g] --- [371g, 127g]  
498g/480g: [480g] --- [371g, 127g]  
498g/490g: [490g] --- [371g, 127g]  
498g/500g: [500g] --- [371g, 127g]  
10000g/9500g: [9500g, 371g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9510g: [9510g, 371g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9520g: [9520g, 371g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10042g/9530g: [9530g, 371g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10042g/9540g: [9540g, 371g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10000g/9550g: [9550g, 371g, 42g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9560g: [9560g, 371g, 42g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9570g: [9570g, 371g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9580g: [9580g, 371g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9590g: [9590g, 371g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9600g: [9600g, 371g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9610g: [9610g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9620g: [9620g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9630g: [9630g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9640g: [9640g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9650g: [9650g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10042g/9660g: [9660g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10042g/9670g: [9670g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10042g/9680g: [9680g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10042g/9690g: [9690g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10084g/9700g: [9700g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g, 42g]  
10084g/9710g: [9710g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g, 42g]  
10127g/9720g: [9720g, 371g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 127g]  
10127g/9730g: [9730g, 371g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 127g]  
10127g/9740g: [9740g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 127g]  
10127g/9750g: [9750g, 371g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 127g]  
10000g/9760g: [9760g, 127g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10042g/9770g: [9770g, 127g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10042g/9780g: [9780g, 127g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10000g/9790g: [9790g, 127g, 42g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9800g: [9800g, 127g, 42g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9810g: [9810g, 127g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9820g: [9820g, 127g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9830g: [9830g, 127g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9840g: [9840g, 127g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9850g: [9850g, 127g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9860g: [9860g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9870g: [9870g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9880g: [9880g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9890g: [9890g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10042g/9900g: [9900g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10042g/9910g: [9910g, 127g] --- [2000g, 2000g, 2000g, 2000g, 2000g, 42g]  
10000g/9920g: [9920g, 42g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9930g: [9930g, 42g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9940g: [9940g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9950g: [9950g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9960g: [9960g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9970g: [9970g, 42g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9980g: [9980g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g/9990g: [9990g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  
10000g: [10000g] --- [2000g, 2000g, 2000g, 2000g, 2000g]  

### Beispiel 3 (gewichtsstuecke2.txt)

10g: [10g] --- [10g]  
20g: [20g] --- [20g]  
30g: [30g] --- [20g, 10g]  
40g: [40g] --- [40g]  
50g: [50g] --- [40g, 10g]  
60g: [60g] --- [40g, 20g]  
70g: [70g, 10g] --- [80g]  
80g: [80g] --- [80g]  
90g: [90g] --- [80g, 10g]  
100g: [100g] --- [80g, 20g]  
110g: [110g] --- [80g, 20g, 10g]  
120g: [120g] --- [80g, 40g]  
130g: [130g, 20g, 10g] --- [160g]  
140g: [140g, 20g] --- [160g]  
150g: [150g, 10g] --- [160g]  
160g: [160g] --- [160g]  
170g: [170g] --- [160g, 10g]  
180g: [180g] --- [160g, 20g]  
190g: [190g] --- [160g, 20g, 10g]  
200g: [200g] --- [160g, 40g]  
210g: [210g] --- [160g, 40g, 10g]  
220g: [220g] --- [160g, 40g, 20g]  
230g: [230g, 10g] --- [160g, 80g]  
240g: [240g] --- [160g, 80g]  
250g: [250g, 80g] --- [320g, 10g]  
260g: [260g, 40g, 20g] --- [320g]  
270g: [270g, 40g, 10g] --- [320g]  
280g: [280g, 40g] --- [320g]  
290g: [290g, 20g, 10g] --- [320g]  
300g: [300g, 20g] --- [320g]  
310g: [310g, 10g] --- [320g]  
320g: [320g] --- [320g]  
330g: [330g] --- [320g, 10g]  
340g: [340g] --- [320g, 20g]  
350g: [350g] --- [320g, 20g, 10g]  
360g: [360g] --- [320g, 40g]  
370g: [370g] --- [320g, 40g, 10g]  
380g: [380g] --- [320g, 40g, 20g]  
390g: [390g, 10g] --- [320g, 80g]  
400g: [400g] --- [320g, 80g]  
410g: [410g] --- [320g, 80g, 10g]  
420g: [420g] --- [320g, 80g, 20g]  
430g: [430g] --- [320g, 80g, 20g, 10g]  
440g: [440g] --- [320g, 80g, 40g]  
450g: [450g, 20g, 10g] --- [320g, 160g]  
460g: [460g, 20g] --- [320g, 160g]  
470g: [470g, 10g] --- [320g, 160g]  
480g: [480g] --- [320g, 160g]  
490g: [490g, 160g] --- [640g, 10g]  
500g: [500g, 160g] --- [640g, 20g]  
9500g: [9500g, 80g, 20g] --- [5120g, 2560g, 1280g, 640g]  
9510g: [9510g, 80g, 10g] --- [5120g, 2560g, 1280g, 640g]  
9520g: [9520g, 80g] --- [5120g, 2560g, 1280g, 640g]  
9530g: [9530g, 80g] --- [5120g, 2560g, 1280g, 640g, 10g]  
9540g: [9540g, 40g, 20g] --- [5120g, 2560g, 1280g, 640g]  
9550g: [9550g, 40g, 10g] --- [5120g, 2560g, 1280g, 640g]  
9560g: [9560g, 40g] --- [5120g, 2560g, 1280g, 640g]  
9570g: [9570g, 20g, 10g] --- [5120g, 2560g, 1280g, 640g]  
9580g: [9580g, 20g] --- [5120g, 2560g, 1280g, 640g]  
9590g: [9590g, 10g] --- [5120g, 2560g, 1280g, 640g]  
9600g: [9600g] --- [5120g, 2560g, 1280g, 640g]  
9610g: [9610g] --- [5120g, 2560g, 1280g, 640g, 10g]  
9620g: [9620g] --- [5120g, 2560g, 1280g, 640g, 20g]  
9630g: [9630g] --- [5120g, 2560g, 1280g, 640g, 20g, 10g]  
9640g: [9640g] --- [5120g, 2560g, 1280g, 640g, 40g]  
9650g: [9650g] --- [5120g, 2560g, 1280g, 640g, 40g, 10g]  
9660g: [9660g] --- [5120g, 2560g, 1280g, 640g, 40g, 20g]  
9670g: [9670g, 10g] --- [5120g, 2560g, 1280g, 640g, 80g]  
9680g: [9680g] --- [5120g, 2560g, 1280g, 640g, 80g]  
9690g: [9690g] --- [5120g, 2560g, 1280g, 640g, 80g, 10g]  
9700g: [9700g] --- [5120g, 2560g, 1280g, 640g, 80g, 20g]  
9710g: [9710g] --- [5120g, 2560g, 1280g, 640g, 80g, 20g, 10g]  
9720g: [9720g] --- [5120g, 2560g, 1280g, 640g, 80g, 40g]  
9730g: [9730g, 20g, 10g] --- [5120g, 2560g, 1280g, 640g, 160g]  
9740g: [9740g, 20g] --- [5120g, 2560g, 1280g, 640g, 160g]  
9750g: [9750g, 10g] --- [5120g, 2560g, 1280g, 640g, 160g]  
9760g: [9760g] --- [5120g, 2560g, 1280g, 640g, 160g]  
9770g: [9770g] --- [5120g, 2560g, 1280g, 640g, 160g, 10g]  
9780g: [9780g] --- [5120g, 2560g, 1280g, 640g, 160g, 20g]  
9790g: [9790g] --- [5120g, 2560g, 1280g, 640g, 160g, 20g, 10g]  
9800g: [9800g] --- [5120g, 2560g, 1280g, 640g, 160g, 40g]  
9810g: [9810g] --- [5120g, 2560g, 1280g, 640g, 160g, 40g, 10g]  
9820g: [9820g] --- [5120g, 2560g, 1280g, 640g, 160g, 40g, 20g]  
9830g: [9830g, 10g] --- [5120g, 2560g, 1280g, 640g, 160g, 80g]  
9840g: [9840g] --- [5120g, 2560g, 1280g, 640g, 160g, 80g]  
9850g: [9850g, 80g] --- [5120g, 2560g, 1280g, 640g, 320g, 10g]  
9860g: [9860g, 40g, 20g] --- [5120g, 2560g, 1280g, 640g, 320g]  
9870g: [9870g, 40g, 10g] --- [5120g, 2560g, 1280g, 640g, 320g]  
9880g: [9880g, 40g] --- [5120g, 2560g, 1280g, 640g, 320g]  
9890g: [9890g, 20g, 10g] --- [5120g, 2560g, 1280g, 640g, 320g]  
9900g: [9900g, 20g] --- [5120g, 2560g, 1280g, 640g, 320g]  
9910g: [9910g, 10g] --- [5120g, 2560g, 1280g, 640g, 320g]  
9920g: [9920g] --- [5120g, 2560g, 1280g, 640g, 320g]  
9930g: [9930g] --- [5120g, 2560g, 1280g, 640g, 320g, 10g]  
9940g: [9940g] --- [5120g, 2560g, 1280g, 640g, 320g, 20g]  
9950g: [9950g] --- [5120g, 2560g, 1280g, 640g, 320g, 20g, 10g]  
9960g: [9960g] --- [5120g, 2560g, 1280g, 640g, 320g, 40g]  
9970g: [9970g] --- [5120g, 2560g, 1280g, 640g, 320g, 40g, 10g]  
9980g: [9980g] --- [5120g, 2560g, 1280g, 640g, 320g, 40g, 20g]  
9990g: [9990g, 10g] --- [5120g, 2560g, 1280g, 640g, 320g, 80g]  
10000g: [10000g] --- [5120g, 2560g, 1280g, 640g, 320g, 80g]  

### Beispiel 4 (gewichtsstuecke3.txt)

10g: [10g] --- [10g]  
20g: [20g, 10g] --- [30g]  
30g: [30g] --- [30g]  
40g: [40g] --- [30g, 10g]  
50g: [50g, 30g, 10g] --- [90g]  
60g: [60g, 30g] --- [90g]  
70g: [70g, 30g] --- [90g, 10g]  
80g: [80g, 10g] --- [90g]  
90g: [90g] --- [90g]  
100g: [100g] --- [90g, 10g]  
110g: [110g, 10g] --- [90g, 30g]  
120g: [120g] --- [90g, 30g]  
130g: [130g] --- [90g, 30g, 10g]  
140g: [140g, 90g, 30g, 10g] --- [270g]  
150g: [150g, 90g, 30g] --- [270g]  
160g: [160g, 90g, 30g] --- [10g, 270g]  
170g: [170g, 10g, 90g] --- [270g]  
180g: [180g, 90g] --- [270g]  
190g: [190g, 90g] --- [270g, 10g]  
200g: [200g, 90g, 10g] --- [270g, 30g]  
210g: [210g, 90g] --- [270g, 30g]  
220g: [220g, 90g] --- [270g, 30g, 10g]  
230g: [230g, 30g, 10g] --- [270g]  
240g: [240g, 30g] --- [270g]  
250g: [250g, 30g] --- [270g, 10g]  
260g: [260g, 10g] --- [270g]  
270g: [270g] --- [270g]  
280g: [280g] --- [270g, 10g]  
290g: [290g, 10g] --- [270g, 30g]  
300g: [300g] --- [270g, 30g]  
310g: [310g] --- [270g, 30g, 10g]  
320g: [320g, 30g, 10g] --- [270g, 90g]  
330g: [330g, 30g] --- [270g, 90g]  
340g: [340g, 30g] --- [270g, 90g, 10g]  
350g: [350g, 10g] --- [270g, 90g]  
360g: [360g] --- [270g, 90g]  
370g: [370g] --- [270g, 90g, 10g]  
380g: [380g, 10g] --- [270g, 90g, 30g]  
390g: [390g] --- [270g, 90g, 30g]  
400g: [400g] --- [270g, 90g, 30g, 10g]  
410g: [410g, 270g, 90g, 30g, 10g] --- [810g]  
420g: [420g, 270g, 90g, 30g] --- [810g]  
430g: [430g, 270g, 90g, 30g] --- [10g, 810g]  
440g: [440g, 270g, 10g, 90g] --- [810g]  
450g: [450g, 270g, 90g] --- [810g]  
460g: [460g, 270g, 90g] --- [10g, 810g]  
470g: [470g, 270g, 90g, 10g] --- [30g, 810g]  
480g: [480g, 270g, 90g] --- [30g, 810g]  
490g: [490g, 270g, 90g] --- [30g, 10g, 810g]  
500g: [500g, 270g, 30g, 10g] --- [810g]  
9500g: [9500g, 270g, 30g, 10g] --- [7290g, 2430g, 90g]  
9510g: [9510g, 270g, 30g] --- [7290g, 2430g, 90g]  
9520g: [9520g, 270g, 30g] --- [7290g, 2430g, 90g, 10g]  
9530g: [9530g, 270g, 10g] --- [7290g, 2430g, 90g]  
9540g: [9540g, 270g] --- [7290g, 2430g, 90g]  
9550g: [9550g, 270g] --- [7290g, 2430g, 10g, 90g]  
9560g: [9560g, 10g, 270g] --- [7290g, 2430g, 90g, 30g]  
9570g: [9570g, 270g] --- [7290g, 2430g, 90g, 30g]  
9580g: [9580g, 270g] --- [7290g, 2430g, 90g, 30g, 10g]  
9590g: [9590g, 90g, 30g, 10g] --- [7290g, 2430g]  
9600g: [9600g, 90g, 30g] --- [7290g, 2430g]  
9610g: [9610g, 90g, 30g] --- [7290g, 2430g, 10g]  
9620g: [9620g, 90g, 10g] --- [7290g, 2430g]  
9630g: [9630g, 90g] --- [7290g, 2430g]  
9640g: [9640g, 90g] --- [7290g, 2430g, 10g]  
9650g: [9650g, 90g, 10g] --- [7290g, 2430g, 30g]  
9660g: [9660g, 90g] --- [7290g, 2430g, 30g]  
9670g: [9670g, 90g] --- [7290g, 2430g, 30g, 10g]  
9680g: [9680g, 30g, 10g] --- [7290g, 2430g]  
9690g: [9690g, 30g] --- [7290g, 2430g]  
9700g: [9700g, 30g] --- [7290g, 2430g, 10g]  
9710g: [9710g, 10g] --- [7290g, 2430g]  
9720g: [9720g] --- [7290g, 2430g]  
9730g: [9730g] --- [7290g, 2430g, 10g]  
9740g: [9740g, 10g] --- [7290g, 2430g, 30g]  
9750g: [9750g] --- [7290g, 2430g, 30g]  
9760g: [9760g] --- [7290g, 2430g, 30g, 10g]  
9770g: [9770g, 30g, 10g] --- [7290g, 2430g, 90g]  
9780g: [9780g, 30g] --- [7290g, 2430g, 90g]  
9790g: [9790g, 30g] --- [7290g, 2430g, 90g, 10g]  
9800g: [9800g, 10g] --- [7290g, 2430g, 90g]  
9810g: [9810g] --- [7290g, 2430g, 90g]  
9820g: [9820g] --- [7290g, 2430g, 90g, 10g]  
9830g: [9830g, 10g] --- [7290g, 2430g, 90g, 30g]  
9840g: [9840g] --- [7290g, 2430g, 90g, 30g]  
9850g: [9850g] --- [7290g, 2430g, 90g, 30g, 10g]  
9860g: [9860g, 90g, 30g, 10g] --- [7290g, 2430g, 270g]  
9870g: [9870g, 90g, 30g] --- [7290g, 2430g, 270g]  
9880g: [9880g, 90g, 30g] --- [7290g, 2430g, 10g, 270g]  
9890g: [9890g, 10g, 90g] --- [7290g, 2430g, 270g]  
9900g: [9900g, 90g] --- [7290g, 2430g, 270g]  
9910g: [9910g, 90g] --- [7290g, 2430g, 270g, 10g]  
9920g: [9920g, 90g, 10g] --- [7290g, 2430g, 270g, 30g]  
9930g: [9930g, 90g] --- [7290g, 2430g, 270g, 30g]  
9940g: [9940g, 90g] --- [7290g, 2430g, 270g, 30g, 10g]  
9950g: [9950g, 30g, 10g] --- [7290g, 2430g, 270g]  
9960g: [9960g, 30g] --- [7290g, 2430g, 270g]  
9970g: [9970g, 30g] --- [7290g, 2430g, 270g, 10g]  
9980g: [9980g, 10g] --- [7290g, 2430g, 270g]  
9990g: [9990g] --- [7290g, 2430g, 270g]  
10000g: [10000g] --- [7290g, 2430g, 270g, 10g]  

### Beispiel 5 (gewichtsstuecke4.txt)

5g/10g: [10g] --- [5g]  
21g/20g: [20g] --- [21g]  
29g/30g: [30g] --- [29g]  
50g/40g: [40g, 5g] --- [29g, 21g]  
50g: [50g] --- [29g, 21g]  
58g/60g: [60g] --- [29g, 29g]  
79g/70g: [70g, 5g] --- [29g, 29g, 21g]  
79g/80g: [80g] --- [29g, 29g, 21g]  
92g/90g: [90g] --- [29g, 29g, 29g, 5g]  
108g/100g: [100g, 5g] --- [29g, 29g, 29g, 21g]  
108g/110g: [110g] --- [29g, 29g, 29g, 21g]  
129g/120g: [120g, 5g] --- [29g, 29g, 29g, 21g, 21g]  
129g/130g: [130g] --- [29g, 29g, 29g, 21g, 21g]  
150g/140g: [140g, 5g] --- [29g, 29g, 29g, 21g, 21g, 21g]  
259g/150g: [150g, 29g, 29g, 29g, 21g] --- [259g]  
264g/160g: [160g, 29g, 29g, 29g, 21g] --- [259g, 5g]  
259g/170g: [170g, 29g, 29g, 29g] --- [259g]  
180g: [180g, 29g, 29g, 21g] --- [259g]  
264g/190g: [190g, 29g, 29g, 21g] --- [259g, 5g]  
259g/200g: [200g, 29g, 29g] --- [259g]  
259g/210g: [210g, 29g, 21g] --- [259g]  
259g/220g: [220g, 29g, 5g] --- [259g]  
230g: [230g, 29g] --- [259g]  
259g/240g: [240g, 21g] --- [259g]  
259g/250g: [250g, 5g] --- [259g]  
259g/260g: [260g] --- [259g]  
280g/270g: [270g, 5g] --- [259g, 21g]  
287g/280g: [280g, 5g] --- [287g]  
292g/290g: [290g] --- [287g, 5g]  
308g/300g: [300g, 5g] --- [287g, 21g]  
308g/310g: [310g] --- [287g, 21g]  
321g/320g: [320g] --- [287g, 29g, 5g]  
337g/330g: [330g, 5g] --- [287g, 29g, 21g]  
342g/340g: [340g] --- [287g, 29g, 21g, 5g]  
399g/350g: [350g, 29g, 21g] --- [399g]  
399g/360g: [360g, 29g, 5g] --- [399g]  
370g: [370g, 29g] --- [399g]  
399g/380g: [380g, 21g] --- [399g]  
399g/390g: [390g, 5g] --- [399g]  
399g/400g: [400g] --- [399g]  
420g/410g: [410g, 5g] --- [399g, 21g]  
420g: [420g] --- [399g, 21g]  
428g/430g: [430g] --- [399g, 29g]  
449g/440g: [440g, 5g] --- [399g, 29g, 21g]  
449g/450g: [450g] --- [399g, 29g, 21g]  
462g/460g: [460g] --- [399g, 29g, 29g, 5g]  
478g/470g: [470g, 5g] --- [399g, 29g, 29g, 21g]  
478g/480g: [480g] --- [399g, 29g, 29g, 21g]  
491g/490g: [490g] --- [399g, 29g, 29g, 29g, 5g]  
507g/500g: [500g, 5g] --- [399g, 29g, 29g, 29g, 21g]  
9507g/9500g: [9500g, 5g] --- [2993g, 2993g, 2993g, 399g, 29g, 29g, 29g, 21g, 21g]  
9512g/9510g: [9510g] --- [2993g, 2993g, 2993g, 399g, 29g, 29g, 29g, 21g, 21g, 5g]  
9528g/9520g: [9520g, 5g] --- [2993g, 2993g, 2993g, 399g, 29g, 29g, 29g, 21g, 21g, 21g]  
9637g/9530g: [9530g, 29g, 29g, 29g, 21g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9637g/9540g: [9540g, 29g, 29g, 29g, 5g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9550g: [9550g, 29g, 29g, 29g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9637g/9560g: [9560g, 29g, 29g, 21g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9637g/9570g: [9570g, 29g, 29g, 5g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9637g/9580g: [9580g, 29g, 29g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9642g/9590g: [9590g, 29g, 21g] --- [2993g, 2993g, 2993g, 399g, 259g, 5g]  
9637g/9600g: [9600g, 29g, 5g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9637g/9610g: [9610g, 29g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9642g/9620g: [9620g, 21g] --- [2993g, 2993g, 2993g, 399g, 259g, 5g]  
9637g/9630g: [9630g, 5g] --- [2993g, 2993g, 2993g, 399g, 259g]  
9642g/9640g: [9640g] --- [2993g, 2993g, 2993g, 399g, 259g, 5g]  
9658g/9650g: [9650g, 5g] --- [2993g, 2993g, 2993g, 399g, 259g, 21g]  
9660g: [9660g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g]  
9670g: [9670g] --- [2993g, 2993g, 2993g, 399g, 287g, 5g]  
9686g/9680g: [9680g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 21g]  
9691g/9690g: [9690g] --- [2993g, 2993g, 2993g, 399g, 287g, 21g, 5g]  
9699g/9700g: [9700g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 5g]  
9710g: [9710g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 21g]  
9723g/9720g: [9720g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g]  
9728g/9730g: [9730g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 5g]  
9744g/9740g: [9740g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 21g]  
9752g/9750g: [9750g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 29g]  
9757g/9760g: [9760g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 29g, 5g]  
9773g/9770g: [9770g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 29g, 21g]  
9778g/9780g: [9780g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 29g, 21g, 5g]  
9794g/9790g: [9790g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 29g, 21g, 21g]  
9799g/9800g: [9800g] --- [2993g, 2993g, 2993g, 399g, 287g, 29g, 29g, 29g, 21g, 21g, 5g]  
9924g/9810g: [9810g, 29g, 29g, 29g, 21g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g]  
9929g/9820g: [9820g, 29g, 29g, 29g, 21g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 5g]  
9924g/9830g: [9830g, 29g, 29g, 29g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g]  
9929g/9840g: [9840g, 29g, 29g, 29g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 5g]  
9850g: [9850g, 29g, 29g, 21g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 5g]  
9924g/9860g: [9860g, 29g, 29g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g]  
9924g/9870g: [9870g, 29g, 21g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g]  
9929g/9880g: [9880g, 29g, 21g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 5g]  
9890g: [9890g, 29g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g]  
9924g/9900g: [9900g, 21g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g]  
9929g/9910g: [9910g, 21g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 5g]  
9924g/9920g: [9920g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g]  
9929g/9930g: [9930g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 5g]  
9940g: [9940g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 21g]  
9953g/9950g: [9950g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 29g]  
9958g/9960g: [9960g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 29g, 5g]  
9974g/9970g: [9970g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 29g, 21g]  
9982g/9980g: [9980g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 29g, 29g]  
9987g/9990g: [9990g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 29g, 29g, 5g]  
10003g/10000g: [10000g, 5g] --- [2993g, 2993g, 2993g, 399g, 287g, 259g, 29g, 29g, 21g]  

### Beispiel 6 (gewichtsstuecke5.txt)

11g/10g: [10g] --- [11g]  
20g: [20g, 99480g, 11g] --- [99511g]  
99511g/30g: [30g, 99480g] --- [99511g]  
99522g/40g: [40g, 99480g] --- [11g, 99511g]  
99522g/50g: [50g, 99480g] --- [11g, 99511g]  
99522g/60g: [60g, 99480g] --- [11g, 99511g]  
99522g/70g: [70g, 99480g] --- [11g, 99511g]  
99522g/80g: [80g, 99480g] --- [11g, 99511g]  
99522g/90g: [90g, 99480g] --- [11g, 99511g]  
99522g/100g: [100g, 99480g] --- [11g, 99511g]  
99522g/110g: [110g, 99480g] --- [11g, 99511g]  
99522g/120g: [120g, 99480g] --- [11g, 99511g]  
99522g/130g: [130g, 99480g] --- [11g, 99511g]  
99522g/140g: [140g, 99480g] --- [11g, 99511g]  
99522g/150g: [150g, 99480g] --- [11g, 99511g]  
99522g/160g: [160g, 99480g] --- [11g, 99511g]  
99522g/170g: [170g, 99480g] --- [11g, 99511g]  
99522g/180g: [180g, 99480g] --- [11g, 99511g]  
99522g/190g: [190g, 99480g] --- [11g, 99511g]  
99522g/200g: [200g, 99480g] --- [11g, 99511g]  
99522g/210g: [210g, 99480g] --- [11g, 99511g]  
99522g/220g: [220g, 99480g] --- [11g, 99511g]  
99522g/230g: [230g, 99480g] --- [11g, 99511g]  
99522g/240g: [240g, 99480g] --- [11g, 99511g]  
99522g/250g: [250g, 99480g] --- [11g, 99511g]  
99522g/260g: [260g, 99480g] --- [11g, 99511g]  
99522g/270g: [270g, 99480g] --- [11g, 99511g]  
99522g/280g: [280g, 99480g] --- [11g, 99511g]  
99522g/290g: [290g, 99480g] --- [11g, 99511g]  
99522g/300g: [300g, 99480g] --- [11g, 99511g]  
99522g/310g: [310g, 99480g] --- [11g, 99511g]  
99522g/320g: [320g, 99480g] --- [11g, 99511g]  
99522g/330g: [330g, 99480g] --- [11g, 99511g]  
99522g/340g: [340g, 99480g] --- [11g, 99511g]  
99522g/350g: [350g, 99480g] --- [11g, 99511g]  
99522g/360g: [360g, 99480g] --- [11g, 99511g]  
99522g/370g: [370g, 99480g] --- [11g, 99511g]  
99522g/380g: [380g, 99480g] --- [11g, 99511g]  
99522g/390g: [390g, 99480g] --- [11g, 99511g]  
99522g/400g: [400g, 99480g] --- [11g, 99511g]  
99522g/410g: [410g, 99480g] --- [11g, 99511g]  
99522g/420g: [420g, 99480g] --- [11g, 99511g]  
99522g/430g: [430g, 99480g] --- [11g, 99511g]  
99522g/440g: [440g, 99480g] --- [11g, 99511g]  
99522g/450g: [450g, 99480g] --- [11g, 99511g]  
99522g/460g: [460g, 99480g] --- [11g, 99511g]  
99522g/470g: [470g, 99480g] --- [11g, 99511g]  
99522g/480g: [480g, 99480g] --- [11g, 99511g]  
99522g/490g: [490g, 99480g] --- [11g, 99511g]  
99522g/500g: [500g, 99480g] --- [11g, 99511g]  
99522g/9500g: [9500g, 99480g] --- [11g, 99511g]  
99522g/9510g: [9510g, 99480g] --- [11g, 99511g]  
99522g/9520g: [9520g, 99480g] --- [11g, 99511g]  
99522g/9530g: [9530g, 99480g] --- [11g, 99511g]  
99522g/9540g: [9540g, 99480g] --- [11g, 99511g]  
99522g/9550g: [9550g, 99480g] --- [11g, 99511g]  
99522g/9560g: [9560g, 99480g] --- [11g, 99511g]  
99522g/9570g: [9570g, 99480g] --- [11g, 99511g]  
99522g/9580g: [9580g, 99480g] --- [11g, 99511g]  
99522g/9590g: [9590g, 99480g] --- [11g, 99511g]  
99522g/9600g: [9600g, 99480g] --- [11g, 99511g]  
99522g/9610g: [9610g, 99480g] --- [11g, 99511g]  
99522g/9620g: [9620g, 99480g] --- [11g, 99511g]  
99522g/9630g: [9630g, 99480g] --- [11g, 99511g]  
99522g/9640g: [9640g, 99480g] --- [11g, 99511g]  
99522g/9650g: [9650g, 99480g] --- [11g, 99511g]  
99522g/9660g: [9660g, 99480g] --- [11g, 99511g]  
99522g/9670g: [9670g, 99480g] --- [11g, 99511g]  
99522g/9680g: [9680g, 99480g] --- [11g, 99511g]  
99522g/9690g: [9690g, 99480g] --- [11g, 99511g]  
99522g/9700g: [9700g, 99480g] --- [11g, 99511g]  
99522g/9710g: [9710g, 99480g] --- [11g, 99511g]  
99522g/9720g: [9720g, 99480g] --- [11g, 99511g]  
99522g/9730g: [9730g, 99480g] --- [11g, 99511g]  
99522g/9740g: [9740g, 99480g] --- [11g, 99511g]  
99522g/9750g: [9750g, 99480g] --- [11g, 99511g]  
99522g/9760g: [9760g, 99480g] --- [11g, 99511g]  
99522g/9770g: [9770g, 99480g] --- [11g, 99511g]  
99522g/9780g: [9780g, 99480g] --- [11g, 99511g]  
99522g/9790g: [9790g, 99480g] --- [11g, 99511g]  
99522g/9800g: [9800g, 99480g] --- [11g, 99511g]  
99522g/9810g: [9810g, 99480g] --- [11g, 99511g]  
99522g/9820g: [9820g, 99480g] --- [11g, 99511g]  
99522g/9830g: [9830g, 99480g] --- [11g, 99511g]  
99522g/9840g: [9840g, 99480g] --- [11g, 99511g]  
99522g/9850g: [9850g, 99480g] --- [11g, 99511g]  
99522g/9860g: [9860g, 99480g] --- [11g, 99511g]  
99522g/9870g: [9870g, 99480g] --- [11g, 99511g]  
99522g/9880g: [9880g, 99480g] --- [11g, 99511g]  
99522g/9890g: [9890g, 99480g] --- [11g, 99511g]  
99522g/9900g: [9900g, 99480g] --- [11g, 99511g]  
99522g/9910g: [9910g, 99480g] --- [11g, 99511g]  
99522g/9920g: [9920g, 99480g] --- [11g, 99511g]  
99522g/9930g: [9930g, 99480g] --- [11g, 99511g]  
99522g/9940g: [9940g, 99480g] --- [11g, 99511g]  
99522g/9950g: [9950g, 99480g] --- [11g, 99511g]  
99522g/9960g: [9960g, 99480g] --- [11g, 99511g]  
99522g/9970g: [9970g, 99480g] --- [11g, 99511g]  
99522g/9980g: [9980g, 99480g] --- [11g, 99511g]  
99522g/9990g: [9990g, 99480g] --- [11g, 99511g]  
99522g/10000g: [10000g, 99480g] --- [11g, 99511g]  