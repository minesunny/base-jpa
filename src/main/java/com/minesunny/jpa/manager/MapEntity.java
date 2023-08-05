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


import lombok.Getter;

import java.util.Set;

@Getter
public class MapEntity {

    private String tableName;

    private String mapName;
    private Set<Long> mapIds;

    private String mappedByName;
    private Set<Long> mappedByIds;


    protected MapEntity(MapEntityBuilder<?, ?> b) {
        this.mapName = b.mapName;
        this.mapIds = b.mapIds;
        this.tableName = b.tableName;
        this.mappedByName = b.mappedByName;
        this.mappedByIds = b.mappedByIds;
    }

    public MapEntity() {
    }

    public static MapEntityBuilder<?, ?> builder() {
        return new MapEntityBuilderImpl();
    }

    public abstract static class MapEntityBuilder<C extends MapEntity, B extends MapEntity.MapEntityBuilder<C, B>> {
        private String tableName;

        private String mapName;
        private Set<Long> mapIds;

        private String mappedByName;
        private Set<Long> mappedByIds;

        public MapEntityBuilder() {
        }

        public B tableName(String tableName) {
            this.tableName = tableName;
            return this.self();
        }

        public B mapIds(String mapName, Set<Long> mapIds) {
            this.mapName = mapName;
            this.mapIds = mapIds;
            return this.self();
        }

        public B mappedByIds(String mappedByName, Set<Long> mappedByIds) {
            this.mappedByName = mappedByName;
            this.mappedByIds = mappedByIds;
            return this.self();
        }

        protected abstract B self();

        public abstract C build();

    }

    private static final class MapEntityBuilderImpl extends MapEntityBuilder<MapEntity, MapEntityBuilderImpl> {
        private MapEntityBuilderImpl() {
        }

        protected MapEntity.MapEntityBuilderImpl self() {
            return this;
        }

        public MapEntity build() {
            return new MapEntity(this);
        }
    }

}
