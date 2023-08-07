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

import com.minesunny.jpa.MineSpecification;
import com.minesunny.jpa.SdtTree;
import com.minesunny.jpa.entity.SdtNode;
import com.minesunny.jpa.exception.ServiceException;
import com.minesunny.jpa.repository.SdtNodeRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SdtNodeMgrImpl implements SdtNodeMgr {
    private final SdtNodeRepository repository;

    @Override
    public SdtNode addSdtNode(@NotNull SdtNode sdtNode) throws ServiceException {
        if (Objects.nonNull(sdtNode.getId())) {
            if (repository.findById(sdtNode.getId()).isPresent()) {
                throw new ServiceException(("[addSdtNode]:  The SdtNode entity [id: %s ] is already  exist").formatted(sdtNode.getId()));
            }
        }
        sdtNode.setParentId(null);
        SdtNode saved = repository.saveAndFlush(sdtNode);
        saved.setNodePath(saved.getId().toString() + "-");
        return saved;
    }

    @Override
    public Optional<SdtNode> readSdtNode(@NotNull SdtNode sdtNode) throws ServiceException {
        if (Objects.isNull(sdtNode.getId())) {
            throw new ServiceException("[readSdtNode]:    The childSdtNode entity's id is required and cannot be null");
        }

        return repository.findById(sdtNode.getId());
    }

    @Override
    public List<SdtNode> readSdtNodes(@NotNull SdtNode sdtNode) throws ServiceException {
        MineSpecification<SdtNode> search = sdtNode.specification();
        if (Objects.isNull(search)) {
            throw new ServiceException("[readSdtNodes]:    The SdtNode searchCondition is required and cannot be null");
        }
        return repository.findAll(search);
    }

    @Override
    public void bindSdtNode(@NotNull SdtNode childSdtNode, @NotNull SdtNode parentSdtNode) throws ServiceException {
        if (Objects.isNull(childSdtNode.getId())) {
            throw new ServiceException("[bindSdtNode]:    The childSdtNode entity's id is required and cannot be null");
        }
        if (Objects.isNull(parentSdtNode.getId())) {
            throw new ServiceException("[bindSdtNode]:    The parentSdtNode entity's id is required and cannot be null");
        }
        if (!Objects.equals(childSdtNode.getClass().getName(), parentSdtNode.getClass().getName())) {
            throw new ServiceException(String.format("[bindSdtNode]:    can not bind different type entity childSdtNode [%s]," +
                    "parentSdtNode[ %s]", childSdtNode.getClass().getName(), parentSdtNode.getClass().getName()));
        }
        Optional<SdtNode> optionalChildSdtNode = repository.findById(childSdtNode.getId());
        if (optionalChildSdtNode.isEmpty()) {
            throw new ServiceException("[bindSdtNode]:    The childSdtNode entity is not exist");
        }

        Optional<SdtNode> optionalParentSdtNode = repository.findById(parentSdtNode.getId());
        if (optionalParentSdtNode.isEmpty()) {
            throw new ServiceException("[bindSdtNode]:    The parentSdtNode entity is not exist");
        }
        SdtNode readChildSdtNode = optionalChildSdtNode.get();
        SdtNode readParentSdtNode = optionalParentSdtNode.get();
        if (Objects.equals(readChildSdtNode.getParentId(), readParentSdtNode.getId())) {
            throw new ServiceException(String.format("[bindSdtNode]:    The childSdtNode[nodePath: %s] entity is " +
                    "already bind with " +
                    "parentSdtNode [nodePath: %s] ", readParentSdtNode.getNodePath(), readChildSdtNode.getNodePath()));
        }
        if (List.of(readParentSdtNode.getNodePath().split("-")).contains(childSdtNode.getId().toString())) {
            throw new ServiceException(String.format("[bindSdtNode]:    The childSdtNode[nodePath: %s] entity is already  " +
                    "parentSdtNode's [nodePath: %s] " +
                    "parentNode", readParentSdtNode.getNodePath(), readChildSdtNode.getNodePath()));
        }
        String oldNodePath = readChildSdtNode.getNodePath();
        String newNodePath = readParentSdtNode.getNodePath() + readChildSdtNode.getId() + "-";
        readChildSdtNode.setNodePath(newNodePath);
        readChildSdtNode.setParentId(readParentSdtNode.getId());
        repository.saveAndFlush(readChildSdtNode);
        repository.updateNodePathByNodePathStartsWith(oldNodePath, newNodePath);
    }

    @Override
    public void unbindSdtNode(@NotNull SdtNode childSdtNode) throws ServiceException {
        if (Objects.isNull(childSdtNode.getId())) {
            throw new ServiceException("[unbindSdtNode]:   The SdtNode  entity's id is required and cannot be null");
        }
        Optional<SdtNode> optionalSdtNode = repository.findById(childSdtNode.getId());
        if (optionalSdtNode.isEmpty()) {
            return;
        }
        repository.deleteByNodePathStartsWith(optionalSdtNode.get().getNodePath());
    }

    @Override
    public List<SdtNode> childSdtNodes(@NotNull SdtNode sdtNode) throws ServiceException {
        if (Objects.isNull(sdtNode.getId())) {
            throw new ServiceException("[childSdtNodes]:    The childSdtNode entity's id is required and cannot be null");
        }
        Optional<SdtNode> optionalSdtNode = repository.findById(sdtNode.getId());
        if (optionalSdtNode.isEmpty()) {
            return Collections.emptyList();
        }
        return repository.findByParentId(optionalSdtNode.get().getId());
    }

    @Override
    public List<SdtNode> descendentSdtNodes(@NotNull SdtNode sdtNode) throws ServiceException {
        if (Objects.isNull(sdtNode.getId())) {
            throw new ServiceException("[descendentSdtNodes]:    The childSdtNode entity's id is required and cannot be null");
        }

        Optional<SdtNode> optionalSdtNode = repository.findById(sdtNode.getId());
        if (optionalSdtNode.isEmpty()) {
            throw new ServiceException("[descendentSdtNodes]:    The childSdtNode entity is not exist");
        }
        List<SdtNode> sdtNodes = repository.findByNodePathStartsWith(optionalSdtNode.get().getNodePath());
        sdtNodes.remove(optionalSdtNode.get());
        return sdtNodes;
    }

    @Override
    public SdtTree childSdtTree(@NotNull SdtNode sdtNode) throws ServiceException {
        SdtNodeMgr sdtNodeMgr = (SdtNodeMgr) AopContext.currentProxy();
        List<SdtNode> sdtNodes = sdtNodeMgr.childSdtNodes(sdtNode);
        return SdtTree.buildTree(sdtNode, sdtNodes);
    }

    @Override
    public SdtTree descendentSdtTree(SdtNode sdtNode) throws ServiceException {
        SdtNodeMgr sdtNodeMgr = (SdtNodeMgr) AopContext.currentProxy();
        List<SdtNode> sdtNodes = sdtNodeMgr.descendentSdtNodes(sdtNode);
        return SdtTree.buildTree(sdtNode, sdtNodes);
    }
}
