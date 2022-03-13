package gp.developer.api.task_3.controllers;

import gp.developer.api.task_3.entity.DeveloperEntity;
import gp.developer.api.task_3.exception.DeveloperMailNotUniqueException;
import gp.developer.api.task_3.exception.DeveloperNotFoundException;
import gp.developer.api.task_3.exception.DeveloperUpdateBadRequestException;
import gp.developer.api.task_3.exception.NameValidateException;
import gp.developer.api.task_3.service.DeveloperService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/developer")
@Api("Сontroller for operations with the developer entity")
//@Tag(name="Developer API", description="Сontroller for operations with the developer entit")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @PostMapping("/")
    @ApiOperation("Adding a new developer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addDeveloper(@RequestBody DeveloperEntity developerEntity)
            throws DeveloperMailNotUniqueException,
            NameValidateException {

        return new ResponseEntity(developerService.addDeveloper(developerEntity), HttpStatus.CREATED);

//        try {
//            return new ResponseEntity(developerService.addDeveloper(developerEntity), HttpStatus.CREATED);
//        } catch (DeveloperMailNotUniqueException | NameValidateException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("ERROR");
//        }
    }

    @GetMapping("/")
    @ApiOperation("Getting a specific developer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR') or hasRole('USER')")
    public ResponseEntity getDeveloper(@RequestParam long id) throws DeveloperNotFoundException {

        return new ResponseEntity(developerService.getDeveloper(id), HttpStatus.OK);

//        try {
//            return new ResponseEntity(developerService.getDeveloper(id), HttpStatus.OK);
//        } catch (DeveloperNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("ERROR");
//        }
    }

    @DeleteMapping("/")
    @ApiOperation("Deleting a specific developer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteDeveloper(@RequestBody DeveloperEntity developerEntity) throws DeveloperNotFoundException {

        return new ResponseEntity(developerService.deleteDeveloper(developerEntity), HttpStatus.OK);

//        try {
//            return new ResponseEntity(developerService.deleteDeveloper(developerEntity), HttpStatus.OK);
//        } catch (DeveloperNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("ERROR");
//        }
    }

    @PutMapping("/")
    @ApiOperation("Changing developer options")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateDeveloper(@RequestParam long id, @RequestBody DeveloperEntity developerEntity)
            throws NameValidateException,
            DeveloperUpdateBadRequestException,
            DeveloperNotFoundException,
            DeveloperMailNotUniqueException {

        return new ResponseEntity(developerService.updateDeveloper(developerEntity, id), HttpStatus.OK);

//        try {
//            return new ResponseEntity(developerService.updateDeveloper(developerEntity, id), HttpStatus.OK);
//        } catch (DeveloperNotFoundException
//                | DeveloperUpdateBadRequestException
//                | DeveloperMailNotUniqueException
//                | NameValidateException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("ERROR");
//        }
    }
}
