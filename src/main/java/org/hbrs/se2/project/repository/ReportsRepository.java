package org.hbrs.se2.project.repository;

import org.hbrs.se2.project.dtos.ReportsDTO;
import org.hbrs.se2.project.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Integer>{
    @Query("SELECT COUNT(r) > 4 FROM Reports  r WHERE r.companyid =: companyid ")
    boolean shouldBeBanned(int companyid);
    @Query("SELECT COUNT(r) > 0 FROM Reports r WHERE r.companyid = :companyid AND r.studentid = :studentid")
    boolean studentHasReportedCompany(int companyid, int studentid);
}
