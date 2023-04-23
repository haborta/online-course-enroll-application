package com.sha.microserviceusermanagement.controller;

import com.netflix.discovery.DiscoveryClient;
import com.sha.microserviceusermanagement.model.Role;
import com.sha.microserviceusermanagement.model.User;
import com.sha.microserviceusermanagement.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    @Autowired  // for injection dependency
    private UserService userService;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private Environment env;
    @Value("${spring.application.name}")
    private String serviceId;
    @GetMapping("service/port")
    public String getPort(){
        return "service port number: " + env.getProperties();
    }
    @GetMapping("service/instances")
    public ResponseEntity<> getInstance(){
        return new ResponseEntity<>(discoveryClient.getInstancesById(serviceId), HttpStatus.OK);
    }
    @GetMapping("service/services")
    public ResponseEntity<?> getServices(){
        return new ResponseEntity<>(discoveryClient.getDiscoveryServiceUrls(), HttpStatus.OK);
    }
    // create API method
    @PostMapping("/service/registration")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        if(userService.findByUsername(user.getUsername()) != null){
            // Status code: 409
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        else{
            //Default role = user
            user.setRole(Role.USER);
        }
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED)
    }

    @GetMapping("/service/login")
    public ResponseEntity<?> getUser(Principal principal){
        //Principal principal = request.getUserPrincipal();
        if(principal == null || principal.getName() == null)
        {
            //logout will be successful
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // username = principal.getName()
        return ResponseEntity.ok(userService.findByUsername(principal.getName()));
    }

    @PostMapping("/service/names")
    public ResponseEntity<?> getNamesOfUsers(@RequestBody List<Long> idList){
        return ResponseEntity.ok(userService.findUsers(idList));
    }

    @GetMapping("/service/test")
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("test, it is working.");
    }
}
