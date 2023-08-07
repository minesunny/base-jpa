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

import com.minesunny.jpa.exception.ServiceException;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface MapMgr {

    boolean exist(@NotNull String tableName, @NotNull Map<String, List<Long>> nameIds) throws ServiceException;


    void delete(@NotNull String tableName, @NotNull Map<String, List<Long>> nameIds) throws ServiceException;


    List<Long> find(@NotNull String tableName, @NotNull String selectName,
                    @NotNull String name, @NotNull List<Long> ids) throws ServiceException;


    void save(@NotNull String tableName, @NotNull Map<String, List<Long>> nameIds) throws ServiceException;


}
