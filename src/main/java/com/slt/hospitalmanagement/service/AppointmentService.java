package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Appointment;
import com.slt.hospitalmanagement.entity.AppointmentStatus;
import com.slt.hospitalmanagement.repository.AppointmentRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository) {

        this.appointmentRepository =
                appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {

        return appointmentRepository
                .findAllByOrderByAppointmentDateAscAppointmentTimeAsc();
    }

    public Appointment getAppointmentById(Long id) {

        return appointmentRepository
                .findById(id)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Appointment not found"
                    )
                );
    }

    public Appointment saveAppointment(
            Appointment appointment) {

        return appointmentRepository
                .save(appointment);
    }

    public void cancelAppointment(Long id) {

        Appointment appointment =
                getAppointmentById(id);

        appointment.setStatus(
                AppointmentStatus.CANCELLED);

        appointmentRepository.save(
                appointment);
    }

    public void completeAppointment(Long id) {

        Appointment appointment =
                getAppointmentById(id);

        appointment.setStatus(
                AppointmentStatus.COMPLETED);

        appointmentRepository.save(
                appointment);
    }
}