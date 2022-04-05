package com.nsoft.welcomebot.Repositories;

import com.nsoft.welcomebot.Entities.Trigger;
import com.nsoft.welcomebot.Utilities.TriggerEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long> {
    List<Trigger> findTriggersByChannelAndIsActiveAndTriggerEvent(String name, Boolean active, TriggerEvent triggerEvent);
}
