package machine;

import java.util.List;
import java.util.Objects;
import java.util.NoSuchElementException;

import machine.Machine;
import repository.Repository;
import repository.File;

public class Computer implements Machine {
    private String remoteServerHost;
    private Integer computerId;
    private List<Repository> repositories;
    private Boolean powerOn;
    // private List<RemoteServer> remoteServerList;

    public Computer(Integer Id) {
        this.powerOn = false;
        this.computerId = Id;
        this.repositories = new java.util.ArrayList<Repository>();
    }

    public Integer getComputerId() {
        return this.computerId;
    }

    @Override
    public void turnOn() {
        this.powerOn = true;
        // System.out.println("Computer is started!");
    }

    @Override
    public void turnOff() {
        this.powerOn = false;
        System.out.println("Computer (" + this.computerId + ") is terminated!");
    }

    @Override
    public void checkPowerOn() {
        if (!this.powerOn) {
            throw new RuntimeException("This is not started.");
        }
    }

    private Repository findRepository(Integer repositoryId) {
        // Get the repository that matches the repository_id from repositories
        try {
            return (
                this.repositories
                .stream()
                .filter(r -> r.getRepositoryId() == repositoryId)
                .findFirst().get()
            );
        } catch (NoSuchElementException e) {
            // throw new RuntimeException
            System.out.println(
                "Repository (" + repositoryId + ") is not found "
                + "in Computer (" + this.computerId + ")."
            );
            return null;
        }
    }

    public Integer makeRepository() {
        this.checkPowerOn();

        Repository repository = new Repository();
        this.repositories.add(repository);
        System.out.println("Repository created: " + repository.getRepositoryId());
        return repository.getRepositoryId();
    }

    public void pushRepository(RemoteServer remoteServer, Integer repositoryId) {
        this.checkPowerOn();

        Repository repository = findRepository(repositoryId);
        if (Objects.isNull(repository)) {
            throw new RuntimeException("Repository can't be pushed.");
        }

        remoteServer.addRepository(repository);
    }

    public void pullRepository(RemoteServer remoteServer, Integer repositoryId) {
        this.checkPowerOn();

        // find repository from local repositories
        Repository localRepository = findRepository(repositoryId);
        Repository remoteRepository = remoteServer.returnRepository(repositoryId);

        if (Objects.isNull(remoteRepository)) {
            throw new RuntimeException(
                "Repository (" + repositoryId + ") couldn't be pulled! "
                + "[Computer:" + this.computerId + "]"
            );
        }

        System.out.println("Repository (" + remoteRepository + ") is pulled from remote server.");

        // merge remote repository to local repository
        if (Objects.isNull(localRepository)) {
            this.repositories.add(remoteRepository);
        } else {
            localRepository.merge(remoteRepository);
            // overwrite the old local repository with the new one
            this.repositories.set(
                this.repositories.indexOf(localRepository),
                localRepository
            );
        }
    }

    public void showRepositories() {
        this.checkPowerOn();
        System.out.println("Repositories: " + this.repositories);
    }

    public String createFile(Integer repositoryId, String content) {
        this.checkPowerOn();
        Repository repository = findRepository(repositoryId);
        if (Objects.isNull(repository)) {
            throw new RuntimeException("File can't be created.");
        }

        File file = new File(content);
        repository.addFile(file);
        return file.fileId;
    }

    public void pushFile(Integer repositoryId, String fileId) {
        this.checkPowerOn();
        System.out.println("File (" + fileId +") is pushed to repository: " + repositoryId);
    }

    public void approveFile(Integer repositoryId, Integer fileId, Integer approverId) {
        this.checkPowerOn();
    }
}
