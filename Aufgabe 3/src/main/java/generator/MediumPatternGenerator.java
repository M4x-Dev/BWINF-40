package generator;

public class MediumPatternGenerator extends EasyPatternGenerator {

    public MediumPatternGenerator(String filePath) {
        super(filePath);
    }

    @Override
    public String generatePattern() {
        super.generatePattern();

        return formatMatrix(pattern);
    }

    @Override
    protected void fillEmptySpaces() {

    }

}
