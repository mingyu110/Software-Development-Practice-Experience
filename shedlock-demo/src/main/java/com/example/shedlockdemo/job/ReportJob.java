package com.example.shedlockdemo.job;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 一个示例计划任务，用于演示 ShedLock 的用法。
 */
@Component
public class ReportJob {

    private static final Logger log = LoggerFactory.getLogger(ReportJob.class);

    /**
     * 定时生成报告的方法。
     * @Scheduled(cron = "*/10 * * * * *") 定义了任务的执行频率，这里是每 10 秒执行一次。
     * @SchedulerLock 注解用于确保在分布式环境中，同一时间只有一个实例可以执行该任务。
     *   - name: 锁的唯一名称，必须在所有计划任务中保持唯一。
     *   - lockAtMostFor: 锁的最长持有时间。如果任务执行时间超过该值，锁将自动释放，以防死锁。格式为 ISO-8601 持续时间（例如 PT30S 表示 30 秒）。
     *   - lockAtLeastFor: 锁的最短持有时间。即使任务提前完成，锁也会至少保留这么长时间，以防止不同服务器之间的时钟不同步导致的问题。
     */
    @Scheduled(cron = "*/10 * * * * *")
    @SchedulerLock(name = "generateReportsJob", lockAtMostFor = "PT30S", lockAtLeastFor = "PT1S")
    public void generateReports() {
        log.info("成功获取锁，开始生成报告...");
        try {
            // 模拟耗时操作
            Thread.sleep(2000);
            log.info("报告生成成功。");
        } catch (InterruptedException e) {
            // 恢复中断状态，以便上层代码可以感知到中断
            Thread.currentThread().interrupt();
            log.error("报告生成任务被中断。", e);
        } catch (Exception e) {
            log.error("生成报告时发生未知错误。", e);
        }
    }
}