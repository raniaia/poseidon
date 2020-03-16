package org.raniaia.poseidon.framework.annotation.mapper;

import java.lang.annotation.*;

/**
 *
 * Query annotation, annotated method no need write sql in sql mapper xml file.
 *
 * <p/>
 * License: <a href="https://github.com/Laniakeamly/poseidon/blob/master/LICENSE">Apache License 2.0</a>
 * <p/>
 * Copyright: Create by TianSheng on 2019/12/17 18:29
 * @author TianSheng
 * @version 1.0.0
 * @since 1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Query {

    /**
     * the sql script.
     */
    String value();

}