package workplace.person;

import machine.RemoteServer;
import workplace.Workplace;

public class Engineer extends Person {
    public Engineer(Workplace workplace, String name) {
        super(workplace, name);
    }

    @Override
    public int approveFile(
        RemoteServer remoteServer, Integer repositoryId, String fileId
    ) {
        System.out.println("You don't have permission to approve files.");

        return -1;
    };
}
