package com.waes.scalableweb.timetracking;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import static com.waes.scalableweb.timetracking.ConversionUtils.millisecondsToReadableDuration;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${app.aop.time-tracking-enabled:false}")
public class TimeTracker {

    @Around("@annotation(com.waes.scalableweb.timetracking.TrackExecutionTime)")
    public Object executionTime(ProceedingJoinPoint point) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object object = point.proceed();

        stopWatch.stop();
        log.info("Execution time: {}, class: {}, method: {}",
                millisecondsToReadableDuration(stopWatch.getTotalTimeMillis()), point.getSignature().getDeclaringTypeName(), point.getSignature().getName());

        return object;
    }

}
