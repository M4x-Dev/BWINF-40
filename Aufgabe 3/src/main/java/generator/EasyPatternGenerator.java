package generator;

public class EasyPatternGenerator extends PatternGenerator {

    public EasyPatternGenerator(String filePath) {
        super(filePath);
    }

    @Override
    public String generatePattern() {
        String[][] pattern = new String[height][width];
        System.out.println(PatternGenerator.formatMatrix(pattern));
        return "";
    }

    @Override
    protected void calculateEmptySpaces(String empty) {

    }

}