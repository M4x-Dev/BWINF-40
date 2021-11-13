import java.util.ArrayList;

/**
 * Klasse, welche eine Marktwaage, wie in der Aufgabenstellung beschrieben, darstellt.
 */
public class Scale {

    private static final int NEGATIVE_RECURSIONS_MAX = 10; //Höchstzahl an Durchgängen ohne Bewegung

    public final ArrayList<ScaleWeight> leftWeights = new ArrayList<>(); //Linke Seite der Waage
    public final ArrayList<ScaleWeight> rightWeights = new ArrayList<>(); //Rechte Seite der Waage

    private ScaleWeight lastMovedWeight; //Gewicht, welches zuletzt bewegt wurde
    private ScaleState bestState; //Zustand der Waage, welcher am nächsten an das Zielgewicht herankam

    /**
     * Hauptfunktion, welche die Waage ausbalanciert.
     * Dabei ist ein Startgewicht gegeben, welches auf der linken Seite der Waage positioniert wird.
     * Außerdem darf während dieses Vorganges nur eine bestimmte Liste an Gewichten verwendet werden.
     *
     * @param initialWeight Startgewicht, welches auf der linken Seite positioniert wird.
     * @param availableWeights Gewichte, welche verwendet werden dürfen.
     *
     * @return Gibt den finalen Zustand der Waage zurück, welcher ermittelt wurde.
     */
    public ScaleState balance(int initialWeight, ArrayList<ScaleWeight> availableWeights) {
        println("");
        return balanceInternal(initialWeight, availableWeights, 0).finalize(initialWeight); //Ausbalancieren der Waage
    }

    /**
     * Interne Funktion, welche zum Ausbalancieren der Waage verwendet wird.
     * Diese Funktion wird so oft aufgerufen, bis die Waage ausbalanciert ist, oder eine bestimmte Abbruchbedingung erfüllt wurde.
     *
     * <br><br>Dabei geht der Algorithmus wie folgt vor:
     * <br>0. Wenn die Waage nicht ausbalanciert werden kann, wird der momentane Zustand zurückgegeben
     * <br>1. Berechnung der Differenz der beiden Seiten
     * <br>2. Wenn die Differenz negativ ist, ist die rechte Seite der Waage schwerer
     * <br>&#09; 2a. Berechnen der Gewichte, welche am nächsten an der Differenz liegen
     * <br>&#09; 2b. Berechnen des optimalsten Zuges, welcher die Waage am weitesten ausgleicht
     * <br>&#09; &#09; I) Ein Gewicht aus der verfügbaren Liste wird auf die linke Seite der Waage gestellt
     * <br>&#09; &#09; II) Ein Gewicht von der rechten Seite der Waage wird entfernt
     * <br>&#09; &#09; III) Ein Gewicht von der rechten Seite wird auf die linke Seite der Waage gestellt
     * <br>&#09; 2c. Rekursiver Aufruf der Funktion
     * <br>3. Wenn die Differenz positiv ist, ist die linke Seite der Waage schwerer
     * <br>&#09; 3a. Berechnen der Gewichte, welche am nächsten an der Differenz liegen
     * <br>&#09; 3b. Berechnen des optimalsten Zuges, welcher die Waage am weitesten ausgleicht
     * <br>&#09; &#09; I) Ein Gewicht aus der verfügbaren Liste wird auf die rechte Seite der Waage gestellt
     * <br>&#09; &#09; II) Ein Gewicht von der linken Seite der Waage wird entfernt
     * <br>&#09; &#09; III) Ein Gewicht von der linken Seite wird auf die rechte Seite der Waage gestellt
     * <br>&#09; 3c: Rekursiver Aufruf der Funktion
     * <br>4. Wenn die Differenz gleich null ist, dann ist die Waage ausbalanciert und der Zustand wird zurückgegeben
     *
     * @param initialWeight Startgewicht, welches sich auf der linken Seite befindet.
     * @param availableWeights Liste an verfügbaren Gewichten, welche verwendet werden dürfen.
     * @param negativeRecursions Rekursion, welche ohne Fortschritt durchlaufen wurden.
     *
     * @return Gibt den finalen Zustand der Waage zurück.
     */
    public ScaleState balanceInternal(int initialWeight, ArrayList<ScaleWeight> availableWeights, int negativeRecursions) {
        int difference = getPlatformDifference(initialWeight); //Differenz der beiden Seiten der Waage

        println(leftWeights + " --- " + rightWeights + " | " + difference);

        //Wenn das Zielgewicht größer ist, als die Summe der verfügbaren Gewichte, kann die Waage nicht ausbalanciert werden
        if(initialWeight > sumWeights(availableWeights) && rightWeights.size() == 0) return new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(availableWeights), false);

