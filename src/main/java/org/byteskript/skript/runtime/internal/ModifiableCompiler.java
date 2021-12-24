/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.runtime.internal;

import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.language.PostCompileClass;
import org.byteskript.skript.api.Library;

import java.io.InputStream;

public interface ModifiableCompiler {
    
    Class<?> load(byte[] bytecode, String name);
    
    PostCompileClass[] compile(InputStream stream, Type name);
    
    PostCompileClass[] compile(InputStream file, String path);
    
    PostCompileClass[] compile(String file, Type path);
    
    boolean addLibrary(Library library);
    
    boolean removeLibrary(Library library);
    
    Library[] getLibraries();
    
}
