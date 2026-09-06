package com.slt.hospitalmanagement.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.slt.hospitalmanagement.entity.Appointment;
import com.slt.hospitalmanagement.entity.Payment;
import com.slt.hospitalmanagement.service.ReportService;

@Controller
@RequestMapping("/admin/reports")
public class ReportsController {

    private final ReportService reportService;

    public ReportsController(
            ReportService reportService) {

        this.reportService = reportService;
    }

    @GetMapping
    public String reports(

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            Model model) {

        LocalDate today = LocalDate.now();

        if (startDate == null) {
            startDate = today.withDayOfMonth(1);
        }

        if (endDate == null) {
            endDate = today;
        }

        if (endDate.isBefore(startDate)) {

            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        // =========================
        // GENERAL DASHBOARD
        // =========================

        model.addAttribute(
                "totalPatients",
                reportService.getTotalPatients()
        );

        model.addAttribute(
                "todayAppointments",
                reportService.getTodayAppointments()
        );

        model.addAttribute(
                "labRequests",
                reportService.getLaboratoryRequests()
        );

        model.addAttribute(
                "lowStockCount",
                reportService.getLowStockCount()
        );

        model.addAttribute(
                "activeStaffCount",
                reportService.getActiveStaffCount()
        );

        // =========================
        // PATIENT REPORT
        // =========================

        model.addAttribute(
                "patients",
                reportService.getPatientReport()
        );

        // =========================
        // APPOINTMENTS
        // =========================

        List<Appointment> appointments =
                reportService
                        .getAppointmentReport(
                                startDate,
                                endDate
                        );

        model.addAttribute(
                "appointments",
                appointments
        );

        model.addAttribute(
                "scheduledCount",
                reportService
                        .countAppointmentsByStatus(
                                appointments,
                                "SCHEDULED"
                        )
        );

        model.addAttribute(
                "completedCount",
                reportService
                        .countAppointmentsByStatus(
                                appointments,
                                "COMPLETED"
                        )
        );

        model.addAttribute(
                "cancelledCount",
                reportService
                        .countAppointmentsByStatus(
                                appointments,
                                "CANCELLED"
                        )
        );

        // =========================
        // REVENUE
        // =========================

        List<Payment> payments =
                reportService
                        .getPaymentReport(
                                startDate,
                                endDate
                        );

        model.addAttribute(
                "payments",
                payments
        );

        model.addAttribute(
                "revenue",
                reportService
                        .calculateRevenue(payments)
        );

        model.addAttribute(
                "outstandingBalance",
                reportService
                        .getOutstandingBalance()
        );

        // =========================
        // PHARMACY
        // =========================

        model.addAttribute(
                "medicines",
                reportService.getMedicineReport()
        );

        model.addAttribute(
                "expiringCount",
                reportService
                        .getExpiringMedicineCount()
        );

        // =========================
        // LABORATORY
        // =========================

        model.addAttribute(
                "labTests",
                reportService
                        .getLaboratoryReport()
        );

        model.addAttribute(
                "requestedLabCount",
                reportService
                        .countLabStatus("REQUESTED")
        );

        model.addAttribute(
                "sampleCollectedCount",
                reportService
                        .countLabStatus(
                                "SAMPLE_COLLECTED"
                        )
        );

        model.addAttribute(
                "completedLabCount",
                reportService
                        .countLabStatus("COMPLETED")
        );

        // =========================
        // STAFF
        // =========================

        model.addAttribute(
                "employees",
                reportService.getStaffReport()
        );

        model.addAttribute(
                "attendanceRecords",
                reportService.getAttendanceReport()
        );

        model.addAttribute(
                "leaveRecords",
                reportService.getLeaveReport()
        );

        model.addAttribute(
                "pendingLeaveCount",
                reportService
                        .getPendingLeaveCount()
        );

        model.addAttribute(
                "startDate",
                startDate
        );

        model.addAttribute(
                "endDate",
                endDate
        );

        return "reports";
    }
}