package com.telecom.billing.repository;

import com.telecom.billing.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    // Чистая магия Spring Data: этот интерфейс автоматически сгенерирует
    // все методы списания, поиска и апдейта в Oracle СУБД без единой строчки SQL!
}
