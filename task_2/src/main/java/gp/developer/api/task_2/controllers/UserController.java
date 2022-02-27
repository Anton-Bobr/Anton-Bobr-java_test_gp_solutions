package gp.developer.api.task_2.controllers;

import gp.developer.api.task_2.entity.UserEntity;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@Api("Сontroller for operations with the user entity")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @ApiOperation("Adding a new user")
    public ResponseEntity addUser(@RequestBody UserEntity userEntity) {
        try {
            return ResponseEntity.ok(userService.addUser(userEntity));
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @GetMapping("/get")
    @ApiOperation("Getting a specific user")
    public ResponseEntity getUser(@RequestBody UserEntity userEntity) {
        try {
            return ResponseEntity.ok(userService.getUser(userEntity));
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }


    @DeleteMapping("/delete")
    @ApiOperation("Deleting a specific user")
    public ResponseEntity deleteUser(@RequestBody UserEntity userEntity) {
        try {
            return ResponseEntity.ok(userService.deleteUser(userEntity));
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @PutMapping("/update")
    @ApiOperation("Changing user options")
    public ResponseEntity updateUser(@RequestBody List<UserEntity> userEntityList) {
        try {
            return ResponseEntity.ok(userService.updateUser(userEntityList));
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }
}
