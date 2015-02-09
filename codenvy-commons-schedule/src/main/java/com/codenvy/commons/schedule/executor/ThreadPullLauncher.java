/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.commons.schedule.executor;

import com.codenvy.commons.lang.NamedThreadFactory;
import com.codenvy.commons.schedule.Launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Execute method marked with @ScheduleCron @ScheduleDelay and @ScheduleRate annotations using
 * CronThreadPoolExecutor.
 *
 * @author Sergii Kabashniuk
 */
@Singleton
public class ThreadPullLauncher implements Launcher {
    private static final Logger LOG = LoggerFactory.getLogger(CronThreadPoolExecutor.class);
    private final CronThreadPoolExecutor service;

    @Inject
    public ThreadPullLauncher(@Named("schedule.initial_pool_size") Integer initialPoolSize) {
        service = new CronThreadPoolExecutor(initialPoolSize, new NamedThreadFactory("Scheduler-", false));
    }


    @PreDestroy
    public void shutdown() throws InterruptedException {
        // Tell threads to finish off.
        service.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                service.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!service.awaitTermination(60, TimeUnit.SECONDS))
                    LOG.warn("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            service.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


    @Override
    public void scheduleCron(Runnable runnable, String cron) {
        CronExpression expression = new CronExpression(cron);
        service.schedule(runnable, expression);
        LOG.info("Schedule method {} with cron  {} schedule", runnable, expression.getExpressionSummary());
    }

    @Override
    public void scheduleWithFixedDelay(Runnable runnable, long initialDelay, long delay, TimeUnit unit) {
        service.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
        LOG.info("Schedule method {} with fixed initial delay {} delay {} unit {}",
                 runnable,
                 initialDelay,
                 delay, unit);
    }

    @Override
    public void scheduleAtFixedRate(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
        service.scheduleAtFixedRate(runnable, initialDelay, period, unit);
        LOG.info("Schedule method {} with fixed rate. Initial delay {} period {} unit {}",
                 runnable,
                 initialDelay,
                 period,
                 unit);
    }
}
