package com.transporter.ModelRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiReport {
    private String origin;
    private String to;
    private String msg;
}
