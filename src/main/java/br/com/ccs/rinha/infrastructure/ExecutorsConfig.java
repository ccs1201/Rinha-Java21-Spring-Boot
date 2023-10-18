package br.com.ccs.rinha.infrastructure;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.*;

@Configuration
public class ExecutorsConfig {

    @Bean("virtual")
    @Lazy
    public Executor virtual() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean("forkjoin")
    @Lazy
    public Executor forkjoin() {
        return ForkJoinPool.commonPool();
    }

    @Bean("custom")
    @Lazy
    public Executor custom() {
        return new ThreadPoolExecutor(200, 300, 10,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(20000, true));
    }

//    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
//    public AsyncTaskExecutor asyncTaskExecutor(@Qualifier("virtual") Executor executor) {
//        return new TaskExecutorAdapter(executor);
//    }
//
//    @Bean
//    public TomcatProtocolHandlerCustomizer<?> protocolHandlerCustomizer(@Qualifier("virtual") Executor executor) {
//        return protocolHandler -> protocolHandler.setExecutor(executor);
//    }
}
