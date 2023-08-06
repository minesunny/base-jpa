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
import com.minesunny.jpa.ServiceException;
import com.minesunny.jpa.autoconfigure.BaseJpaAutoConfiguration;
import com.minesunny.jpa.entity.SdtNode;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ImportAutoConfiguration(BaseJpaAutoConfiguration.class)
class SdtNodeMgrImplTest {
    @Autowired
    SdtNodeMgr nodeMgr;
    SdtNode l10;
    SdtNode l10_1;
    SdtNode l10_2;
    SdtNode l10_3;
    SdtNode l10_2_1;
    SdtNode l10_2_2;
    SdtNode l10_2_3;
    SdtNode l10_3_1;
    SdtNode l10_3_2;
    SdtNode l10_3_3;
    SdtNode l10_3_3_1;
    SdtNode l10_3_3_;

    @Test
    void addSdtNode() throws ServiceException {

        SdtNode sdtNode = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1L).build());


        assertThrows(ServiceException.class, () -> {
            nodeMgr.addSdtNode(sdtNode);
        });
    }

    @Test
    void readSdtNode() throws ServiceException {

        SdtNode sdtNode = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1L).build());
        assertTrue(nodeMgr.readSdtNode(sdtNode).isPresent());
    }

    @Test
    @Commit
    void unbindSdtNode() throws ServiceException {


        assertThrows(ServiceException.class, () -> nodeMgr.unbindSdtNode(new SdtNode()));

        nodeMgr.unbindSdtNode(SdtNode.builder().id(-1L).build());

        SdtNode childStdNode = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1L).build());
        SdtNode parentStdNode = nodeMgr.addSdtNode(SdtNode.builder().nodeId(2L).build());
        SdtNode descendentStdNode = nodeMgr.addSdtNode(SdtNode.builder().nodeId(3L).build());
        nodeMgr.bindSdtNode(childStdNode
                , parentStdNode);
        nodeMgr.bindSdtNode(descendentStdNode, childStdNode);

        nodeMgr.unbindSdtNode(childStdNode);

        assertTrue(nodeMgr.readSdtNode(childStdNode).isEmpty());
        assertFalse(nodeMgr.readSdtNode(parentStdNode).isEmpty());
        assertTrue(nodeMgr.readSdtNode(descendentStdNode).isEmpty());


    }

    @Test
    void bindSdtNode() throws ServiceException {
        assertThrows(ServiceException.class, () -> {
            nodeMgr.bindSdtNode(new SdtNode(), new SdtNode());
        });

        assertThrows(ServiceException.class, () -> {
            nodeMgr.bindSdtNode(SdtNode.builder().id(-1L).build(), new SdtNode());
        });

        assertThrows(ServiceException.class, () -> {
            nodeMgr.bindSdtNode(SdtNode.builder().id(-1L).build()
                    , SdtNode.builder().id(-1L).build());
        });
        SdtNode childStdNode = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1L).build());

        assertThrows(ServiceException.class, () -> {
            nodeMgr.bindSdtNode(childStdNode
                    , SdtNode.builder().id(-1L).build());
        });
        SdtNode parentStdNode = nodeMgr.addSdtNode(SdtNode.builder().nodeId(2L).build());

        Assertions.assertThrows(ServiceException.class, () -> {
            nodeMgr.bindSdtNode(childStdNode
                    , childStdNode);
        });
        nodeMgr.bindSdtNode(childStdNode
                , parentStdNode);
    }

    private void buildSdt() throws ServiceException {
        l10 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(10L).build());
        l10_1 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(101L).build());
        l10_2 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(102L).build());
        l10_2_1 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1021L).build());
        l10_2_2 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1022L).build());
        l10_2_3 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1023L).build());
        l10_3 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(103L).build());
        l10_3_1 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1031L).build());
        l10_3_2 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1032L).build());
        l10_3_3 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1033L).build());
        l10_3_3_ = nodeMgr.addSdtNode(SdtNode.builder().nodeId(1033L).build());

        l10_3_3_1 = nodeMgr.addSdtNode(SdtNode.builder().nodeId(10331L).build());
        nodeMgr.bindSdtNode(l10_2, l10);
        nodeMgr.bindSdtNode(l10_1, l10);
        nodeMgr.bindSdtNode(l10_3, l10);
        nodeMgr.bindSdtNode(l10_3_1, l10_3);
        nodeMgr.bindSdtNode(l10_3_2, l10_3);
        nodeMgr.bindSdtNode(l10_3_3, l10_3);
        nodeMgr.bindSdtNode(l10_3_3_1, l10_3_3);

        nodeMgr.bindSdtNode(l10_2_1, l10_2);
        nodeMgr.bindSdtNode(l10_2_2, l10_2);
        nodeMgr.bindSdtNode(l10_2_3, l10_2);
        nodeMgr.bindSdtNode(l10_3_3_, l10_2);
    }

    @Test
    @Commit
    void childSdtNodes() throws ServiceException {
        if (l10 == null) {
            buildSdt();
        }
        assertEquals(3, nodeMgr.childSdtNodes(l10).size());
        assertEquals(0, nodeMgr.childSdtNodes(l10_1).size());
        assertEquals(4, nodeMgr.childSdtNodes(l10_2).size());
        assertEquals(3, nodeMgr.childSdtNodes(l10_3).size());
        assertEquals(1, nodeMgr.childSdtNodes(l10_3_3).size());

        assertThrows(ServiceException.class, () -> nodeMgr.childSdtTree(SdtNode.builder().build()));
    }

    @Test
    void descendentSdtNodes() throws ServiceException {
        if (l10 == null) {
            buildSdt();
        }
        assertEquals(11,nodeMgr.descendentSdtNodes(l10).size());

    }

    @Test
    void childSdtTree() throws ServiceException {
        if (l10 == null) {
            buildSdt();
        }
        SdtTree sdtTree = nodeMgr.childSdtTree(l10);
        sdtTree.cac();
        assertEquals(4, sdtTree.getNumber());

    }

    @Test
    void descendentSdtTree() throws ServiceException {
        if (l10 == null) {
            buildSdt();
        }
        SdtTree sdtTree = nodeMgr.descendentSdtTree(l10);
        sdtTree.cac();
        assertEquals(12, sdtTree.getNumber());

    }
}