package com.example.demo.task;

import org.springframework.scheduling.annotation.Scheduled;

//TODO 放开下面的注解 即可自动运行
//@Configuration
//@EnableScheduling
public class ScheduledTask {
    @Scheduled(cron = "0/5 * * * * ? ")
    // 间隔5秒执行
    public void taskCycle() {
        System.out.println("zzy定时任务执行 现在是5秒一次");
    }
}
