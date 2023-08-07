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
package com.minesunny.jpa.autoconfigure;


import com.minesunny.jpa.uid.UidProperties;
import com.minesunny.jpa.uid.service.generator.impl.CachedUidGenerator;
import com.minesunny.jpa.uid.service.generator.impl.DefaultUidGenerator;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@AutoConfiguration(before = HibernateJpaAutoConfiguration.class)
@ConditionalOnClass({EntityManager.class})
@Import(BaseJpaAutoConfiguration.JpaConfiguration.class)
public class BaseJpaAutoConfiguration {

    @ComponentScan({"com.minesunny.jpa.manager"})
    @EntityScan({"com.minesunny.jpa.entity"})
    @EnableJpaRepositories({"com.minesunny.jpa.repository"})
    @Configuration
    static class JpaConfiguration {

    }

    /**
     * UID 的自动配置
     *
     * @author wujun
     * @date 2019.02.20 10:57
     */
    @Configuration
    @ComponentScan({"com.minesunny.jpa.uid.service"})
    @EntityScan({"com.minesunny.jpa.uid.entity"})
    @EnableJpaRepositories({"com.minesunny.jpa.uid.repository"})

    @ConditionalOnClass({DefaultUidGenerator.class, CachedUidGenerator.class})
    @EnableConfigurationProperties(UidProperties.class)
    public static class UidAutoConfigure {

    }
}
