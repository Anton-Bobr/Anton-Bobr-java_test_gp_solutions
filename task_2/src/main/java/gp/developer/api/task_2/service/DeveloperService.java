package gp.developer.api.task_2.service;


import gp.developer.api.task_2.entity.DeveloperEntity;
import gp.developer.api.task_2.exception.*;
import gp.developer.api.task_2.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    public DeveloperEntity addDeveloper(DeveloperEntity developerEntity) throws DeveloperMailNotUniqueException,
                                                                                NameValidateException {
        try {
            checkMailUniqueness(developerEntity.getEmail());
            checkNameValidate(developerEntity.getName());
            checkNameLengthValidate(developerEntity.getName());
        }
        catch (DeveloperMailNotUniqueException | NameValidateException  e) {
            throw e;
        }
        return developerRepository.save(developerEntity);
    }

    public DeveloperEntity getDeveloper(long id) throws DeveloperNotFoundException {
        DeveloperEntity developerEntityDB;
        try {
            developerEntityDB = checkDeveloperInDBbyId(id);
        }
        catch (DeveloperNotFoundException e) {
            throw e;
        }
        return developerEntityDB;
    }

    public String deleteDeveloper(DeveloperEntity developerEntity) throws DeveloperNotFoundException {
        DeveloperEntity developerEntityDB;
        try {
            developerEntityDB = checkDeveloperInDBbyNameAndEmail(developerEntity.getName(),
                                                                    developerEntity.getEmail());
        }
        catch (DeveloperNotFoundException e) {
            throw e;
        }
        developerRepository.delete(developerEntityDB);
        return "OK Developer has been successfully deleted";
    }

    public DeveloperEntity updateDeveloper(DeveloperEntity developerEntityNew, long id)
            throws DeveloperNotFoundException,
                    DeveloperUpdateBadRequestException,
                    DeveloperMailNotUniqueException,
                    NameValidateException {

        DeveloperEntity developerEntityDB;
        try {
            developerEntityDB = checkDeveloperInDBbyId(id);
            if ( ! developerEntityDB.getEmail().equals(developerEntityNew.getEmail())) {
                checkMailUniqueness(developerEntityNew.getEmail());
            }
            requestsUpdateValidate(developerEntityNew, developerEntityDB);
            checkNameValidate(developerEntityNew.getName());
            checkNameLengthValidate(developerEntityNew.getName());
        }
        catch (DeveloperNotFoundException | DeveloperMailNotUniqueException
                | NameValidateException | DeveloperUpdateBadRequestException e) {
            throw e;
        }
        developerEntityDB.setName(developerEntityNew.getName());
        developerEntityDB.setEmail(developerEntityNew.getEmail());

        return developerRepository.save(developerEntityDB);
    }
    // -----------------------------------------------------------------------------------------------------------------
    public boolean checkMailUniqueness (String email ) throws DeveloperMailNotUniqueException {
        if (developerRepository.findByEmail(email) != null) {
            throw new DeveloperMailNotUniqueException();
        }
        return true;
    }

    public boolean checkNameValidate (String name) throws NameValidateException {
        if (name.substring(0, 1).matches("[^a-zA-Z]")) {
            throw new NameValidateException("ERROR The name must start with a letter");
        }
        return true;
    }

    public boolean checkNameLengthValidate (String name) throws NameValidateException {
        if (name.length() > 50 || name.length() < 2) {
            throw new NameValidateException("ERROR name must be between 2 and 50 characters");
        }
        return true;
    }

    public DeveloperEntity checkDeveloperInDBbyId(long id) throws DeveloperNotFoundException {
        DeveloperEntity developerEntityDB = developerRepository.findById(id);
        if (developerEntityDB == null) {
            throw new DeveloperNotFoundException();
        }
        return developerEntityDB;
    }

    public DeveloperEntity checkDeveloperInDBbyNameAndEmail(String name, String email) throws DeveloperNotFoundException {
        DeveloperEntity developerEntityDB = developerRepository.findByNameAndEmail(name, email);
        if (developerEntityDB == null) {
            throw new DeveloperNotFoundException();
        }
        return developerEntityDB;
    }

    public boolean requestsUpdateValidate (DeveloperEntity developerEntityNew,
                                           DeveloperEntity developerEntityDB) throws DeveloperUpdateBadRequestException {
        if (developerEntityNew.equals(developerEntityDB)) {
            throw new DeveloperUpdateBadRequestException();
        }
        return true;
    }
}
