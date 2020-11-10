package works2;

public class Main {
    public static void main(String[] args) throws Exception {
        Space space = Space.getSpace();
        space.evolve();
        Space.output2File();
    }
}
