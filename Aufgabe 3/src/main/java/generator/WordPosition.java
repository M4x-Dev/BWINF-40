package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasse, welche die Positionierung eines Wortes innerhalb des Wortfeldes definiert.
 * Dazu zählt die Position des Wortes (X-Koorindate, Y-Koorindate) und die Ausrichtung (Horizontal, Vertikal, Diagonal).
 */
public record WordPosition(int positionX, int positionY, generator.WordPosition.Orientation orientation) {

    public enum Orientation {
        Horizontal,
        Vertical,
        DiagonalUp,
        DiagonalDown
    }

    /**
     * Statische Funktion, welche eine zufällige Ausrichtung mit einer Ausnahme generiert.
     * Diese Funktion wird verwendet, wenn sich zwei Wörter schneiden sollen.
     * Damit sich zwei Wörter schneiden können, dürfen diese nicht die gleiche Ausrichtung haben.
     *
     * @param exclude Ausrichtung, welche nicht generiert werden soll.
     *
     * @return Gibt die zufällige Ausrichtung zurück.
     */
    public static Orientation randomOrientation(Orientation exclude) {
        ArrayList<Orientation> possibleOrientations = new ArrayList<>(List.of(Orientation.values()));
        possibleOrientations.remove(exclude);
        return possibleOrientations.get(new Random().nextInt(possibleOrientations.size()));
    }

    /**
     * Funktion, welche die X-Koordinate eines Wortes berechnet, welches ein anderes schneiden soll.
     * Dazu wird der Schnittpunkt (crosingIndexA, crossingIndexB) und die Ausrichtung des ursprünglichen Wortes verwendet.
     *
     * @param crossingIndexA Schneidender Buchstabe des ersten Wortes.
     * @param crossingIndexB Schneidender Buchttabe des zweiten Wortes.
     * @param crossOrientation Ausrichtung des überschneidenden Wortes.
     *
     * @return Gibt die X-Koorindate des schneidenden Wortes zurück.
     */
    public int crossWordX(int crossingIndexA, int crossingIndexB, Orientation crossOrientation) {
        switch(orientation()) {
            case Horizontal:
                switch(crossOrientation) {
                    case Vertical:
                        return positionX + crossingIndexA;
                    case DiagonalUp:
                    case DiagonalDown:
                        return positionX + (crossingIndexA - crossingIndexB);
                }
                break;
            case Vertical:
                switch(crossOrientation) {
                    case Horizontal:
                    case DiagonalUp:
                    case DiagonalDown:
                        return positionX - crossingIndexB;
                }
                break;
            case DiagonalUp:
                switch(crossOrientation) {
                    case Vertical:
                        return positionX + crossingIndexA;
                    case Horizontal:
                    case DiagonalDown:
                        return positionX + (crossingIndexA - crossingIndexB);
                }
                break;
            case DiagonalDown:
                switch(crossOrientation) {
                    case Vertical:
                        return positionX + crossingIndexA;
                    case Horizontal:
                        return positionX - (crossingIndexA - crossingIndexB);
                    case DiagonalUp:
                        return positionX + (crossingIndexA - crossingIndexB);
                }
                break;
        }

        return positionX;
    }

    /**
     * Funktion, welche die Y-Koordinate eines Wortes berechnet, welches ein anderes schneiden soll.
     * Dazu wird der Schnittpunkt (crosingIndexA, crossingIndexB) und die Ausrichtung des ursprünglichen Wortes verwendet.
     *
     * @param crossingIndexA Schneidender Buchstabe des ersten Wortes.
     * @param crossingIndexB Schneidender Buchttabe des zweiten Wortes.
     * @param crossOrientation Ausrichtung des überschneidenden Wortes.
     *
     * @return Gibt die Y-Koorindate des schneidenden Wortes zurück.
     */
    public int crossWordY(int crossingIndexA, int crossingIndexB, Orientation crossOrientation) {
        switch(orientation()) {
            case Horizontal:
                switch(crossOrientation) {
                    case Vertical:
                    case DiagonalDown:
                        return positionY - crossingIndexB;
                    case DiagonalUp:
                        return positionY + crossingIndexB;
                }
                break;
            case Vertical:
                switch(crossOrientation) {
                    case Horizontal:
                        return positionY + crossingIndexA;
                    case DiagonalUp:
                        return positionY + (crossingIndexA + crossingIndexB);
                    case DiagonalDown:
                        return positionY + (crossingIndexA - crossingIndexB);
                }
                break;
            case DiagonalUp:
                switch(crossOrientation) {
                    case Vertical:
                    case DiagonalDown:
                        return positionY - (crossingIndexA + crossingIndexB);
                    case Horizontal:
                        return positionY - crossingIndexB;
                }
                break;
            case DiagonalDown:
                switch(crossOrientation) {
                    case Vertical:
                        return positionY + (crossingIndexA - crossingIndexB);
                    case Horizontal:
                        return positionY + crossingIndexB;
                    case DiagonalUp:
                        return positionY + (crossingIndexA + crossingIndexB);
                }
                break;
        }

        return positionY;
    }

}
