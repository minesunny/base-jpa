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

package com.minesunny.jpa.uid.service.worker;

import com.minesunny.jpa.type.WorkerNodeType;
import com.minesunny.jpa.uid.entity.WorkNode;
import com.minesunny.jpa.uid.repository.WorkNodeRepository;
import com.minesunny.jpa.uid.utils.DockerUtils;
import com.minesunny.jpa.uid.utils.NetUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

/**
 * Represents an implementation of {@link WorkerIdAssigner},
 * the worker id will be discarded after assigned to the UidGenerator
 *
 * @author yutianbao
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Setter
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {

    private final WorkNodeRepository nodeRepository;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return assigned worker id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long assignWorkerId() {
        // build worker node entity
        WorkNode workerNode = buildWorkerNode();

        Optional<WorkNode> findWorkNode = nodeRepository.findByHostNameAndPort(workerNode.getHostName(),
                workerNode.getPort());
        if (findWorkNode.isPresent()) {
            return findWorkNode.get().getId();
        }

        // add worker node for new (ignore the same IP + PORT)
        nodeRepository.saveAndFlush(workerNode);
        log.info("Add worker node:" + workerNode);
        return workerNode.getId();
    }

    @Override
    public long assignFakeWorkerId() {
        return buildFakeWorkerNode().getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkNode buildWorkerNode() {
        WorkNode workerNodeEntity = new WorkNode();
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER);
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort());
        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL);
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + new Random().nextInt(100000));
        }

        return workerNodeEntity;
    }

    private WorkNode buildFakeWorkerNode() {
        WorkNode workerNodeEntity = new WorkNode();
        workerNodeEntity.setType(WorkerNodeType.FAKE);
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort() + "-" + new Random().nextInt(100000));
        } else {
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + new Random().nextInt(100000));
        }
        return workerNodeEntity;
    }
}
