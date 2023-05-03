import java.util.*;

public class Tests {
    
    public static int numOfTests = 16;
    public static int numOfSuccessfulTests = 0;

    public static void runTests(InMemoryFileSystem fs) {

        // Test the current working directory at the very beginning
       
        testGetCurrentWorkingDir(fs);
        testChangeCurrentWorkingDirectory(fs);
        testMakeDir(fs);
        testRemoveDir(fs);
        testDirContents(fs);
        testCreateFile(fs);
        testWriteFileContents(fs);
        testMoveFile(fs);
        testFindFile(fs);
        testFindMatchingFile(fs);

        System.out.println("Number of tests run: " + numOfTests);
        System.out.println("Number of tests successful: " + numOfSuccessfulTests);

    }

    public static void displayDirContents(InMemoryFileSystem fs) {
        List<String> contents = fs.getDirContents(fs.getCurrentWorkingDir());
        String prefix = "";
        System.out.print("[");
        for(String c: contents) {
            System.out.print(prefix);
            prefix = ",";
            System.out.print(c);
        }
        System.out.print("]");
    }


    public static void testGetCurrentWorkingDir(InMemoryFileSystem fs) {
        System.out.println("*****Testing GetCurrentWorkingDir*****");
        String rootDir = "/";
        
        String dir = fs.getCurrentWorkingDir();
        
        if(dir.equals(rootDir))
            System.out.println("The current directory is: " + dir);
        
        // Test changing directory to a child directory within the current working directory
        String expectedOutput = "/Test1";
        fs.mkdir("Test1");
        fs.changeCurrentWorkingDir("Test1");
        String output = fs.getCurrentWorkingDir();
        if(output.equals(expectedOutput))  {
            System.out.println("Test Successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test Failed!");
        }

        fs.changeCurrentWorkingDir("..");
        fs.removeDir("Test1");
    }   

    public static void testChangeCurrentWorkingDirectory(InMemoryFileSystem fs) {
        System.out.println("*****Testing ChangeCurrentWorkingDir*****");
        String rootDir = "/";
        String dir = fs.getCurrentWorkingDir();
        
        if(dir.equals(rootDir))
            System.out.println("The current directory is: " + dir);
        
        // Test changing directory to a child directory in the current working directory
        fs.mkdir("Homework");
        fs.mkdir("Classwork");
        fs.changeCurrentWorkingDir("Homework");
        String expectedOutput = "/Homework";
        String output = fs.getCurrentWorkingDir();        
        if(output.equals(expectedOutput)) {
            System.out.println("Test Successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test Failed!");
        }

        // Test changing directory to a given absolute path
        fs.changeCurrentWorkingDir("/Classwork");
        expectedOutput = "/Classwork";
        output = fs.getCurrentWorkingDir();
        if(output.equals(expectedOutput)) {
            System.out.println("Test Successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test Failed!");
        }

        // Test changing directory to parent directory
        fs.changeCurrentWorkingDir("..");
        expectedOutput = "/";
        output = fs.getCurrentWorkingDir();
        if(output.equals(expectedOutput)) {
            System.out.println("Test Successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test Failed!");
        }

        //Cleanup
        fs.removeDir("Classwork");
        fs.removeDir("Homework");
    }

    // Test make directory. 
    public static void testMakeDir(InMemoryFileSystem fs) {
        System.out.println("*****Testing MakeDir*****");
        String dirName = "/FirstLevelDir/SecondLevelDir";
        fs.mkdir(dirName);
        String expectedOutput = "SecondLevelDir";
        List<String> output = fs.getDirContents("/FirstLevelDir/");
        
        if(output.get(0).equals(expectedOutput)) {
            System.out.println("Test Successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test Failed!");
        }

        // Cleanup
        fs.removeDir("/FirstLevelDir");
    }

    public static void testRemoveDir(InMemoryFileSystem fs) {
        System.out.println("*****Testing RemoveDir*****");

        //Setup
        List<String> expectedOutput = new ArrayList<String>();
        fs.mkdir("/TestDir/Test");
        displayDirContents(fs);

        // Main test
        fs.removeDir("/TestDir");
        List<String> output = fs.getDirContents(fs.getCurrentWorkingDir());
        if(output.equals(expectedOutput)) {
            System.out.println("Test Successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test Failed!");
        }

        // Test removing non-existent directory - Displays Exception message
        fs.removeDir("/TestDir");

        output = fs.getDirContents(fs.getCurrentWorkingDir());
        if(output.equals(expectedOutput)) {
            System.out.println("Test Successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test Failed!");
        }

    }
    
