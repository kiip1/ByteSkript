/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.api;

import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.compiler.State;
import mx.kenzie.foundation.language.PostCompileClass;
import org.byteskript.skript.compiler.Context;

import java.util.Collection;

public interface Library {
    
    String name();
    
    Collection<SyntaxElement> getHandlers(final State state, final LanguageElement expected, final Context context);
    
    Collection<PropertyHandler> getProperties();
    
    SyntaxElement[] getSyntax();
    
    LanguageElement[] getConstructs();
    
    Type[] getTypes();
    
    /**
     * Runtime dependencies to be included in complete archives.
     */
    Collection<PostCompileClass> getRuntime();
    
}