package com.transporter.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Table(name = "transport")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String msisdn;
    @Column(name = "sendto", length = 20)
    private String sendto;
    private String keyword;
    private Integer status;
    private String created_at;
    private String updated_at;
}
