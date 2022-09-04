package com.bpa.matchingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer userId;
    private Integer rating;
    private Integer botId; // -1 表示本人亲自对战
    private Integer waitingTime; // 等待时间 时间越长 越会放宽对rating的要求
}
