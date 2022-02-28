package gp.developer.api.task_2.service;


import gp.developer.api.task_2.entity.UserEntity;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity addUser(UserEntity userEntity) throws DeveloperNotFoundException {

        if (userRepository.findByEmail(userEntity.getEmail()) != null) {
            throw new DeveloperNotFoundException("ERROR User with this email exists");
        }
        if (userEntity.getName().length() > 50 ||
                userEntity.getName().length() < 2) {
            throw new DeveloperNotFoundException("ERROR Username must be between 2 and 50 characters");
        }
        if (userEntity.getName().substring(0, 1).matches("[^a-zA-Z]")) {
            throw new DeveloperNotFoundException("ERROR The username must start with a letter");
        } else {
            return userRepository.save(userEntity);
        }
    }

    public UserEntity getUser (UserEntity userEntity) throws DeveloperNotFoundException {
        UserEntity userEntityDB = userRepository.findByNameAndEmail(userEntity.getName(),
                                                                    userEntity.getEmail());
        if (userEntityDB == null) {
            throw new DeveloperNotFoundException("ERROR User dont found");
        }
        return userEntityDB;
    }

    public String deleteUser(UserEntity userEntity) throws DeveloperNotFoundException {
        UserEntity userEntityDB = userRepository.findByNameAndEmail(userEntity.getName(),
                                                                    userEntity.getEmail());
        if (userEntityDB == null) {
            throw new DeveloperNotFoundException("ERROR User dont found and delete");
        } else {
            userRepository.delete(userEntityDB);
            return "OK User delete";
        }
    }

    public UserEntity updateUser(List<UserEntity> userEntityList) throws DeveloperNotFoundException {
        if (userEntityList.size() != 2 ) {
            throw new DeveloperNotFoundException("ERROR Bad request");
        }
        if (userEntityList.get(0).equals(userEntityList.get(1))) {
            throw new DeveloperNotFoundException("ERROR User 1 = User 2");
        }
        UserEntity userEntityNew = userRepository.findByEmail(userEntityList.get(1).getEmail());

        UserEntity userEntityOld = userRepository.findByNameAndEmail(userEntityList.get(0).getName(),
                                                                    userEntityList.get(0).getEmail());

        if (userEntityOld == null) {
            throw new DeveloperNotFoundException("ERROR User dont found");
        }
        if (userEntityNew != null) {
            throw new DeveloperNotFoundException("ERROR User with this email exists");
        }
        userEntityOld.setName(userEntityList.get(1).getName());
        userEntityOld.setEmail(userEntityList.get(1).getEmail());

        return userRepository.save(userEntityOld);
    }
}
