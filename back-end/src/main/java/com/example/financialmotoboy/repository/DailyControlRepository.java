package com.example.financialmotoboy.repository;

import com.example.financialmotoboy.entity.DailyControl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyControlRepository extends JpaRepository<DailyControl, Long> {
    List<DailyControl> findAllByOrderByDateDesc(Pageable pageable);
}
