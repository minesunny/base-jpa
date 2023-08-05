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

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class StringUtil {


    public static String createWhereCondition(Map<String, List<Long>> nameIds) {
        List<String> keys = nameIds.keySet().stream().filter(key -> nameIds.get(key) != null && !nameIds.get(key).isEmpty()).collect(Collectors.toList());
        if (keys.isEmpty()) {
            return null;
        }
        List<String> whereCondition = new ArrayList<>();
        nameIds
                .forEach((key, value) -> whereCondition.add(key + " in (" + value.stream().map(String::valueOf)
                        .collect(Collectors.joining(",")) + ")"));
        return String.join(" and ", whereCondition);

    }

    public static String createInsertCondition(Map<String, List<Long>> nameIds) {
        List<String> keys = nameIds.keySet().stream().filter(key -> nameIds.get(key) != null && !nameIds.get(key).isEmpty()).collect(Collectors.toList());
        if (keys.isEmpty()) {
            return null;
        }
        // 使用 Stream API 从两个列表中选择元素组成元组

        List<List<Long>> values = new ArrayList<>();
        Set<Integer> idSizes = new HashSet<>();
        for (String key : keys) {
            List<Long> ids = nameIds.get(key);
            values.add(ids);
            idSizes.add(ids.size());
        }
        if (idSizes.size() != 1) {
            throw new RuntimeException("All ids' length of columnNames must be the same");
        }
        int maxSize = values.get(0).size();
        String insertCondition = IntStream.range(0, maxSize)
                .mapToObj(index -> values.stream()
                        .filter(list -> index < list.size())
                        .map(list -> list.get(index))
                        .map(String::valueOf).collect(Collectors.joining(",")))
                .map(e -> "(" + e + ")")
                .collect(Collectors.joining(","));

        return "(" + String.join(",", keys) + ") values " + String.join(",", insertCondition);
    }

}
