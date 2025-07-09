package com.TestProject1.repository;

import com.TestProject1.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface EmployeeRepo extends JpaRepository<Employee,Long> {
    @Query("SELECT e FROM Employee e WHERE e.deleted = false")
    List<Employee> findAllActiveEmployees();

    List<Employee> findByDeletedTrue();

    List<Employee> findByDeletedFalse();

}
