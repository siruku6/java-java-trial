package workplace;

import java.util.List;

import workplace.person.Person;
import machine.Computer;


public class Workplace {
    private Integer workplaceId;
    private List<Person> people;
    private List<Computer> computers;

    public Workplace() {
        // Set workplaceId
        this.workplaceId = 1;
        this.people = new java.util.ArrayList<Person>();
    }

    public void addPerson(Person person) {
        // Add a person to people
        this.people.add(person);

        System.out.println("Person added to workplace");
        this.showPeople();
    }

    public void removePerson(Person person) {
        // Remove a person from people
        this.people.remove(person);

        System.out.println("Person removed from workplace");
        this.showPeople();
    }

    private void showPeople() {
        // Display people list
        System.out.print("Person ID in attendance: ");
        for (Person p : this.people) {
            System.out.print(p.getPersonId() + " ");
        }
        System.out.print("\n");
    }
}
