/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.test;

import mx.kenzie.foundation.language.PostCompileClass;
import org.byteskript.skript.runtime.Script;
import org.byteskript.skript.runtime.Skript;
import org.byteskript.skript.runtime.internal.Member;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThreadingTest extends SkriptTest {
    
    private static final Skript skript = new Skript();
    private static Script script;
    
    @BeforeClass
    public static void start() throws Throwable {
        final PostCompileClass cls = skript.compileScript(ProvidedFunctionsTest.class.getClassLoader()
            .getResourceAsStream("flow.bsk"), "skript.flow");
        script = skript.loadScript(cls);
    }
    
    @Test
    public void sleep_test() throws Throwable {
        final Member function = script.getFunction("sleep_flow");
        assert function != null;
        function.run(skript).get();
    }
    
}
