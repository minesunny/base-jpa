/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package com.minesunny.jpa;


import lombok.Getter;

import java.io.Serial;


@Getter
public class ServiceException extends Exception {
    /**
     * Default serialVersionUID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * -- GETTER --
     * Returns the HTTP Status code mapped to represent this error
     *
     * @return HTTP status code assigned for this exception
     */
    private int httpStatus = 500; // default is mapped to server error


    /**
     * Create exception containing error code and message.
     *
     * @param errorId see {@link GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg     contains textual information including method of origin and description of the root cause.
     */
    public ServiceException(String msg) {
        super(msg);
    }


    /**
     * Create exception containing error code, message and previous exception.
     *
     * @param errorId           see {@link GlobalErrIds} for list of valid error codes that can be set.  Valid values between 0 & 100_000.
     * @param msg               contains textual information including method of origin and description of the root cause.
     * @param previousException contains reference to related exception which usually is system related, i.e. ldap.
     */
    protected ServiceException(String msg, Throwable previousException) {
        super(msg, previousException);
    }


    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }
}
