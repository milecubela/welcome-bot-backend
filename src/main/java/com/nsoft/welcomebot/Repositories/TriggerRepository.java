package com.nsoft.welcomebot.Repositories;

import com.nsoft.welcomebot.Entities.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long> {
    List<Trigger> findTriggersByChannelAndIsActive(String name, boolean active);
}
