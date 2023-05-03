The Code files include:
FileSystem.java: It is the main class that defines the In-Memory File System and its functionalities
InMemoryFileSystem.java: A facade class for FileSystem.java
Tests.java: Consists of tests for the various functions implemented in the FileSystem.java
Runner.java: Instantiates a FileSystem object and invokes the tests in Tests.java
Run.sh: Script to compile and run the code
*Exeption.java: Basic exception handling classes 

Extensions addressed:
* Walk a subtree
* Operations on path - mostly addressed absolute and relative paths requirements. However, the special path ".." is handled only in some cases and not all.

How to:
Run "./Run.sh" to compile the files and run some sample tests

Improvements:
* Maintain a pointer to the current working directory, which would enable faster traversal especially in very deep nested directory structures instead of traversing from root each time
* Maintain a pointer to the parent directory for easy navigation 
* Maintain a variable tracking the name of the directory/file in the class 'File' in FileSystem.java 
* Use a testing framework and add more comprehensive tests
* Extend the exception handling to address exceptions in more sophisticated manner





