package com.transporter.Repository;

import com.transporter.Entity.CMSReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<CMSReport, String> {
    public Optional<CMSReport> findById(String id);

}
