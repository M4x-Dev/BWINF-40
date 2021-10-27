package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public record WordPosition(int positionX, int positionY, generator.WordPosition.Orientation orientation) {

    public enum Orientation {
        Horizontal,
        Vertical,
        DiagonalUp,
        DiagonalDown
    }

    public static Orientation randomOrientation(Orientation exclude) {
        ArrayList<Orientation> possibleOrientations = new ArrayList<>(List.of(Orientation.values()));
        possibleOrientations.remove(exclude);
        return possibleOrientations.get(new Random().nextInt(possibleOrientations.size()));
    }

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
                        return positionX + (crossingIndexA - crossingIndexB);
                    case DiagonalDown:
                        return positionX - (crossingIndexA + crossingIndexB);
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
                        return positionY - (crossingIndexA + crossingIndexB);
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
                        return positionY - crossingIndexB;
                    case DiagonalUp:
                        return positionY + (crossingIndexA + crossingIndexB);
                }
                break;
        }

        return positionY;
    }

}
