package com.transporter.Repository;

import com.transporter.Entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Integer> {
    @Query("SELECT t FROM Transport t WHERE t.msisdn = ?1")
    public Transport findByMsisdn0(String msisdn);
    @Query("SELECT t FROM Transport t WHERE t.msisdn = ?1")
    public List<Transport> findByMsisdn(String msisdn);

}
