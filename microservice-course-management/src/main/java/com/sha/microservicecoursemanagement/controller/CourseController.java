package com.sha.microservicecoursemanagement.controller;

import com.netflix.discovery.DiscoveryClient;
import com.sha.microservicecoursemanagement.intercomm.UserClient;
import com.sha.microservicecoursemanagement.model.Transaction;
import com.sha.microservicecoursemanagement.service.CourseService;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CourseController {
    @Autowired
    private UserClient userClient;

    //call it from methods
    @Autowired
    private CourseService courseService;

    // API methods
    @GetMapping("/service/user/{userId}")
    public ResponseEntity<?> findTransactionsOfUser(@PathVariable Long userId){
        return ResponseEntity.ok(courseService.findTransactionsOfUser(userId));
    }

    @GetMapping("/service/all")
    public ResponseEntity<?> findAllCourses(){
        return ResponseEntity.ok(courseService.allCourses());
    }
    @GetMapping("/service/enroll")
    public ResponseEntity<?> saveTransaction(@RequestBody Transaction transaction){
        transaction.setDateOfIssue(LocalDateTime.now());
        transaction.setCourse(courseService.findCourseById(transaction.getCourse().getId()));
        return new ResponseEntity<>(courseService.saveTransaction(transaction), HttpStatus.CREATED);
    }
    @GetMapping("/service/course/{courseId}")
    public ResponseEntity<?> findStudentOfCourse(@PathVariable Long courseId){
        List<Transaction> transactionList = courseService.findTransactionOfCourse(courseId);
        if(transactionList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        List<Long> userIdList = transactionList.parallelStream().map(t -> t.getUserId()).collect(Collectors.toList());
        List<String> students = userClient.getUserNames(userIdList);
        return ResponseEntity.ok(students);
    }
//     eureka
    @Autowired
    private DiscoveryClient discoveryClient;
    // want to display port number of service instance and list all instance of user services
     // import environment
    private Environment env;
    @Value("${spring.application.name}")
    private String serviceId;

    @GetMapping("/service/port")
    public String getPort() {
        return "Service is working at port : " + env.toString();
    }

    @GetMapping("/service/instances")
    public ResponseEntity<?> getInstances(){
        return ResponseEntity.ok(discoveryClient.getInstancesById(serviceId));
    }
}
