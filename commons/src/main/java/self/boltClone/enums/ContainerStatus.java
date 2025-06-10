package self.boltClone.enums;

public enum ContainerStatus {
    NEW("NEW"),
    STARTING("STARTING"),
    RUNNING("RUNNING");

    private final String status;

    ContainerStatus(String status) {
        this.status = status;
    }
}
