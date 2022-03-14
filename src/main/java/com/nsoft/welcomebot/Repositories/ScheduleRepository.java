package com.nsoft.welcomebot.Repositories;

import com.nsoft.welcomebot.Entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    @Query(
            value = "SELECT * FROM `schedules` \n" +
                    "where is_active = true\n" +
                    "and is_repeat = true\n" +
                    "and run_date<LOCALTIME",
            nativeQuery = true)
    List<Schedule> findAllActiveSchedulesNative();
}
