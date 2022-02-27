package gp.developer.api.task_2.repository;

import gp.developer.api.task_2.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <UserEntity, Long>{
    UserEntity findByName (String name);
    UserEntity findByEmail (String email);
    UserEntity findByNameAndEmail (String name, String email);
}
