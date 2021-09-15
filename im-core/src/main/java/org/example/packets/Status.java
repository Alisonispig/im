package org.example.packets;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class Status {

    private String state;

    private String lastChanged;

    public static Status online(){
       return  new Status("ONLINE", DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN));
    }
}
