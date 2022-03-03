package gp.developer.api.task_2.repository;

import gp.developer.api.task_2.entity.DeveloperEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeveloperRepository extends CrudRepository <DeveloperEntity, Long>{
    DeveloperEntity findByName (String name);
    DeveloperEntity findByEmail (String email);
    DeveloperEntity findByNameAndEmail (String name, String email);
}
