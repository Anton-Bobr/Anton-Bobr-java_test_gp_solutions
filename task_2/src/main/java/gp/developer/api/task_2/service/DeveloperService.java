package gp.developer.api.task_2.service;


import gp.developer.api.task_2.entity.DeveloperEntity;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    public DeveloperEntity addDeveloper(DeveloperEntity developerEntity) throws DeveloperNotFoundException {

        try {
            checkMailUniqueness(developerEntity.getEmail());
            checkNameValidate(developerEntity.getName());
            checkNameLengthValidate(developerEntity.getName());
        }
        catch (DeveloperNotFoundException e) {
            throw e;
        }
        return developerRepository.save(developerEntity);
    }

    public DeveloperEntity getDeveloper(DeveloperEntity developerEntity) throws DeveloperNotFoundException {
        DeveloperEntity developerEntityDB = developerRepository.findByNameAndEmail(developerEntity.getName(),
                                                                    developerEntity.getEmail());
        if (developerEntityDB == null) {
            throw new DeveloperNotFoundException("ERROR Developer dont found");
        }
        return developerEntityDB;
    }

    public String deleteDeveloper(DeveloperEntity developerEntity) throws DeveloperNotFoundException {
        DeveloperEntity developerEntityDB;
        try {
            developerEntityDB = checkDeveloperInBD(developerEntity);
        }
        catch (DeveloperNotFoundException e) {
            throw e;
        }
        developerRepository.delete(developerEntityDB);
        return "OK Developer has been successfully deleted";
    }

    public DeveloperEntity updateDeveloper(List<DeveloperEntity> developerEntityList) throws DeveloperNotFoundException {

        DeveloperEntity developerEntityOld;
        try {
            requestsUpdateValidate(developerEntityList);
            developerEntityOld = checkDeveloperInBD(developerEntityList.get(0));
            checkMailUniqueness(developerEntityList.get(1).getEmail());
        }
        catch (DeveloperNotFoundException e) {
            throw e;
        }
        developerEntityOld.setName(developerEntityList.get(1).getName());
        developerEntityOld.setEmail(developerEntityList.get(1).getEmail());

        return developerRepository.save(developerEntityOld);
    }

    public boolean checkMailUniqueness (String email ) throws DeveloperNotFoundException {
        if (developerRepository.findByEmail(email) != null) {
            throw new DeveloperNotFoundException("ERROR Developer with this email exists");
        }
        return true;
    }

    public boolean checkNameValidate (String name) throws DeveloperNotFoundException {
        if (name.substring(0, 1).matches("[^a-zA-Z]")) {
            throw new DeveloperNotFoundException("ERROR The name must start with a letter");
        }
        return true;
    }

    public boolean checkNameLengthValidate (String name) throws DeveloperNotFoundException {
        if (name.length() > 50 || name.length() < 2) {
            throw new DeveloperNotFoundException("ERROR name must be between 2 and 50 characters");
        }
        return true;
    }

    public DeveloperEntity checkDeveloperInBD(DeveloperEntity developerEntity) throws DeveloperNotFoundException {
        DeveloperEntity developerEntityDB = developerRepository.findByNameAndEmail(developerEntity.getName(),
                developerEntity.getEmail());
        if (developerEntityDB == null) {
            throw new DeveloperNotFoundException("ERROR Developer dont found");
        }
        return developerEntityDB;
    }

    public boolean requestsUpdateValidate (List<DeveloperEntity> developerEntityList) throws DeveloperNotFoundException {
        if (developerEntityList.size() != 2 ) {
            throw new DeveloperNotFoundException("ERROR Bad request");
        }
        if (developerEntityList.get(0).equals(developerEntityList.get(1))) {
            throw new DeveloperNotFoundException("ERROR Developer 1 = Developer 2");
        }
        return true;
    }
}
