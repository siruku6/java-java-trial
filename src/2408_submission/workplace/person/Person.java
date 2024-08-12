package workplace.person;

import machine.RemoteServer;
import machine.Computer;
import workplace.Workplace;
import repository.File;
import repository.Repository;


public abstract class Person {
    private static Integer personIdCounter = 1;
    protected Integer personId;
    protected String name;
    protected Workplace workplace;
    public Computer computer;

    public Person(Workplace workplace, String name) {
        this.workplace = workplace;
        this.personId = personIdCounter;
        personIdCounter += 1;
        this.name = name;
        this.computer = new Computer(this.personId);
        this.attendWorkplace();
    }

    public int getPersonId() {
        return this.personId;
    }

    public String getName() {
        return this.name;
    }

    private void attendWorkplace() {
        workplace.addPerson(this);
        this.turnOnComputer();
    }

    private void turnOnComputer() {
        this.computer.turnOn();
    }

    public void showRepositories() {
        this.computer.showRepositories();
    }

    public String createFile(Integer repositoryId, String content) {
        String fileId = this.computer.createFile(repositoryId, this.personId, content);
        System.out.println("File (" + fileId + ") is created.");
        return fileId;
    }

    public void delete(Integer repositoryId, String fileId) {
        this.computer.delete(repositoryId, fileId);
        System.out.println("File (" + fileId + ") is deleted.");
    }

    public void push(RemoteServer remoteServer, Integer repositoryId, String fileId) {
        this.computer.push(remoteServer, repositoryId, fileId);
    }


    public void pullRepository(RemoteServer remoteServer, Integer repositoryId) {
        this.computer.pullRepository(
            remoteServer, repositoryId
        );
    }

    public abstract int approve(
        RemoteServer remoteServer, Integer repositoryId, String fileId
    );

    public void quitWorkplace() {
        this.turnOffComputer();
        workplace.removePerson(this);
    }

    private void turnOffComputer() {
        this.computer.turnOff();
    }
}
