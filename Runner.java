import java.util.*;

public class Runner {
    public static void main(String[] args) {
        InMemoryFileSystem inMemfs = new InMemoryFileSystem();
        Tests.runTests(inMemfs);
    }
}