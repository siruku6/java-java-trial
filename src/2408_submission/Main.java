import machine.RemoteServer;
import workplace.Workplace;
import workplace.person.Leader;
import workplace.person.Engineer;
import repository.Repository;


public class Main {
    public static void main(String[] args) {
        System.out.println("---------------- Initialization ---------------");
        String hostName = "10.2.22.4";
        RemoteServer remoteServer = RemoteServer.init(hostName);
        RemoteServer failedRemoteServer = RemoteServer.init(hostName);
        Workplace shinagawa = new Workplace();

        System.out.println("\n---------------- Leader(1) David makes a repository. ---------------");
        Leader david = new Leader(shinagawa, "david");
        int repositoryId = david.makeRepository();
        david.pushRepository(remoteServer, repositoryId);

        // David quit workplace.
        david.quitWorkplace();

        System.out.println("\n---------------- Worker(2) adds a File. ---------------");
        Engineer amy = new Engineer(shinagawa, "amy");
        amy.pullRepository(remoteServer, repositoryId);
        amy.showRepositories();

        String amyFileId = amy.createFile(repositoryId, "[Fair Copy] lorem ipsum ...");
        amy.computer.showFileHistory(repositoryId);
        amy.push(remoteServer, repositoryId, amyFileId);

        // Draft file isn't pushed to the remote server..
        String draftFileId = amy.createFile(repositoryId, "[Draft] hoge huga ...");
        amy.computer.showFileHistory(repositoryId);

        System.out.println("\n---------------- Worker(3) adds another File. ---------------");
        Engineer boby = new Engineer(shinagawa, "boby");
        boby.pullRepository(remoteServer, repositoryId);
        // Just pushed files cannot be seen by other workers.
        boby.computer.showFileHistory(repositoryId);

        String bobyFileId1 = boby.createFile(repositoryId, "[Fair Copy] Hi, you guys! ...");
        String bobyFileId2 = boby.createFile(repositoryId, "[Fair Copy] Crushing an apple! ...");
        boby.computer.showFileHistory(repositoryId);
        boby.push(remoteServer, repositoryId, bobyFileId1);
        boby.push(remoteServer, repositoryId, bobyFileId2);

        // Boby quit workplace.
        boby.quitWorkplace();

        System.out.println("\n---------------- Leader(4) approves the files. ---------------");
        Leader cachy = new Leader(shinagawa, "cachy");
        cachy.pullRepository(remoteServer, repositoryId);
        // She can't see the files which are not approved.
        cachy.computer.showFileHistory(repositoryId);

        // Approve the files.
        cachy.approve(remoteServer, repositoryId, amyFileId);
        cachy.approve(remoteServer, repositoryId, bobyFileId1);

        // She can't see the file now yet.
        cachy.computer.showFileHistory(repositoryId);

        // After approval and pulling, she can see the file.
        cachy.pullRepository(remoteServer, repositoryId);
        cachy.computer.showFileHistory(repositoryId);

        System.out.println("\n---------------- Worker(5) create and delete the same file on the local repository. ---------------");
        Engineer daniel = new Engineer(shinagawa, "daniel");
        daniel.pullRepository(remoteServer, repositoryId);
        String danielFileId = daniel.createFile(repositoryId, "[Fair Copy] How bland Java is! ...");
        daniel.computer.showFileHistory(repositoryId);

        // Daniel delete the file on his local machine.
        daniel.delete(repositoryId, danielFileId);
        daniel.computer.showFileHistory(repositoryId);

        System.out.println("\n---------------- Worker(5) and Leader(4) delete the approved file from the remote repository. ---------------");
        // Daniel delete the file on his local machine which already approved.
        daniel.delete(repositoryId, amyFileId);
        daniel.computer.showFileHistory(repositoryId);

        // Daniel push the deletion of the file to the remote server.
        daniel.push(remoteServer, repositoryId, amyFileId);

        // Approve the deletion of the file.
        cachy.approve(remoteServer, repositoryId, amyFileId);

        // She can still see the logically deleted file.
        cachy.computer.showFileHistory(repositoryId);

        // After approval and pulling, she can confirm the file is deleted.
        cachy.pullRepository(remoteServer, repositoryId);
        cachy.computer.showFileHistory(repositoryId);

        // After approval and pulling, other person can confirm the deletion of file is approved.
        daniel.pullRepository(remoteServer, repositoryId);
        daniel.computer.showFileHistory(repositoryId);
    }
}
