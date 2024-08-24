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

        // After pulling, she can confirm the file is logically deleted.
        cachy.pullRepository(remoteServer, repositoryId);
        cachy.computer.showFileHistory(repositoryId);

        // Approve the deletion of the file.
        cachy.approve(remoteServer, repositoryId, amyFileId);

        // She can still see the logically deleted file.
        cachy.computer.showFileHistory(repositoryId);

        // After approval and pulling, even other person can confirm the deletion of file is approved.
        daniel.pullRepository(remoteServer, repositoryId);
        daniel.computer.showFileHistory(repositoryId);
        daniel.quitWorkplace();

        System.out.println("\n---------------- Worker(6) and Leader(4) try to execute invalid processings. ---------------");
        Engineer edgardo = new Engineer(shinagawa, "edgardo");
        edgardo.pullRepository(remoteServer, repositoryId);

        // Try approve as an Engineer
        edgardo.approve(remoteServer, repositoryId, amyFileId);

        // try to push the file to a repository which doesn't exist.
        edgardo.push(remoteServer, -1, amyFileId);

        // Try to push the file which is not in the local repository.
        edgardo.push(remoteServer, repositoryId, "dummy_file_id");

        // Try to delete the file which is already deleted.
        edgardo.delete(repositoryId, amyFileId);
        edgardo.delete(repositoryId, amyFileId);

        // Try to approve the file which is already approved.
        cachy.approve(remoteServer, repositoryId, amyFileId);

        // Try to approve the file which is not in the remote repository.
        cachy.approve(remoteServer, repositoryId, "dummy_file_id");

        cachy.quitWorkplace();
        edgardo.quitWorkplace();

        System.out.println("\n---------------- Leader(6) create, push, delete, apprpve, and pull the files. ---------------");
        Leader fatimah = new Leader(shinagawa, "fatimah");
        fatimah.pullRepository(remoteServer, repositoryId);

        // Check existence of the created file.
        String fatimahFileId = fatimah.createFile(repositoryId, "[Fair Copy] اصفهان...");
        fatimah.computer.showFileHistory(repositoryId);

        // Check the existence of the deleted file.
        fatimah.push(remoteServer, repositoryId, fatimahFileId);
        fatimah.delete(repositoryId, fatimahFileId);
        fatimah.computer.showFileHistory(repositoryId);

        // Check the existence locally deleted but pulled from remote.
        fatimah.approve(remoteServer, repositoryId, fatimahFileId);
        fatimah.pullRepository(remoteServer, repositoryId);
        fatimah.computer.showFileHistory(repositoryId);
    }
}
