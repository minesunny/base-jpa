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

import com.minesunny.jpa.SdtTree;
import com.minesunny.jpa.entity.SdtNode;
import com.minesunny.jpa.exception.ServiceException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Transactional
public interface SdtNodeMgr {
    SdtNode addSdtNode(@NotNull SdtNode sdtNode) throws ServiceException;


    Optional<SdtNode> readSdtNode(@NotNull SdtNode sdtNode) throws ServiceException;

    List<SdtNode> readSdtNodes(@NotNull SdtNode sdtNode) throws ServiceException;

    void bindSdtNode(@NotNull SdtNode childSdtNode, @NotNull SdtNode parentSdtNode) throws ServiceException;

    void unbindSdtNode(@NotNull SdtNode childSdtNode) throws ServiceException;

    List<SdtNode> childSdtNodes(@NotNull SdtNode sdtNode) throws ServiceException;

    List<SdtNode> descendentSdtNodes(@NotNull SdtNode sdtNode) throws ServiceException;

    SdtTree childSdtTree(@NotNull SdtNode sdtNode) throws ServiceException;

    SdtTree descendentSdtTree(SdtNode sdtNode) throws ServiceException;
}
