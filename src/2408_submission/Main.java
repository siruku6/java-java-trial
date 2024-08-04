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

        System.out.println("\n---------------- Worker(2) add a File. ---------------");
        Engineer amy = new Engineer(shinagawa, "amy");
        amy.pullRepository(remoteServer, repositoryId);
        amy.showRepositories();

        String amyFileId = amy.createFile(repositoryId, "[Fair Copy] lorem ipsum ...");
        amy.computer.showFiles(repositoryId);
        amy.pushFile(remoteServer, repositoryId, amyFileId);

        // Draft file isn't pushed to the remote server..
        String draftFileId = amy.createFile(repositoryId, "[Draft] hoge huga ...");
        amy.computer.showFiles(repositoryId);

        System.out.println("\n---------------- Worker(3) add another File. ---------------");
        Engineer boby = new Engineer(shinagawa, "boby");
        boby.pullRepository(remoteServer, repositoryId);
        // Just pushed files cannot be seen by other workers.
        boby.computer.showFiles(repositoryId);

        String bobyFileId1 = boby.createFile(repositoryId, "[Fair Copy] Hi, you guys! ...");
        String bobyFileId2 = boby.createFile(repositoryId, "[Fair Copy] Crushing an apple! ...");
        boby.computer.showFiles(repositoryId);
        boby.pushFile(remoteServer, repositoryId, bobyFileId1);
        boby.pushFile(remoteServer, repositoryId, bobyFileId2);

        // Boby quit workplace.
        boby.quitWorkplace();

        System.out.println("\n---------------- Leader(4) approves the files. ---------------");
        Leader cachy = new Leader(shinagawa, "cachy");
        cachy.pullRepository(remoteServer, repositoryId);
        // She can't see the files which are not approved.
        cachy.computer.showFiles(repositoryId);

        // Approve the files.
        cachy.approveFile(remoteServer, repositoryId, amyFileId);
        cachy.approveFile(remoteServer, repositoryId, bobyFileId1);

        // She can't see the file now yet.
        cachy.computer.showFiles(repositoryId);

        // After approval and pulling, she can see the file.
        cachy.pullRepository(remoteServer, repositoryId);
        cachy.computer.showFiles(repositoryId);

        System.out.println("\n---------------- Worker(5) create and delete the same file. ---------------");
        Engineer daniel = new Engineer(shinagawa, "daniel");
        daniel.pullRepository(remoteServer, repositoryId);
        String danielFileId = daniel.createFile(repositoryId, "[Fair Copy] How bland Java is! ...");
        daniel.computer.showFiles(repositoryId);

        // Daniel delete the file on his local machine.
        daniel.deleteFile(repositoryId, danielFileId);
        daniel.computer.showFiles(repositoryId);
    }
}