        if(bestState == null || (Math.abs(difference) < Math.abs(bestState.difference(initialWeight)))) bestState = new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), false);
        else negativeRecursions++;

        if(negativeRecursions > NEGATIVE_RECURSIONS_MAX) return bestState; //Rückgabe des Zustandes, wenn die Waage nicht weiter ausbalanciert werden kann.

        if(difference > 0) {
            //Rechte Seite ist schwerer
            println("Right side is heavier");
            ScaleWeight nearestWeightAdd = getNearestWeight(availableWeights, difference); //Gewicht, welches hinzugefügt werden könnte
            ScaleWeight nearestWeightRemove = getNearestWeight(rightWeights, difference); //Gewicht, welches heruntergenommen werden könnte
            ScaleWeight nearestWeightSwap = getNearestWeight(rightWeights, difference / 2); //Gewicht, welches verschoben werden könnte

            //Berechnen der optimalsten Aktion, welche die Waage am weitesten ausbalanciert
            switch (getBestAction(nearestWeightAdd, nearestWeightRemove, nearestWeightSwap, initialWeight, false)) {
                case Add -> {
                    //Das Gewicht wird auf die linke Seite der Waage gestellt
                    println("Adding " + nearestWeightAdd + " from open set");
                    availableWeights.remove(nearestWeightAdd);
                    leftWeights.add(nearestWeightAdd);
                    lastMovedWeight = nearestWeightAdd;
                }
                case Remove -> {
                    //Das Gewicht wird von der rechten Seite der Waage heruntergenommen
                    println("Removing " + nearestWeightRemove + " from right side");
                    rightWeights.remove(nearestWeightRemove);
                    availableWeights.add(nearestWeightRemove);
                    lastMovedWeight = nearestWeightRemove;
                }
                case Swap -> {
                    //Das Gewicht wird von der rechten Seite auf die linke Seite der Waage gestellt
                    println("Swapping " + nearestWeightSwap + " from right side to left side");
                    rightWeights.remove(nearestWeightSwap);
                    leftWeights.add(nearestWeightSwap);
                    lastMovedWeight = nearestWeightSwap;
                }
            }

            //Funktion wird rekursiv aufgerufen
            return balanceInternal(initialWeight, availableWeights, negativeRecursions);
        } else if(difference < 0) {
            //Links Seite ist schwerer
            println("Left side is heavier");
            ScaleWeight nearestWeightAdd = getNearestWeight(availableWeights, difference);
            ScaleWeight nearestWeightRemove = getNearestWeight(leftWeights, difference);
            ScaleWeight nearestWeightSwap = getNearestWeight(leftWeights, difference / 2);

            //Berechnen der optimalsten Aktion, welche die Waage am weitesten ausbalanciert
            switch (getBestAction(nearestWeightAdd, nearestWeightRemove, nearestWeightSwap, initialWeight, true)) {
                case Add -> {
                    //Das Gewicht wird auf die rechte Seite der Waage gestellt
                    println("Adding " + nearestWeightAdd + " from open set");
                    availableWeights.remove(nearestWeightAdd);
                    rightWeights.add(nearestWeightAdd);
                    lastMovedWeight = nearestWeightAdd;
                }
                case Remove -> {
                    //Das Gewicht wird von der linken Seite der Waage heruntergenommen
                    println("Removing " + nearestWeightRemove + " from left side");
                    leftWeights.remove(nearestWeightRemove);
                    availableWeights.add(nearestWeightRemove);
                    lastMovedWeight = nearestWeightRemove;
                }
                case Swap -> {
                    //Das Gewicht wird von der rechten Seite auf die linke Seite der Waage gestellt
                    println("Swapping " + nearestWeightSwap + " from left side to right side");
                    leftWeights.remove(nearestWeightSwap);
                    rightWeights.add(nearestWeightSwap);
                    lastMovedWeight = nearestWeightSwap;
                }
            }

            //Funktion wird rekursiv aufgerufen
            return balanceInternal(initialWeight, availableWeights, negativeRecursions);
        } else {
            //Gleichgewicht ist erreicht
            return new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), true);
        }
    }

    /**
     * Funktion, welche das Gewicht bestimmt, welches am nächsten an der gegebenen Differenz liegt.
     * Dazu verwendet die Funktion eine Schleife, welche alle Gewichte der gegebenen Liste durchläuft und das Gewicht ermittelt, welches am nächsten an der Differenz liegt.
     *
     * @param source Liste an verfügbaren Gewichten.
     * @param difference Differenz, welche ausgeglichen werden soll.
     *
     * @return Gibt das Gewicht zurück, welches am nähesten an der Differenz liegt.
     */
    public ScaleWeight getNearestWeight(ArrayList<ScaleWeight> source, int difference) {
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
    }

    /**
     * Enum, welcher die möglichen Aktionen der Waage definiert.
     */
    public enum Action {
        Add,
        Remove,
        Swap,
        None
    }

    /**
     * Funktion, welche die beste Folgeaktion berechnet.
     * Dabei stehen folgende Aktionen zur Verfügung:
     * <br>Hinzufügen (Add): Ein Gewicht wird auf eine Seite der Waage gelegt.
     * <br>Entfernen (Remove): Ein Gewicht wird von einer Seite der Waage entfernt.
     * <br>Tauschen (Swap): Ein Gewicht wird von der einen auf die andere Seite der Waage gelegt.
     *
     * <br><br>Um herauszufinden, welches der drei Möglichkeiten die beste ist, führt die Funktion alle drei Aktionen separat in Kopien der Waage aus und berechnet die erzielte Differenz.
     * Die Differenz, welche am niedrigsten ist, wurde durch die beste Aktion erzielt, welche anschließend zurückgegeben wird.
     *
     * @param possibleAdd Gewicht, welches theoretisch der Waage hinzugefügt werden könnte.
     * @param possibleRemove Gewicht, welches theoretisch von der Waage entfernt werden könnte.
     * @param possibleSwap Gewicht, welches theoretisch verschoben werden kann.
     * @param targetWeight Zielgewicht, welches die Seiten der Waagen erreichen sollen.
     * @param leftHeavier Gibt an, welche Seite der Waage schwerer ist (true → links).
     *
     * @return Gibt die beste Folgeaktion zurück.
     */
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
            println("No weights to add/remove. Action is skipped...");
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

    /**
     * Funktion, welche die Differenz der beiden Platformen errechnet.
     * Dabei wird das Startgewicht hinzuaddiert, da sich dieses momentan nicht auf der Waage befindet.
     * Das gegebene Startgewicht befindet sich nicht auf der Waage, damit der Algorithmus nicht zufällig beginnt, dieses zu verschieben.
     *
     * @param targetWeight Zielgewicht der Waage.
     *
     * @return Gibt die Differenz der beiden Seiten der Waage zurück.
     */
    public int getPlatformDifference(int targetWeight) {
        return sumWeights(rightWeights) - (sumWeights(leftWeights) + targetWeight);
    }

    /**
     * Funktion, welche die Differenz der beiden Platformen errechnet.
     * Dabei wird das Startgewicht hinzuaddiert, da sich dieses momentan nicht auf der Waage befindet.
     * Das gegebene Startgewicht befindet sich nicht auf der Waage, damit der Algorithmus nicht zufällig beginnt, dieses zu verschieben.
     *
     * @param customLeftWeights Gewichte der linken Seite der Waage.
     * @param customRightWeights Gewichte der rechten Seite der Waage.
     * @param targetWeight Zielgewicht der Waage.
     *
     * @return Gibt die Differenz der beiden Seiten der Waage zurück.
     */
    public static int getPlatformDifference(ArrayList<ScaleWeight> customLeftWeights, ArrayList<ScaleWeight> customRightWeights, int targetWeight) {
        return sumWeights(customRightWeights) - (sumWeights(customLeftWeights) + targetWeight);
    }

    /**
     * Funktion, welche alle Gewichte der gegebenen Liste addiert.
     *
     * @param list Liste an Gewichten, welche addiert werden sollen.
     *
     * @return Gibt die Summe aller Gewichte in der Liste zurück.
     */
    public static int sumWeights(ArrayList<ScaleWeight> list) {
        int sum = 0;

        for(ScaleWeight weight : list)
            sum += weight.value; //Hinzufügen des Wertes

        return sum; //Rückgabe der Summe
    }

    /**
     * Record, welcher den Zustand der Waage beschreibt.
     * Dieser beeinhaltet die Gewichte auf den beiden Seiten der Waage und gibt an, ob diese ausbalanciert ist.
     */
    public record ScaleState(ArrayList<ScaleWeight> leftWeights, ArrayList<ScaleWeight> rightWeights, boolean balanced) {

        /**
         * Funktion, welche das Startgewicht der Waage hinzufügt, um die Prozedur zu vervollständigen.
         *
         * @param targetWeight Startgewicht der Waage.
         *
         * @return Gibt die Instanz mit dem Startgewicht zurück (zur Verkettung).
         */
        public ScaleState finalize(int targetWeight) {
            leftWeights.add(0, new ScaleWeight(targetWeight));
            return this;
        }

        /**
         * Funktion, welche die Differenz der beiden Platformen errechnet.
         *
         * @param targetWeight Startgewicht der Waage, welches berpckschtig werden muss.
         *
         * @return Gibt die Differenz der beiden Seiten der Waage zurück.
         */
        public int difference(int targetWeight) {
            return Scale.getPlatformDifference(leftWeights, rightWeights, targetWeight);
        }

    }

    /**
     * Debug-Methode, welche etwas in der Konsole ausgibt, wenn der Debug-Modus des Programmes aktiviert ist.
     *
     * @param line Inhalt, welcher in der Konsole ausgegeben werden soll.
     */
    public static void println(String line) {
        if(Main.DEBUG_MODE) System.out.println(line);
    }

}