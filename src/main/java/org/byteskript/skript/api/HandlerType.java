/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.api;

public interface HandlerType {
    
    String name();
    
    boolean expectInputs();
    
    boolean expectReturn();
    
}
