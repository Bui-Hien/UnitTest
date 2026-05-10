package com.hienbui.unittest.repository;

import com.hienbui.unittest.domain.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankRepository extends JpaRepository<Bank, UUID> {
    @Query("SELECT entity FROM Bank entity WHERE entity.code =?1")
    List<Bank> findByCode(String code);

    @Query("SELECT entity FROM Bank entity WHERE entity.id = ?1 AND entity.code =?2")
    List<Bank> findByIdAndCode(UUID id, String code);

    Page<Bank> findAll(Pageable pageable);
}
