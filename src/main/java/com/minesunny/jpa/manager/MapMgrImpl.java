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
package com.minesunny.jpa.manager;

import com.minesunny.jpa.ServiceException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MapMgrImpl implements MapMgr {
    private static final String count = "select count(1) > 0 from %s where %s;";
    private static final String delete = "delete from %s where %s;";

    private static final String select = "select %s from %s where %s";
    private static final String insert = "insert into %s %s";
    private final EntityManager entityManager;

    @Override
    public boolean exist(@NotNull String tableName, @NotNull Map<String, List<Long>> nameIds) throws ServiceException {
        try {
            String whereCondition = StringUtil.createWhereCondition(nameIds);
            if (Objects.isNull(whereCondition)) {
                return false;
            }
            return (boolean) entityManager.createNativeQuery(count.formatted(tableName, whereCondition), Boolean.class).getSingleResult();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void delete(@NotNull String tableName, @NotNull Map<String, List<Long>> nameIds) throws ServiceException {
        try {
            String whereCondition = StringUtil.createWhereCondition(nameIds);
            if (Objects.isNull(whereCondition)) {
                return;
            }
            entityManager.createNativeQuery(delete.formatted(tableName, whereCondition)).executeUpdate();
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Long> find(@NotNull String tableName, @NotNull String selectName, @NotNull String name, @NotNull List<Long> ids) throws ServiceException {

        try {
            Map<String, List<Long>> nameIds = new HashMap<>();
            nameIds.put(name, ids);
            String whereCondition = StringUtil.createWhereCondition(nameIds);
            if (Objects.isNull(whereCondition)) {
                return Collections.emptyList();
            }
            return (List<Long>) entityManager.createNativeQuery(String.format(select, selectName, tableName, whereCondition),
                    Long.class).getResultList();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());

        }
    }


    @Override
    public void save(@NotNull String tableName, @NotNull Map<String, List<Long>> nameIds) throws ServiceException {
        try {
            String insertCondition = StringUtil.createInsertCondition(nameIds);
            if (Objects.isNull(insertCondition)) {
                return;
            }
            entityManager.createNativeQuery(String.format(insert, tableName, insertCondition)).executeUpdate();
            entityManager.flush();
            entityManager.clear();
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());

        }
    }

}
