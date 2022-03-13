package gp.developer.api.task_3.controllers;

import gp.developer.api.task_3.exception.AuthenticateAuthorizationException;
import gp.developer.api.task_3.service.AuthenticationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@Api("Controller for getting JWT token")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/builder-jwt")
    @ApiOperation("The request must contain \"iss\" and \"roles\" in [ ]")
    public ResponseEntity builderJWT(@ApiParam(value = "Data to be stored in Json format",
            required = true,
            type = "Map",
            examples = @Example(value = {
            @ExampleProperty(
                    value = "{'iss': 'GP','sub': 'user_1','roles' : ['USER', 'ADMIN']}",
                    mediaType = "application/json")
    }))
            @RequestBody Map<String,Object> claims)
            throws AuthenticateAuthorizationException {

        return new ResponseEntity(authenticationService.authenticate(claims), HttpStatus.OK);
    }
}