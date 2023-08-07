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
package com.minesunny.jpa.uid.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * DateUtils provides date formatting, parsing
 *
 * @author yutianbao
 */

public abstract class DateUtils{
    /**
     * Patterns
     */
    public static final String DAY_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date parseByDayPattern(String str) {
        return parseDate(str, DAY_PATTERN);
    }




    public static Date parseDate(String str, String pattern) {
        try {
            return new SimpleDateFormat(pattern).parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }



    public static String formatByDayPattern(Date date) {
        if (date != null) {
            return new SimpleDateFormat(DAY_PATTERN).format(date);
        } else {
            return null;
        }
    }


    public static String formatByDateTimePattern(Date date) {
        if (date != null) {
            return new SimpleDateFormat(DATETIME_PATTERN).format(date);
        } else {
            return null;
        }
    }


}
