package org.art.services.impl;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * Creates source URI path from java task string.
 */
public class JavaSourceFromString extends SimpleJavaFileObject {

    private String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
