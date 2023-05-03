import java.util.*;

public class InMemoryFileSystem {
    FileSystem fs = new FileSystem();

    // Gets the current working directory
    public String getCurrentWorkingDir() {
        return this.fs.getCurrentWorkingDir();
    }

    // Changes the current working directory to the given directory
    public void changeCurrentWorkingDir(String path) {
        try {
            this.fs.changeCurrentWorkingDir(path);
        } catch (DirectoryNotFoundException e) {
            System.out.println(e);
        }
    }

    // Creates a new directory in the given path
    public void mkdir(String path) {
        this.fs.mkdir(path);
    }

    // Returns the contents of the given directory path
    public List<String> getDirContents(String path) {
        try {
            return this.fs.getDirContents(path);
        } catch (DirectoryNotFoundException e) {
            System.out.println(e);
        }
        return new ArrayList<String>();
    }

    // Removes the specified directory
    public void removeDir(String path) {
        try{
            this.fs.removeDir(path);
        } catch (DirectoryNotFoundException e) {
            System.out.println(e);
        }
    }

    // Creates a new empty file in the given path
    public void createFile(String path) {
        try {
            this.fs.createFile(path);
        } catch (FileAlreadyExistsException e) {
            System.out.println(e);
        }
    }

    // Writes the content to the specified file
    public void writeFileContents(String path, String content) {
        try {
            this.fs.writeFileContents(path, content);
        } catch (DirectoryNotFoundException de) {
            System.out.println(de);
        } catch (NotFileException ne) {
            System.out.println(ne);
        }
    }

    // Gets the contents of the given file
    public String getFileContents(String path) {
        try {
            return this.fs.getFileContents(path);
        } catch (DirectoryNotFoundException de) {
            System.out.println(de);
        } catch (FileNotFoundException fe) {
            System.out.println(fe);
        }
        return "";
    }

    // Moves the target file to the given path
    public void moveFile(String path, String targetPath) {
        try {
            this.fs.moveFile(path, targetPath);
        } catch (DirectoryNotFoundException de) {
            System.out.println(de);
        } catch (FileNotFoundException fe) {
            System.out.println(fe);
        }
    }

    // Finds all the occurrences (directory/file) of the specified name
    // in the given path
    public List<String> findAllOccurrencesFile(String path, String filename) {
        try {
            return this.fs.findFile(path, filename);
        } catch (DirectoryNotFoundException de) {
            System.out.println(de);
        }
        return new ArrayList<String>();
    }

    // Finds the first file/directory that matches the given regex
    public String findFirstMatchingRegexFile(String regex, String path) {
        try {
            return this.fs.findMatchingRegexFile(regex, path);
        } catch (DirectoryNotFoundException de) {
            System.out.println(de);
        }
        return "";
    }
}