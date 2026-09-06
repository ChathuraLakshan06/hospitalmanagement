package com.slt.hospitalmanagement.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Appointment;
import com.slt.hospitalmanagement.entity.Attendance;
import com.slt.hospitalmanagement.entity.Bill;
import com.slt.hospitalmanagement.entity.Employee;
import com.slt.hospitalmanagement.entity.EmployeeStatus;
import com.slt.hospitalmanagement.entity.LaboratoryTest;
import com.slt.hospitalmanagement.entity.LeaveRecord;
import com.slt.hospitalmanagement.entity.LeaveStatus;
import com.slt.hospitalmanagement.entity.Medicine;
import com.slt.hospitalmanagement.entity.Patient;
import com.slt.hospitalmanagement.entity.Payment;
import com.slt.hospitalmanagement.repository.AppointmentRepository;
import com.slt.hospitalmanagement.repository.AttendanceRepository;
import com.slt.hospitalmanagement.repository.BillRepository;
import com.slt.hospitalmanagement.repository.EmployeeRepository;
import com.slt.hospitalmanagement.repository.LaboratoryTestRepository;
import com.slt.hospitalmanagement.repository.LeaveRecordRepository;
import com.slt.hospitalmanagement.repository.MedicineRepository;
import com.slt.hospitalmanagement.repository.PatientRepository;
import com.slt.hospitalmanagement.repository.PaymentRepository;

@Service
public class ReportService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final MedicineRepository medicineRepository;
    private final LaboratoryTestRepository laboratoryTestRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRecordRepository leaveRecordRepository;

    public ReportService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            BillRepository billRepository,
            PaymentRepository paymentRepository,
            MedicineRepository medicineRepository,
            LaboratoryTestRepository laboratoryTestRepository,
            EmployeeRepository employeeRepository,
            AttendanceRepository attendanceRepository,
            LeaveRecordRepository leaveRecordRepository) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
        this.medicineRepository = medicineRepository;
        this.laboratoryTestRepository = laboratoryTestRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRecordRepository = leaveRecordRepository;
    }

    // =========================
    // DASHBOARD SUMMARY
    // =========================

    public long getTotalPatients() {
        return patientRepository.count();
    }

    public long getTodayAppointments() {

        LocalDate today = LocalDate.now();

        return appointmentRepository
                .findAll()
                .stream()
                .filter(a ->
                        a.getAppointmentDate() != null &&
                        a.getAppointmentDate().equals(today))
                .count();
    }

    public long getLaboratoryRequests() {

        return laboratoryTestRepository
                .findAll()
                .stream()
                .filter(test ->
                        test.getStatus() != null &&
                        test.getStatus().name()
                                .equals("REQUESTED"))
                .count();
    }

    public long getLowStockCount() {

        return medicineRepository
                .findAll()
                .stream()
                .filter(Medicine::isLowStock)
                .count();
    }

    public long getActiveStaffCount() {

        return employeeRepository
                .findAll()
                .stream()
                .filter(employee ->
                        employee.getStatus() != null &&
                        employee.getStatus()
                                == EmployeeStatus.ACTIVE)
                .count();
    }

    // =========================
    // PATIENT REPORT
    // =========================

    public List<Patient> getPatientReport() {

        return patientRepository.findAll();
    }

    // =========================
    // APPOINTMENT REPORT
    // =========================

    public List<Appointment> getAppointmentReport(
            LocalDate startDate,
            LocalDate endDate) {

        return appointmentRepository
                .findAll()
                .stream()
                .filter(appointment -> {

                    if (appointment.getAppointmentDate() == null) {
                        return false;
                    }

                    return !appointment
                            .getAppointmentDate()
                            .isBefore(startDate)
                            &&
                            !appointment
                            .getAppointmentDate()
                            .isAfter(endDate);
                })
                .sorted(
                        (a, b) ->
                                a.getAppointmentDate()
                                        .compareTo(
                                                b.getAppointmentDate()
                                        )
                )
                .toList();
    }

    public long countAppointmentsByStatus(
            List<Appointment> appointments,
            String status) {

        return appointments
                .stream()
                .filter(a ->
                        a.getStatus() != null &&
                        a.getStatus().name()
                                .equals(status))
                .count();
    }

    // =========================
    // REVENUE REPORT
    // =========================

    public List<Payment> getPaymentReport(
            LocalDate startDate,
            LocalDate endDate) {

        return paymentRepository
                .findAll()
                .stream()
                .filter(payment -> {

                    if (payment.getPaymentDate() == null) {
                        return false;
                    }

                    LocalDate paymentDate =
                            payment.getPaymentDate()
                                    .toLocalDate();

                    return !paymentDate.isBefore(startDate)
                            &&
                            !paymentDate.isAfter(endDate);
                })
                .toList();
    }

    public BigDecimal calculateRevenue(
            List<Payment> payments) {

        return payments
                .stream()
                .map(Payment::getAmount)
                .filter(amount -> amount != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    public BigDecimal getOutstandingBalance() {

        return billRepository
                .findAll()
                .stream()
                .map(Bill::getBalanceAmount)
                .filter(amount -> amount != null)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    // =========================
    // PHARMACY REPORT
    // =========================

    public List<Medicine> getMedicineReport() {

        return medicineRepository.findAll();
    }

    public long getExpiringMedicineCount() {

        return medicineRepository
                .findAll()
                .stream()
                .filter(Medicine::isExpiringSoon)
                .count();
    }

    // =========================
    // LABORATORY REPORT
    // =========================

    public List<LaboratoryTest> getLaboratoryReport() {

        return laboratoryTestRepository.findAll();
    }

    public long countLabStatus(String status) {

        return laboratoryTestRepository
                .findAll()
                .stream()
                .filter(test ->
                        test.getStatus() != null &&
                        test.getStatus()
                                .name()
                                .equals(status))
                .count();
    }

    // =========================
    // STAFF REPORT
    // =========================

    public List<Employee> getStaffReport() {

        return employeeRepository.findAll();
    }

    public List<Attendance> getAttendanceReport() {

        return attendanceRepository.findAll();
    }

    public List<LeaveRecord> getLeaveReport() {

        return leaveRecordRepository.findAll();
    }

    public long getPendingLeaveCount() {

        return leaveRecordRepository
                .findAll()
                .stream()
                .filter(leave ->
                        leave.getStatus() != null &&
                        leave.getStatus()
                                == LeaveStatus.PENDING)
                .count();
    }
}