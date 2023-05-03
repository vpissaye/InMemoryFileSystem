class FileAlreadyExistsException extends Exception {
    public FileAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}