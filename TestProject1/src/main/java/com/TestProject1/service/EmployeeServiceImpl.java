package com.TestProject1.service;

import com.TestProject1.entity.Employee;
import com.TestProject1.repository.EmployeeRepo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl {

    @Autowired
    private EmployeeRepo employeeRepo;

    public Employee createEmployee(Employee employee){
        return employeeRepo.save(employee);
    }

    public List<Employee> getAllEmployees(){
        return employeeRepo.findAll();
    }

    public void deleteEmployeeById(Long id) {
        Optional<Employee> empOptional = employeeRepo.findById(id);
        if (empOptional.isPresent()) {
            Employee employee = empOptional.get();
            employee.setDeleted(true); // Set the deleted flag
            employeeRepo.save(employee); // Save the updated entity
        } else {
            throw new RuntimeException("Employee not found with ID: " + id);
        }
    }

    public List<Employee> getAllDeletedEmployees() {
        return employeeRepo.findByDeletedTrue();
    }

    public List<Employee> getAllActiveEmployees() {
        return employeeRepo.findByDeletedFalse();
    }

    public void saveFromExcel(MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            List<Employee> people = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
                Row row = sheet.getRow(i);

                if (row == null) continue;

                Employee person = new Employee();
                person.setName(row.getCell(0).getStringCellValue());
                person.setAge((int) row.getCell(1).getNumericCellValue());
                person.setCity(row.getCell(2).getStringCellValue());
                person.setEmail(row.getCell(3).getStringCellValue());
                person.setDeleted(row.getCell(4).getBooleanCellValue());

                people.add(person);
            }

            employeeRepo.saveAll(people);
        }
    }

}
