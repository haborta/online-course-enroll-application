package com.sha.microservicecoursemanagement.service;

import com.sha.microservicecoursemanagement.model.Course;
import com.sha.microservicecoursemanagement.model.Transaction;

import java.util.List;

public interface CourseService {
    abstract List<Course> allCourses();

    Course findCourseById(Long courseId);

    List<Transaction> findTransactionsOfUser(Long userId);

    List<Transaction> findTransactionOfCourse(Long courseId);

    Transaction saveTransaction(Transaction transaction);
}
