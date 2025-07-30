package cn.clancy.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author ClancyLv
 * @Date 2023/10/25 15:30
 * @Description 实体类--描述Quartz调度任务的数据库实体，包含任务的核心属性和状态信息。
 * 可用该类持久化定时任务信息，实现动态控制定时任务、重启恢复任务等功能。（建表sql不限制，ScheduleEntity只是个例子，重点是维护定时任务相关信息）
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduleEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 任务id
     */
    private String jobId;

    /**
     * spring bean名称
     */
    private String beanName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 任务状态  0：暂停  1：正常
     */
    private Integer status;
}
