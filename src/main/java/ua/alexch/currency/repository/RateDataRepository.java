package ua.alexch.currency.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.alexch.currency.model.Rate;

@Repository
public interface RateDataRepository extends JpaRepository<Rate, Long> {

//    @Query("SELECT r FROM Rate r WHERE r.source = :src")
    List<Rate> findAllBySource(String source);

    @Query("SELECT r FROM Rate r WHERE r.source = :src AND r.date >= :from AND r.date <= :to ORDER BY r.id ASC")
    List<Rate> findAllBySourceAndDatePeriod(
            @Param(value = "src") String source,
            @Param(value = "from") LocalDateTime dateFrom,
            @Param(value = "to") LocalDateTime dateTo);
}
