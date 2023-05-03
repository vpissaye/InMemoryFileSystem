import java.util.*;

public class FileSystem {
    class File {
        boolean isFile = false;
        HashMap<String, File> files = new HashMap<> ();
        String content = "";
    }

    File root;
    String currentWorkingDir; // Maintains the current working directory path

    public FileSystem() {
        root = new File();
        currentWorkingDir = "/";
    }

    /* 
    * Helper method
    * Gets the list of directories in the given path
    * depending on if it is an absolute path or a relative path
    */
    private String[] getPathDirs(String path) {
        String dirs[];
        // Handle absolute paths
        if(path.startsWith("/")) {
            dirs = path.split("/");
        } else { // Handle relative paths
            String newPath = this.currentWorkingDir + '/' + path;
            if(this.currentWorkingDir.equals("/"))
                newPath = this.currentWorkingDir + path;
            dirs = newPath.split("/");
        } 
        return dirs;
    }

    /*
    * Helper method
    * Navigates to the given subtree.
    * Throws an exception if any directory in the path doesn't exist
    */
    private File navigateSubtree(String[] dirs, int length) throws DirectoryNotFoundException {
        File file = this.root;
        for(int i = 1; i < length; i++) {
            try {
                file = file.files.get(dirs[i]);
            } catch(Exception e) {
                throw new DirectoryNotFoundException("Directory not found: " +  dirs[i]);
            }
        }
        return file;
    }

    /*
    * Helper method
    * Navigates to the given subtree and 
    * creates directories if they don't already exist
    */
    private File navigateSubtreeAndCreate(String[] dirs, int length) {
        File file = this.root;
        for(int i = 1; i < length; i++) {
            if(!file.files.containsKey(dirs[i])) {
                file.files.put(dirs[i], new File());
            }
            file = file.files.get(dirs[i]);
        }
        return file;
    }

    /*
    * Returns the current working directory
    */
    public String getCurrentWorkingDir() {
        return this.currentWorkingDir;
    }

