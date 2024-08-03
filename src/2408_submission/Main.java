import machine.RemoteServer;
import workplace.Workplace;
import workplace.person.Leader;
import workplace.person.Engineer;
import repository.Repository;


public class Main {
    public static void main(String[] args) {
        System.out.println("---------------- Initialization ---------------");
        String hostName = "10.2.22.4";
        RemoteServer remoteServer = new RemoteServer(hostName);
        Repository repository = new Repository();
        Workplace shinagawa = new Workplace();

        System.out.println("\n---------------- Leader(1) David makes a repository. ---------------");
        Leader david = new Leader(shinagawa, "david");
        int repositoryId = david.makeRepository();
        david.pushRepository(remoteServer, repositoryId);

        // David quit workplace.
        david.quitWorkplace();

        System.out.println("\n---------------- Worker(2) add Files. ---------------");
        Engineer amy = new Engineer(shinagawa, "amy");
        amy.pullRepository(remoteServer, repositoryId);
        amy.showRepositories();

        String fileId = amy.createFile(repositoryId, "[Fair Copy] lorem ipsum ...");
        amy.viewFiles(repositoryId);
        amy.pushFile(repositoryId, fileId);

        // Draft file isn't pushed to the remote server..
        fileId = amy.createFile(repositoryId, "[Draft] hoge huga ...");
        amy.viewFiles(repositoryId);

        System.out.println("\n---------------- Worker(3) add other Files. ---------------");
        Engineer boby = new Engineer(shinagawa, "boby");
        boby.pullRepository(remoteServer, repositoryId);
        // Only pushed files cannot be seen by other workers. 
        boby.viewFiles(repositoryId);

        fileId = boby.createFile(repositoryId, "[Fair Copy] Hi, you guys! ...");
        boby.pushFile(repositoryId, fileId);

        // Boby quit workplace.
        boby.quitWorkplace();

        System.out.println("\n---------------- Leader(4) approves file. ---------------");
        Leader cachy = new Leader(shinagawa, "cachy");
        cachy.pullRepository(remoteServer, repositoryId);
    }
}
