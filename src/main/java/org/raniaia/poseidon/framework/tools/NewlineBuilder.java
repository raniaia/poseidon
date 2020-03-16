package org.raniaia.poseidon.framework.tools;

/**
 * Copyright: Create by TianSheng on 2019/12/13 15:59
 */
public final class NewlineBuilder extends StringNewline {

    public NewlineBuilder() {
    }

    public NewlineBuilder(int size) {
        super(size);
    }

    public NewlineBuilder(int valueCapacity, int lineCapacity) {
        super(valueCapacity, lineCapacity);
    }

    public NewlineBuilder(String str) {
        super(str);
    }

    public NewlineBuilder(char[] charArray) {
        super(charArray);
    }

}