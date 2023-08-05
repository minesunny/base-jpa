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
import com.minesunny.jpa.entity.Role;
import com.minesunny.jpa.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@Import(MapMgrImpl.class)
class MapMgrImplTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    MapMgr mapMgr;


    @Test
    void exist() throws ServiceException {
        User user = new User();
        Role role = new Role();
        role.setUsers(Set.of(user));
        entityManager.persist(role);
        entityManager.flush();
        HashMap<String, List<Long>> map = new HashMap<>();
        map.put("user_id", List.of(user.getId()));
        map.put("role_id", List.of(role.getId()));
        assertTrue(mapMgr.exist("t_role_user_map", map));
    }

    @Test
    void delete() throws ServiceException {
        User user = new User();
        Role role = new Role();
        entityManager.persist(role);
        entityManager.persist(user);
        User user1 = new User();
        entityManager.persist(user1);
        entityManager.flush();
        HashMap<String, List<Long>> map = new HashMap<>();
        map.put("user_id", List.of(user.getId()));
        map.put("role_id", List.of(role.getId()));
        mapMgr.save("t_role_user_map", map);
        assertTrue(mapMgr.exist("t_role_user_map", map));
        map.put("user_id", List.of(user.getId(), user1.getId()));
        assertThrows(ServiceException.class, () -> mapMgr.save("t_role_user_map", map));
        mapMgr.delete("t_role_user_map", map);
        assertEquals(mapMgr.find("t_role_user_map", "role_id", "user_id", List.of(user.getId(), 3L)).size(), 0);

    }

    @Test
    void find() throws ServiceException {
        User user = new User();
        Role role = new Role();
        role.setUsers(Set.of(user));
        entityManager.persist(role);
        entityManager.flush();
        assertEquals(mapMgr.find("t_role_user_map", "role_id", "user_id", List.of(user.getId())).get(0), role.getId());

    }

    @Test
    void save() throws ServiceException {

        User user = new User();
        Role role = new Role();
        entityManager.persist(role);
        entityManager.persist(user);
        entityManager.flush();
        HashMap<String, List<Long>> map = new HashMap<>();
        map.put("user_id", List.of(user.getId()));
        map.put("role_id", List.of(role.getId()));
        mapMgr.save("t_role_user_map", map);
        assertTrue(mapMgr.exist("t_role_user_map", map));

    }
}