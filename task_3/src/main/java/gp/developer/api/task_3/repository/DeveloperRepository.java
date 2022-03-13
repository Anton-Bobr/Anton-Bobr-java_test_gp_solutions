package gp.developer.api.task_3.repository;

import gp.developer.api.task_3.entity.DeveloperEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeveloperRepository extends CrudRepository <DeveloperEntity, Long>{
    DeveloperEntity findByName (String name);
    DeveloperEntity findByEmail (String email);
    DeveloperEntity findByNameAndEmail (String name, String email);
    DeveloperEntity findById (long id);

}
