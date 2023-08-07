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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * DockerUtils
 *
 * @author yutianbao
 */
public abstract class DockerUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerUtils.class);

    /**
     * Environment param keys
     */
    private static final String ENV_KEY_HOST = "JPAAS_HOST";
    private static final String ENV_KEY_PORT = "JPAAS_HTTP_PORT";
    private static final String ENV_KEY_PORT_ORIGINAL = "JPAAS_HOST_PORT_8080";

    /**
     * Docker host & port
     */
    private static String DOCKER_HOST = "";
    private static String DOCKER_PORT = "";

    /**
     * Whether is docker
     */
    private static boolean IS_DOCKER;

    static {
        retrieveFromEnv();
    }

    /**
     * Retrieve docker host
     *
     * @return empty string if not a docker
     */
    public static String getDockerHost() {
        return DOCKER_HOST;
    }

    /**
     * Retrieve docker port
     *
     * @return empty string if not a docker
     */
    public static String getDockerPort() {
        return DOCKER_PORT;
    }

    /**
     * Whether a docker
     */
    public static boolean isDocker() {
        return IS_DOCKER;
    }

    /**
     * Retrieve host & port from environment
     */
    private static void retrieveFromEnv() {
        // retrieve host & port from environment
        DOCKER_HOST = System.getenv(ENV_KEY_HOST);
        DOCKER_PORT = System.getenv(ENV_KEY_PORT);

        if (Objects.isNull(DOCKER_PORT) || Objects.equals("", DOCKER_PORT)) {
            DOCKER_PORT = System.getenv(ENV_KEY_PORT_ORIGINAL);
        }


        boolean hasEnvHost = Objects.nonNull(DOCKER_HOST) && !Objects.equals("", DOCKER_HOST);
        boolean hasEnvPort = Objects.nonNull(DOCKER_PORT) && !Objects.equals("", DOCKER_PORT);

        // docker can find both host & port from environment
        if (hasEnvHost && hasEnvPort) {
            IS_DOCKER = true;

            // found nothing means not a docker, maybe an actual machine
        } else if (!hasEnvHost && !hasEnvPort) {
            IS_DOCKER = false;

        } else {
            LOGGER.error("Missing host or port from env for Docker. host:{}, port:{}", DOCKER_HOST, DOCKER_PORT);
            throw new RuntimeException(
                    "Missing host or port from env for Docker. host:" + DOCKER_HOST + ", port:" + DOCKER_PORT);
        }
    }

}
