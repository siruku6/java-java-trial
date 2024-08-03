package workplace.person;


import workplace.Workplace;
import machine.Computer;
import machine.RemoteServer;
import repository.Repository;


public class Leader extends Person {
    public Leader(Workplace workplace, String name) {
        super(workplace, name);
    }

    @Override
    public int approveFile(
        RemoteServer remoteServer, Integer repositoryId, String fileId
    ) {
        int approvalId = this.computer.approveFile(
            remoteServer, repositoryId, fileId, this.personId
        );
        return approvalId;
    }

    public Integer makeRepository() {
        int repository_id = this.computer.makeRepository();
        return repository_id;
    }

    public void pushRepository(RemoteServer remoteServer, Integer repositoryId) {
        this.computer.pushRepository(remoteServer, repositoryId);
    }
}
