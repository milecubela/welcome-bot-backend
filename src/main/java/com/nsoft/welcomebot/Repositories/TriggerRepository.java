package com.nsoft.welcomebot.Repositories;

import com.nsoft.welcomebot.Entities.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long> {
}
