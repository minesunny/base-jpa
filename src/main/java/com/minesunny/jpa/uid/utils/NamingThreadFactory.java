/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package com.minesunny.jpa.uid.utils;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Named thread in ThreadFactory. If there is no specified name for thread, it
 * will auto detect using the invoker classname instead.
 *
 * @author yutianbao
 */
@Setter
@Getter
public class NamingThreadFactory implements ThreadFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(NamingThreadFactory.class);
    /**
     * Sequences for multi thread name prefix
     */
    private final ConcurrentHashMap<String, AtomicLong> sequences;
    /**
     * Thread name pre
     */
    private String name;
    /**
     * Is daemon thread
     */
    private boolean daemon;
    /**
     * UncaughtExceptionHandler
     */
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * Constructors
     */
    public NamingThreadFactory() {
        this(null, false, null);
    }

    public NamingThreadFactory(String name) {
        this(name, false, null);
    }

    public NamingThreadFactory(String name, boolean daemon) {
        this(name, daemon, null);
    }

    public NamingThreadFactory(String name, boolean daemon, UncaughtExceptionHandler handler) {
        this.name = name;
        this.daemon = daemon;
        this.uncaughtExceptionHandler = handler;
        this.sequences = new ConcurrentHashMap<String, AtomicLong>();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(this.daemon);

        // If there is no specified name for thread, it will auto detect using the invoker classname instead.
        // Notice that auto-detect may cause some performance overhead
        String prefix = this.name;
        if (Objects.isNull(prefix) || Objects.equals("", prefix)) {
            prefix = getInvoker(2);
        }
        thread.setName(prefix + "-" + getSequence(prefix));

        // no specified uncaughtExceptionHandler, just do logging.
        if (this.uncaughtExceptionHandler != null) {
            thread.setUncaughtExceptionHandler(this.uncaughtExceptionHandler);
        } else {
            thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    LOGGER.error("unhandled exception in thread: " + t.getId() + ":" + t.getName(), e);
                }
            });
        }

        return thread;
    }

    /**
     * Get the method invoker's class name
     *
     * @param depth
     * @return
     */
    private String getInvoker(int depth) {
        Exception e = new Exception();
        StackTraceElement[] stes = e.getStackTrace();
        if (stes.length > depth) {
            return ClassUtils.getShortName(stes[depth].getClass());
        }
        return getClass().getSimpleName();
    }

    /**
     * Get sequence for different naming prefix
     *
     * @param invoker
     * @return
     */
    private long getSequence(String invoker) {
        AtomicLong r = this.sequences.get(invoker);
        if (r == null) {
            r = new AtomicLong(0);
            AtomicLong previous = this.sequences.putIfAbsent(invoker, r);
            if (previous != null) {
                r = previous;
            }
        }

        return r.incrementAndGet();
    }

}
