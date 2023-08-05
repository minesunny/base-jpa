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
package com.minesunny.jpa;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
public class SdtTree {
    private Long id;

    private Long nodeId;

    private Long parentId;

    private String nodePath;

    private BaseEntity nodeValue;

    private Long number = 1L;

    private List<SdtTree> childNodes;

    public static SdtTree buildTree(SdtNode rootNode, List<SdtNode> nodes) {
        SdtTree sdtTree = createTree(rootNode);
        List<SdtTree> sdtTrees = nodes.stream()
                .filter(e -> Objects.nonNull(e.getParentId()))
                .sorted(Comparator.comparingInt(o -> o.getNodePath().split("-").length))
                .map(SdtTree::createTree).toList();
        Map<Long, List<SdtTree>> sdtTreeMap = sdtTrees.stream()
                .collect(Collectors.groupingBy(SdtTree::getParentId));
        sdtTree.setChildNodes(sdtTreeMap.get(sdtTree.getId()));
        for (SdtTree sdt : sdtTrees) {
            sdt.setChildNodes(sdtTreeMap.get(sdt.getId()));
        }
        return sdtTree;
    }

    public static SdtTree createTree(SdtNode sdtNode) {
        SdtTree sdtTree = new SdtTree();
        sdtTree.setNodeId(sdtNode.getNodeId());
        sdtTree.setParentId(sdtNode.getParentId());
        sdtTree.setId(sdtNode.getId());
        return sdtTree;
    }

    public Long cac() {
        if (childNodes == null || childNodes.isEmpty()) {
            return number;
        }
        number = childNodes.stream().map(SdtTree::cac).reduce(Long::sum).get() + 1;
        return number;
    }
}
