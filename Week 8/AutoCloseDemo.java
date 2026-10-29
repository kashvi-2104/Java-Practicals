public class AutoCloseDemo {
    public static void main(String[] args) {

        try (FileResource resource = new FileResource()) {
            resource.use();
            throw new Exception("Original error occurred.");
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());

            for (Throwable t : e.getSuppressed()) {
                System.out.println("Suppressed: " + t.getMessage());
            }
        }
    }
}

class FileResource implements AutoCloseable {

    FileResource() {
        System.out.println("Resource opened.");
    }

    void use() {
        System.out.println("Resource is being used.");
    }

    @Override
    public void close() throws Exception {
        System.out.println("Resource closed.");
        throw new Exception("Error while closing resource.");
    }
}
