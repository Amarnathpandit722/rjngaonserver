package gov.municipal.suda.usermanagement.controller;
import gov.municipal.suda.usermanagement.model.LoginDetails;
import gov.municipal.suda.usermanagement.service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@CrossOrigin
public class LoginDetailsController {

    @Autowired
    private LoginServiceImpl loginService;

    @PostMapping("user/loginDetails")
    public ResponseEntity<String> createLoginDetails(@RequestBody @Valid  LoginDetails loginDetails) {
        loginService.createLoginData(loginDetails);
        return ResponseEntity.ok("Login details for the User");
    }
}
