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

        System.out.println("\n---------------- Leader David makes a repository. ---------------");
        Leader david = new Leader(shinagawa, "david");
        int repositoryId = david.makeRepository();
        david.pushRepository(remoteServer, repositoryId);

        // David quit workplace.
        david.quitWorkplace();

        System.out.println("\n---------------- Worker1 add Files. ---------------");
        Engineer amy = new Engineer(shinagawa, "amy");
        amy.pullRepository(remoteServer, repositoryId);
        amy.showRepositories();

        String fileId = amy.createFile(repositoryId, "[Fair Copy] lorem ipsum ...");
        amy.pushFile(repositoryId, fileId);

        // Draft file isn't pushed to the remote server..
        fileId = amy.createFile(repositoryId, "[Draft] hoge huga ...");

        // // Amy quit workplace.
        // amy.quitWorkplace();

        System.out.println("\n---------------- Worker2 add other Files. ---------------");
        Engineer boby = new Engineer(shinagawa, "boby");
        // fileId = boby.createFile(repositoryId, "lorem ipsum ...");

        // repository = amy.pullRepository(remoteServer, repositoryId);
        // if (repository == null) {
        //     System.out.println("Repository was not found...");
        // }

        System.out.println("\n---------------- Worker3 approves file. ---------------");

        Engineer cachy = new Engineer(shinagawa, "cachy");
        cachy.createFile(repositoryId, "lorem ipsum ...");
    }
}
