package com.nsoft.welcomebot.Repositories;

import com.nsoft.welcomebot.Entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query(
            value = "SELECT * FROM `schedules` \n" +
                    "where is_active = true",
            nativeQuery = true)
    List<Schedule> findAllActiveSchedules();
}
