package machine;

import java.util.List;

import machine.Machine;
import repository.Repository;


public class RemoteServer implements Machine {
    private static Integer serverIdCounter = 1;
    private Integer serverId;
    public String hostName;
    private Boolean powerOn;
    private List<Repository> repositories;

    public RemoteServer(String hostName) {
        this.serverId = serverIdCounter;
        serverIdCounter += 1;

        this.powerOn = false;
        this.hostName = hostName;
        this.repositories = new java.util.ArrayList<Repository>();
        this.turnOn();
    }

    @Override
    public void turnOn() {
        this.powerOn = true;
        System.out.println("Remote server is started!");
    }

    @Override
    public void turnOff() {
        this.powerOn = false;
        System.out.println("Remote server is terminated!");
    }

    @Override
    public void checkPowerOn() {
        if (!this.powerOn) {
            throw new RuntimeException("Remote server is not started.");
        }
    }

    private Repository searchRepository(Integer repositoryId) {
        this.checkPowerOn();

        // Search repository having the same repositoryId
        Repository repository = this.repositories
            .stream()
            .filter(r -> r.getRepositoryId() == repositoryId)
            .findFirst().get();

        return repository;
    }

    private void mergeFile(Integer repositoryId, Integer fileId) {
        this.checkPowerOn();
    }

    public void addFile(Integer repositoryId) {
        this.checkPowerOn();
    }

    public void addRepository(Repository repository) {
        this.checkPowerOn();
        this.repositories.add(repository);
        System.out.println(
            "Repository (" + repository.getRepositoryId() + ") added to " + this.hostName
        );
    }

    public Repository returnRepository(Integer repositoryId) {
        this.checkPowerOn();
        if (!this.powerOn) {
            System.out.println("Remote server is not started.");
            return null;
        }

        Repository repository = null;
        try {
            // Search repository having the same repositoryId
            repository = this.searchRepository(repositoryId);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Repository not found:" + Integer.toString(repositoryId));
            return null;
        }

        // Return only approved files
        Repository clonedRepository = repository.returnClone(true);
        return clonedRepository;
    }

    public void approveFile(Integer repositoryId, Integer fileId, Integer approverId) {
        this.checkPowerOn();
    }
}
