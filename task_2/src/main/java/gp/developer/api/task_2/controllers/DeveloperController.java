package gp.developer.api.task_2.controllers;

import gp.developer.api.task_2.entity.DeveloperEntity;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.service.DeveloperService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/developer")
@Api("Сontroller for operations with the user entity")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @PostMapping("/add")
    @ApiOperation("Adding a new user")
    public ResponseEntity addDeveloper(@RequestBody DeveloperEntity developerEntity) {
        try {
            return new ResponseEntity(developerService.addDeveloper(developerEntity), HttpStatus.CREATED);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @GetMapping("/get")
    @ApiOperation("Getting a specific user")
    public ResponseEntity getDeveloper(@RequestBody DeveloperEntity developerEntity) {
        try {
            return new ResponseEntity(developerService.getDeveloper(developerEntity), HttpStatus.OK);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }


    @DeleteMapping("/delete")
    @ApiOperation("Deleting a specific user")
    public ResponseEntity deleteDeveloper(@RequestBody DeveloperEntity developerEntity) {
        try {
            return new ResponseEntity(developerService.deleteDeveloper(developerEntity), HttpStatus.OK);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @PutMapping("/update")
    @ApiOperation("Changing user options")
    public ResponseEntity updateDeveloper(@RequestBody List<DeveloperEntity> developerEntityList) {
        try {
            return new ResponseEntity(developerService.updateDeveloper(developerEntityList), HttpStatus.OK);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }
}
