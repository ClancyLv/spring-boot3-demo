package cn.clancy.job;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component("testJob")
@Slf4j
public class TestJob {

    private final AtomicBoolean executed = new AtomicBoolean(false);
    @Getter
    private String lastParams;

    public void execute(String params) {
        log.info("TestJob执行方法execute的参数: {}", params);
        this.lastParams = params;
        this.executed.set(true);
    }

    public boolean hasExecuted() {
        return executed.get();
    }

    public void reset() {
        executed.set(false);
        lastParams = null;
    }
}
