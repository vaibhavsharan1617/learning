package com.TestProject1.controller;

import com.TestProject1.entity.Employee;
import com.TestProject1.service.EmployeeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @GetMapping("/getAllEmployee")
    @Operation(summary = "Test data", description = "Returns a greeting message")
    public Employee getEmployee(){

        Employee emp = new Employee();
        emp.setAge(20);
        emp.setCity("Delhi");
        emp.setEmail("pradeep@gmail.com");
        emp.setName("Pradeep");
        System.out.println(emp);
        return emp;
    }

    @PostMapping("/saveEmployee")
    public Employee saveEmployee(@RequestBody Employee emp)
    {
        return employeeService.createEmployee(emp);
    }

    @GetMapping("/getAllEmployees")
    @Operation(summary = "Get all employee", description = "Returns a greeting message")
    public List<Employee> findAllEmployee(){
        return employeeService.getAllEmployees();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete employee by id", description = "Returns a greeting message")
    public ResponseEntity<?> delete(@PathVariable Long id){
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/deleted/employee")
    @Operation(summary = "Get deleted Employee", description = "Returns a greeting message")
    public ResponseEntity<List<Employee>> getAllDeletedEmployees() {
        List<Employee> deletedEmployees = employeeService.getAllDeletedEmployees();
        return ResponseEntity.ok(deletedEmployees);
    }

    @GetMapping("/active/employee")
    @Operation(summary = "Get active Employee", description = "Returns a greeting message")
    public ResponseEntity<List<Employee>> getAllActiveEmployees() {
        List<Employee> activeEmployees = employeeService.getAllActiveEmployees();
        return ResponseEntity.ok(activeEmployees);
    }

    @Operation(summary = "Upload Excel File", description = "Upload excel file that corresponds to postgres employee table.")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadExcel(@Parameter(description = "Excel file to upload")
                                                  @RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Please upload a file. This is an empty file.");
        }

        try {
            employeeService.saveFromExcel(file);
            return ResponseEntity.ok("File uploaded and data saved.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

}
