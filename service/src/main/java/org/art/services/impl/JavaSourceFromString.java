package org.art.services.impl;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * This class provides creation of URI source from string with java task
 */
public class JavaSourceFromString extends SimpleJavaFileObject {

    final String code;

    JavaSourceFromString(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}
