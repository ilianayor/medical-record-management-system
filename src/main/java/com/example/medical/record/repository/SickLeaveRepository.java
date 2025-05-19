package com.example.medical.record.repository;

import com.example.medical.record.domain.entity.SickLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SickLeaveRepository extends JpaRepository<SickLeave, Long> {

    @Query("""
        SELECT EXTRACT(YEAR FROM s.startDate) AS year,
               EXTRACT(MONTH FROM s.startDate) AS month,
               COUNT(s) AS sickLeaveCount
        FROM SickLeave s
        GROUP BY EXTRACT(YEAR FROM s.startDate), EXTRACT(MONTH FROM s.startDate)
        HAVING COUNT(s) = (
            SELECT MAX(sickLeaveCount) FROM (
                SELECT COUNT(s2) AS sickLeaveCount
                FROM SickLeave s2
                GROUP BY EXTRACT(YEAR FROM s2.startDate), EXTRACT(MONTH FROM s2.startDate)
            )
        )
        ORDER BY year DESC, month DESC
        """)
    List<Object[]> findMonthsWithHighestSickLeave();

    @Query("""
        SELECT mv.doctor.id, mv.doctor.doctorName, COUNT(sl)
        FROM SickLeave sl
        JOIN sl.medicalVisit mv
        GROUP BY mv.doctor.id, mv.doctor.doctorName
        HAVING COUNT(sl) = (
            SELECT MAX(sickLeaveCount) FROM (
                SELECT COUNT(sl2) AS sickLeaveCount
                FROM SickLeave sl2
                JOIN sl2.medicalVisit mv2
                GROUP BY mv2.doctor.id, mv2.doctor.doctorName
            )
        )
        ORDER BY COUNT(sl) DESC
        """)
    List<Object[]> findDoctorsWithMostSickLeaves();

}
