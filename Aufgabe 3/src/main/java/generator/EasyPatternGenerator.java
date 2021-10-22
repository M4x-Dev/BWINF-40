package generator;

public class EasyPatternGenerator extends PatternGenerator {

    public EasyPatternGenerator(String filePath) {
        super(filePath);
    }

    public String generatePattern() {
        String[][] pattern = new String[height][width];
        System.out.println(PatternGenerator.formatMatrix(pattern));
        return "";
    }

}