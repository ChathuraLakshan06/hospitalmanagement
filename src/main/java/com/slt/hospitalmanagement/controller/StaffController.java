package com.slt.hospitalmanagement.controller;

import java.time.LocalDate;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.slt.hospitalmanagement.entity.Attendance;
import com.slt.hospitalmanagement.entity.Department;
import com.slt.hospitalmanagement.entity.Employee;
import com.slt.hospitalmanagement.entity.LeaveRecord;
import com.slt.hospitalmanagement.service.AttendanceService;
import com.slt.hospitalmanagement.service.DepartmentService;
import com.slt.hospitalmanagement.service.EmployeeService;
import com.slt.hospitalmanagement.service.LeaveRecordService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/staff")
public class StaffController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final AttendanceService attendanceService;
    private final LeaveRecordService leaveRecordService;

    public StaffController(
            EmployeeService employeeService,
            DepartmentService departmentService,
            AttendanceService attendanceService,
            LeaveRecordService leaveRecordService) {

        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.attendanceService = attendanceService;
        this.leaveRecordService = leaveRecordService;
    }

    // =========================
    // EMPLOYEES
    // =========================

    @GetMapping
    public String employees(
            @RequestParam(required = false)
            String keyword,
            Model model) {

        if (keyword != null &&
            !keyword.trim().isEmpty()) {

            model.addAttribute(
                    "employees",
                    employeeService
                        .searchEmployees(keyword)
            );

        } else {

            model.addAttribute(
                    "employees",
                    employeeService
                        .getAllEmployees()
            );
        }

        model.addAttribute(
                "keyword",
                keyword
        );

        return "staff";
    }

    @GetMapping("/new")
    public String newEmployee(Model model) {

        Employee employee =
                new Employee();

        employee.setJoinDate(
                LocalDate.now());

        model.addAttribute(
                "employee",
                employee
        );

        model.addAttribute(
                "departments",
                departmentService
                    .getAllDepartments()
        );

        return "staff-form";
    }

    @GetMapping("/{id}/edit")
    public String editEmployee(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "employee",
                employeeService
                    .getEmployeeById(id)
        );

        model.addAttribute(
                "departments",
                departmentService
                    .getAllDepartments()
        );

        return "staff-form";
    }

    @PostMapping("/save")
    public String saveEmployee(
            @Valid
            @ModelAttribute("employee")
            Employee employee,

            BindingResult result,

            @RequestParam Long departmentId,

            Model model) {

        if (result.hasErrors()) {

            model.addAttribute(
                    "departments",
                    departmentService
                        .getAllDepartments()
            );

            return "staff-form";
        }

        Department department =
                departmentService
                    .getDepartmentById(
                        departmentId);

        if (employee.getId() != null) {

            Employee existing =
                    employeeService
                        .getEmployeeById(
                            employee.getId());

            existing.setFirstName(
                    employee.getFirstName());

            existing.setLastName(
                    employee.getLastName());

            existing.setEmail(
                    employee.getEmail());

            existing.setPhone(
                    employee.getPhone());

            existing.setPosition(
                    employee.getPosition());

            existing.setDepartment(
                    department);

            existing.setJoinDate(
                    employee.getJoinDate());

            existing.setStatus(
                    employee.getStatus());

            employeeService
                    .saveEmployee(existing);

        } else {

            employee.setDepartment(
                    department);

            employeeService
                    .saveEmployee(employee);
        }

        return "redirect:/admin/staff";
    }

    // =========================
    // ATTENDANCE
    // =========================

    @GetMapping("/attendance")
    public String attendance(Model model) {

        model.addAttribute(
                "attendanceRecords",
                attendanceService
                    .getAllAttendance()
        );

        model.addAttribute(
                "employees",
                employeeService
                    .getAllEmployees()
        );

        return "attendance";
    }

    @PostMapping("/attendance/save")
    public String saveAttendance(
            @RequestParam Long employeeId,
            @ModelAttribute Attendance attendance,
            Authentication authentication,
            Model model) {

        Employee employee =
                employeeService
                    .getEmployeeById(
                        employeeId);

        attendance.setEmployee(employee);

        attendance.setRecordedBy(
                authentication.getName());

        try {

            attendanceService
                    .saveAttendance(
                        attendance);

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage());

            model.addAttribute(
                    "attendanceRecords",
                    attendanceService
                        .getAllAttendance());

            model.addAttribute(
                    "employees",
                    employeeService
                        .getAllEmployees());

            return "attendance";
        }

        return "redirect:/admin/staff/attendance";
    }

    // =========================
    // LEAVE
    // =========================

    @GetMapping("/leaves")
    public String leaves(Model model) {

        model.addAttribute(
                "leaveRecords",
                leaveRecordService
                    .getAllLeaves()
        );

        model.addAttribute(
                "employees",
                employeeService
                    .getAllEmployees()
        );

        return "leave-records";
    }

    @PostMapping("/leaves/save")
    public String saveLeave(
            @RequestParam Long employeeId,
            @ModelAttribute LeaveRecord leave,
            Model model) {

        Employee employee =
                employeeService
                    .getEmployeeById(
                        employeeId);

        leave.setEmployee(employee);

        try {

            leaveRecordService
                    .saveLeave(leave);

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "error",
                    e.getMessage());

            model.addAttribute(
                    "leaveRecords",
                    leaveRecordService
                        .getAllLeaves());

            model.addAttribute(
                    "employees",
                    employeeService
                        .getAllEmployees());

            return "leave-records";
        }

        return "redirect:/admin/staff/leaves";
    }

    @PostMapping("/leaves/{id}/approve")
    public String approveLeave(
            @PathVariable Long id,
            Authentication authentication) {

        leaveRecordService.approve(
                id,
                authentication.getName());

        return "redirect:/admin/staff/leaves";
    }

    @PostMapping("/leaves/{id}/reject")
    public String rejectLeave(
            @PathVariable Long id,
            Authentication authentication) {

        leaveRecordService.reject(
                id,
                authentication.getName());

        return "redirect:/admin/staff/leaves";
    }
}