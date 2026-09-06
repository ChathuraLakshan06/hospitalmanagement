package com.slt.hospitalmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.slt.hospitalmanagement.entity.Attendance;
import com.slt.hospitalmanagement.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;

    public AttendanceService(
            AttendanceRepository repository) {

        this.repository = repository;
    }

    public List<Attendance> getAllAttendance() {

        return repository
                .findAllByOrderByAttendanceDateDesc();
    }

    public void saveAttendance(Attendance attendance) {

        boolean alreadyExists =
                repository
                    .existsByEmployeeIdAndAttendanceDate(
                        attendance.getEmployee().getId(),
                        attendance.getAttendanceDate()
                    );

        if (alreadyExists) {

            throw new IllegalArgumentException(
                "Attendance has already been recorded for this employee on this date."
            );
        }

        repository.save(attendance);
    }
}