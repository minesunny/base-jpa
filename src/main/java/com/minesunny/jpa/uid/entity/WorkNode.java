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

package com.minesunny.jpa.uid.entity;

import com.minesunny.jpa.entity.BaseEntity;
import com.minesunny.jpa.type.WorkerNodeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_work_node")
@Getter
@Setter
@SuperBuilder
public class WorkNode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    /**
     * Type of CONTAINER: HostName, ACTUAL : IP.
     */
    @Column
    private String hostName;

    /**
     * Type of CONTAINER: Port, ACTUAL : Timestamp + Random(0-10000)
     */
    @Column
    private String port;

    /**
     * type of {@link WorkNode}
     */
    @Column
    @Enumerated(EnumType.STRING)
    private WorkerNodeType type;

    /**
     * Worker launch date, default now
     */
    @Column
    private Date launchDate;
}
