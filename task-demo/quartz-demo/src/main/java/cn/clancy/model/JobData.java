package cn.clancy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author ClancyLv
 * @Date 2025/7/11 13:17
 * @Description 模型类--描述Quartz调度任务的核心数据结构，包含任务的基本信息。
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobData implements Serializable {
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
