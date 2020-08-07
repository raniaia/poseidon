package org.jiakesimk.minipika.framework.exception;

/* ************************************************************************
 *
 * Copyright (C) 2020 2B键盘 All rights reserved.
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
 * ************************************************************************/

/*
 * Creates on 2019/12/12.
 */

/**
 * mapper xml文件异常
 * @author 2B键盘
 * @email jiakesiws@gmail.com
 */
public class BuilderXmlException extends MinipikaException {

    public BuilderXmlException() {
    }

    public BuilderXmlException(String message) {
        super(message);
    }

    public BuilderXmlException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuilderXmlException(Throwable cause) {
        super(cause);
    }

    public BuilderXmlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
