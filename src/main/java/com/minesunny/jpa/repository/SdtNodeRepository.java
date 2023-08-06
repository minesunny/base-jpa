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
package com.minesunny.jpa.repository;

import com.minesunny.jpa.entity.SdtNode;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SdtNodeRepository extends JpaRepositoryImplementation<SdtNode, Long> {
    @Query("select s from SdtNode s where s.nodeId = :nodeId")
    List<SdtNode> findByNodeId(@Param("nodeId") @NonNull Long nodeId);

    @Query("select s from SdtNode s where s.nodePath like concat(:nodePath, '%')")
    List<SdtNode> findByNodePathStartsWith(@Param("nodePath") @NonNull String nodePath);

    @Query("select s from SdtNode s where s.parentId = :id")
    List<SdtNode> findByParentId(@Param("id") @NonNull Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update SdtNode s set s.nodePath = :newPath where s.nodePath like concat(:oldPath, '%')")
    void updateNodePathByNodePathStartsWith(@NonNull @Param("oldPath") String oldPath,
                                            @NonNull @Param("newPath") String newPath);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update SdtNode s set s.nodePath = to_char(s.nodeId) where s.nodePath like concat(:oldPath, '%')")
    void updateNodePath(@NonNull @Param("oldPath") String oldPath);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("delete from SdtNode s where s.nodePath like concat(:nodePath, '%')")
    void deleteByNodePathStartsWith(@NonNull @Param("nodePath") String nodePath);
}