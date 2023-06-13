package com.transporter.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Table(name = "MSISDN")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MSISDN_Ref {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String msisdn;
}
