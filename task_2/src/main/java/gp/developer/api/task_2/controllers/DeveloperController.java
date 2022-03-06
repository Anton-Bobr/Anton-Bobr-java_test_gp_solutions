package gp.developer.api.task_2.controllers;

import gp.developer.api.task_2.entity.DeveloperEntity;
import gp.developer.api.task_2.exception.DeveloperMailNotUniqueException;
import gp.developer.api.task_2.exception.DeveloperNotFoundException;
import gp.developer.api.task_2.exception.DeveloperUpdateBadRequestException;
import gp.developer.api.task_2.exception.NameValidateException;
import gp.developer.api.task_2.service.DeveloperService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/developer")
@Api("Сontroller for operations with the user entity")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @PostMapping("/")
    @ApiOperation("Adding a new developer")
    public ResponseEntity addDeveloper(@RequestBody DeveloperEntity developerEntity) {
        try {
            return new ResponseEntity(developerService.addDeveloper(developerEntity), HttpStatus.CREATED);
        } catch (DeveloperMailNotUniqueException | NameValidateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @GetMapping("/")
    @ApiOperation("Getting a specific developer")
    public ResponseEntity getDeveloper(@RequestParam long id) {
        try {
            return new ResponseEntity(developerService.getDeveloper(id), HttpStatus.OK);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @DeleteMapping("/")
    @ApiOperation("Deleting a specific developer")
    public ResponseEntity deleteDeveloper(@RequestBody DeveloperEntity developerEntity) {
        try {
            return new ResponseEntity(developerService.deleteDeveloper(developerEntity), HttpStatus.OK);
        } catch (DeveloperNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    @PutMapping("/")
    @ApiOperation("Changing developer options")
    public ResponseEntity updateDeveloper(@RequestParam long id, @RequestBody DeveloperEntity developerEntity) {
        try {
            return new ResponseEntity(developerService.updateDeveloper(developerEntity, id), HttpStatus.OK);
        } catch (DeveloperNotFoundException
                | DeveloperUpdateBadRequestException
                | DeveloperMailNotUniqueException
                | NameValidateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }
}
