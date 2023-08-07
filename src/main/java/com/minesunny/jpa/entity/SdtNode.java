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
package com.minesunny.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_sdt")
@Getter
@Setter
@SuperBuilder
@DiscriminatorColumn(name = "sdt_node_type")
public class SdtNode extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "node_id")
    @Comment(value = "node id")
    private Long nodeId;

    @Column(name = "parent_id")
    @Comment("parent id")
    private Long parentId;

    @Column(name = "node_path")
    @Comment(value = "nodePath")
    private String nodePath;

    public void setId(Long id) {
        this.id = id;
    }


}
