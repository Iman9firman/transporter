package com.transporter.Repository;

import com.transporter.Entity.MSISDN_Ref;
import com.transporter.Entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MSISDNRepository extends JpaRepository<MSISDN_Ref, Integer> {


}