    /*
    * Changes the current working directory (cwd) to the given directory
    * The input directory can be a directory in the cwd or the keyword
    * "parent" indicating the parent to cwd. If the given directory doesn't 
    * exist then a message is printed.
    */
    public void changeCurrentWorkingDir(String path) throws DirectoryNotFoundException {
        
        File file = this.root;
        String[] dirs;
        int len = 0;
        if(path.equals("..")) {
            // Go to Parent. Get the directory structure for parent of cwd
            dirs = this.currentWorkingDir.split("/");
            len = dirs.length-1;
        } else if (path.startsWith("/")) {
            // Handle absolute path
            dirs = path.split("/");
            len = dirs.length;
        } else {
            // Handle relative path from cwd
            String fullPath = this.currentWorkingDir + '/' + path;
            if(this.currentWorkingDir.equals("/")) 
                fullPath = this.currentWorkingDir + path;
            dirs = fullPath.split("/");
            len = dirs.length;            
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        String prefix = "";
        
        // Navigate to the current working directory (cwd) subtree
        // Or parent of the cwd if requested to change to parent
        for(int i = 1; i < len; i++) {
            if(file.files.containsKey(dirs[i])) {
                file = file.files.get(dirs[i]);
                sb.append(prefix);
                prefix = "/";
                sb.append(dirs[i]);
            } else {
                throw new DirectoryNotFoundException("Directory not found: " +  dirs[i]);
            }
        }
        this.currentWorkingDir = sb.toString();
    }

    /*
    * Create a directory of the given name in the current working directory (cwd)
    */
    public void mkdir(String path) {
        String[] dirs = getPathDirs(path);
        // Navigate to the cwd subtree and create directories if they don't exist
        File file = navigateSubtreeAndCreate(dirs, dirs.length);
    }

    /*
    * Get the contents of the current working directory (cwd)
    */
    public List<String> getDirContents(String path) throws DirectoryNotFoundException {
        List<String> dirContents = new ArrayList<>();
        File file;
        String[] dirs = getPathDirs(path);
        
        // Navigate to the cwd subtree
        try {
            file = navigateSubtree(dirs, dirs.length);
        } catch (DirectoryNotFoundException e) {
            throw e;
        }
        
        for(String name: file.files.keySet()) {
            dirContents.add(name);
        }
        return dirContents;
    }

    /*
    * Remove the given target directory from the current working directory (cwd), if exists.
    */
    public void removeDir(String path) throws DirectoryNotFoundException {
        String[] dirs = getPathDirs(path);
        File file;
        try {
            file = navigateSubtree(dirs, dirs.length-1);
        } catch (DirectoryNotFoundException e) {
            throw e;
        }

        if(file.files.containsKey(dirs[dirs.length-1])) {
            file.files.remove(dirs[dirs.length-1]);
        }
        else {
            throw new DirectoryNotFoundException("Directory not found: " +  dirs[dirs.length-1]);
        }
    }

    /*
    * Create a file of the given name in the current working directory (cwd)
    */
    public void createFile(String path) throws FileAlreadyExistsException {
        String[] dirs = getPathDirs(path);
        // Navigate to the subtree
        File file = navigateSubtreeAndCreate(dirs, dirs.length-1);

        if(file.files.containsKey(dirs[dirs.length-1])) {
            throw new FileAlreadyExistsException("File already exists");
        } 
        File newFile = new File();
        newFile.isFile = true;
        file.files.put(dirs[dirs.length-1], newFile);
    }

    /*
    * Write the given content to the specified file in the current working directory (cwd)
    */
    public void writeFileContents(String path, String content) throws DirectoryNotFoundException, NotFileException {
        String[] dirs = getPathDirs(path);
        // Navigate to the subtree
        File file;
        try {
            file = navigateSubtree(dirs, dirs.length-1);
        } catch (DirectoryNotFoundException e) {
            throw e;
        }
        
        // Create a new file if the given file doesn't already exist
        if(!file.files.containsKey(dirs[dirs.length-1])) {
            file.files.put(dirs[dirs.length-1], new File());
            file.isFile = true;
        }
        
        file = file.files.get(dirs[dirs.length-1]);
        if(file.isFile) {
            file.content = file.content + content;
        } else {
            throw new NotFileException("Cannot write to file. File is a directory");
        }
    }

    /*
    * Get the contents of the requested file in the current working directory (cwd)
    * Return empty string if the file doesn't exist
    */
    public String getFileContents(String path) throws DirectoryNotFoundException, FileNotFoundException {
        String[] dirs = getPathDirs(path);
        File file;
        // Navigate to the subtree
        try {
            file = navigateSubtree(dirs, dirs.length-1);
        } catch (DirectoryNotFoundException e) {
            throw e;
        }
        
        if(!file.files.containsKey(dirs[dirs.length-1])){
            throw new FileNotFoundException("File doesn't exist");
        } 
        file = file.files.get(dirs[dirs.length-1]);
        return file.content;
    }

    /*
    * Move the given file in the current working directory (cwd)
    * to a another location within cwd
    */
    public void moveFile(String path, String targetPath) throws DirectoryNotFoundException, FileNotFoundException {
        String[] dirs = getPathDirs(path);
        // Navigate to the subtree
        File file;
        try {
            file = navigateSubtree(dirs, dirs.length-1);
        } catch (DirectoryNotFoundException e) {
            throw e;
        }

        if(file.files.containsKey(dirs[dirs.length-1])) {
            File temp = file.files.get(dirs[dirs.length-1]);
            file.files.remove(dirs[dirs.length-1]);
            String[] targetDirs = getPathDirs(targetPath);
            file = navigateSubtreeAndCreate(targetDirs, targetDirs.length-1);
            file.files.put(targetDirs[targetDirs.length-1], temp);

        } else {
            throw new FileNotFoundException("File doesn't exist");
        }
    }

    /*
    * Find all the files with the given filename 
    * in the given directory subtree
    */
    public List<String> findFile(String path, String filename) throws DirectoryNotFoundException {
        String[] dirs = getPathDirs(path);
        // Navigate to the subtree
        File file;
        try {
            file = navigateSubtree(dirs, dirs.length);
        } catch (DirectoryNotFoundException e) {
            throw e;
        }
        List<String> matchedFiles = findAllFiles(filename, file, path);
        return matchedFiles;
    }

    /*
    * Find the given filename recursively in the sub directory structure
    */
    private List<String> findAllFiles(String filename, File dir, String path) {
        List<String> matchedResult = new ArrayList<>();
        // Reset the path string if it is root
        if(path.equals("/")) path = "";
        for(String name: dir.files.keySet()) {
            String newPath = path + "/" + name;
            String newName = name;
            // For files, compare file name without the file extension for a match
            // if the given file name doesn't have a file extension
            if(dir.files.get(name).isFile && name.contains(".") && !filename.contains(".")) {
                String[] splitFileName = name.split("\\.");
                newName = splitFileName[0];
            }
            
            if(newName.equals(filename)) {
                matchedResult.add(newPath);
            }
            // Traverse all subtrees recursively
            if(!dir.files.get(name).isFile) {
                List<String> matchedFiles = findAllFiles(filename, dir.files.get(name), newPath);
                matchedResult.addAll(matchedFiles);
            }
        }
        return matchedResult;
    }

    /*
    * Find the first file that matches the regex in the subtree of the given path
    */
    public String findMatchingRegexFile(String regex, String path) throws DirectoryNotFoundException {
        String[] dirs = getPathDirs(path);
        // Navigate to the subtree
        File file;
        try {
            file = navigateSubtree(dirs, dirs.length);
        } catch (DirectoryNotFoundException e) {
            throw e;
        }
        return findFirstMatchingFile(regex, file, path);
    }

    /*
    * Find the first file that matches the regex in the given path
    * Return if a match is found else search recursively in the subtree
    * return empty string if no match is found 
    */
    private String findFirstMatchingFile(String regex, File dir, String path) {
        String found = "";
        
        for(String name: dir.files.keySet()) {
            String newPath = path + "/" + name;
            // Return if a match is found
            if(name.matches(regex)) {
                return newPath;
            }
            // Search recursively in the subtree for a match
            if(!dir.files.get(name).isFile) {
                found = findFirstMatchingFile(regex, dir.files.get(name), newPath);
                if(found != "")
                    return found;
                
            }
        }
        return found;
    }
}