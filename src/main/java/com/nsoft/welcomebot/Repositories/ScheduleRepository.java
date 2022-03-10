package com.nsoft.welcomebot.Repositories;

import com.nsoft.welcomebot.Entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