    public static void testDirContents(InMemoryFileSystem fs) {
        System.out.println("*****Testing GetDirContents*****");
        // Setup
        List<String> expectedOutput = new ArrayList<String>();
        expectedOutput.add("Classwork");
        expectedOutput.add("Homework");
        List<String> output = fs.getDirContents(fs.getCurrentWorkingDir());
        if(output.equals(new ArrayList<String>())) {
            System.out.println("Directory is empty");
        }
        fs.mkdir("Homework");
        fs.mkdir("Classwork");

        // Main test
        output = fs.getDirContents("/");
        if(output.equals(expectedOutput)) {
            System.out.println("Test is successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        }

        // Cleanup
        fs.removeDir("Homework");
        fs.removeDir("Classwork");
    }

    public static void testCreateFile(InMemoryFileSystem fs) {
        System.out.println("*****Testing CreateFile*****");

        // Setup
        List<String> expectedOutput = new ArrayList<String>();
        List<String> output = fs.getDirContents(fs.getCurrentWorkingDir());
        if(output.equals(expectedOutput)) {
            System.out.println("Directory is empty");
        }
        expectedOutput.add("TestFile.txt");

        // Main test
        fs.createFile("TestFile.txt");
        output = fs.getDirContents(fs.getCurrentWorkingDir());
        if(output.equals(expectedOutput)) {
            System.out.println("Test is successful");
            numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        }
    }

    public static void testWriteFileContents(InMemoryFileSystem fs) {
        System.out.println("*****Testing WriteFileContents*****");
        String expectedOutput = "";
        String FileName = "TestFile.txt";

        // Test for empty file
        String output = fs.getFileContents(FileName);

        if(output.equals(expectedOutput)) {
           System.out.println("Test is successful");
           numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        } 

        // Test when content is written to the specified file
        String content = "Hello World!";
        fs.writeFileContents(FileName, content);

        expectedOutput = "Hello World!";
        output = fs.getFileContents(FileName);
        if(output.equals(expectedOutput)) {
           System.out.println("Test is successful");
           numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        } 

    }

    public static void testMoveFile(InMemoryFileSystem fs) {
        System.out.println("*****Testing MoveFile*****");
        // Testfile.txt exists in the current working directory
        String FileName = "TestFile.txt";
        fs.moveFile(FileName, "Homework/Science.txt");

        // Test if the non-existent folder 'Homework' is auto-created 
        // while moving the file and the file 'Testfile.txt' doesn't exist in 
        // the current working directory
        List<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("Homework");
        List<String> output = fs.getDirContents(fs.getCurrentWorkingDir());
        if(output.equals(expectedOutput)) {
           System.out.println("Test is successful");
           numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        } 

        // Test the file is moved from '/Testfile.txt' to the specified location and to the given name
        // '/Homework/Science.txt'
        expectedOutput.remove("Homework");

        fs.changeCurrentWorkingDir("Homework");
        output = fs.getDirContents(fs.getCurrentWorkingDir());
        expectedOutput.add("Science.txt");
        if(output.equals(expectedOutput)) {
           System.out.println("Test is successful");
           numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        }

        fs.changeCurrentWorkingDir("..");        
    }

    public static void testFindFile(InMemoryFileSystem fs) {
        System.out.println("*****Testing Find all occurrences of file*****");
        // Set up
        fs.mkdir("Classwork");
        fs.changeCurrentWorkingDir("Classwork");
        fs.createFile("Science.txt");
        fs.changeCurrentWorkingDir("..");
        fs.mkdir("Science");
        String rootDir = "/";
        String dir = fs.getCurrentWorkingDir();
        
        if(dir.equals(rootDir))
            System.out.println("The current directory is: " + dir);

        String filename = "Science";
        List<String> expectedOutput = new ArrayList<>();
        
        expectedOutput.add("/Classwork/Science.txt");
        expectedOutput.add("/Science");
        expectedOutput.add("/Homework/Science.txt");

        // Main test
        List<String> output = fs.findAllOccurrencesFile(fs.getCurrentWorkingDir(), filename);

        if(output.equals(expectedOutput)) {
           System.out.println("Test is successful");
           numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        }
    }

    public static void testFindMatchingFile(InMemoryFileSystem fs) {
        System.out.println("*****Testing first matching regex*****");

        //Setup
        fs.changeCurrentWorkingDir("Classwork");
        fs.mkdir("Assignment");
        fs.changeCurrentWorkingDir("Assignment");
        fs.mkdir("Science");
        fs.changeCurrentWorkingDir("..");
        fs.changeCurrentWorkingDir("..");

        String rootDir = "/";
        String dir = fs.getCurrentWorkingDir();
        
        if(dir.equals(rootDir))
            System.out.println("The current directory is root: " + dir);

        // Main test
        String expectedOutput = "/Classwork/Assignment/Science";
        String output = fs.findFirstMatchingRegexFile("(.*)Scie(.*)", "/Classwork");

        if(output.equals(expectedOutput)) {
           System.out.println("Test is successful");
           numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        } 

        // Test the case when no match is found
        expectedOutput = "";
        output = fs.findFirstMatchingRegexFile("(.*)Com(.*)", "/Classwork");

        if(output.equals(expectedOutput)) {
           System.out.println("Test is successful");
           numOfSuccessfulTests++;
        } else {
            System.out.println("Test failed");
        } 



    }


}