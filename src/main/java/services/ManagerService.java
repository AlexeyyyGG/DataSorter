package services;

import java.util.ArrayList;
import java.util.List;
import models.Manager;

public class ManagerService {
    private final List<Manager> managers = new ArrayList<>();

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    public List<Manager> getManagers() {
        return managers;
    }
}
