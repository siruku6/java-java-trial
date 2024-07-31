package workplace.person;

import workplace.Workplace;

public class Engineer extends Person {
    public Engineer(Workplace workplace, String name) {
        super(workplace, name);
    }

    @Override
    public void approveFile(Integer repositoryId, Integer fileId) {
    }
}
