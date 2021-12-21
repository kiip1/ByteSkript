package mx.kenzie.skript.test;

import mx.kenzie.foundation.language.PostCompileClass;
import mx.kenzie.skript.runtime.Script;
import mx.kenzie.skript.runtime.Skript;
import mx.kenzie.skript.runtime.internal.Member;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProvidedFunctionsTest extends SkriptTest {
    
    private static final Skript skript = new Skript();
    private static Script script;
    
    @BeforeClass
    public static void start() throws Throwable {
        final PostCompileClass cls = skript.compileScript(ProvidedFunctionsTest.class.getClassLoader()
            .getResourceAsStream("provided_functions.bsk"), "skript.provided_functions");
        script = skript.loadScript(cls);
    }
    
    @Test
    public void generic() throws Throwable {
        final Member function = script.getFunction("generic");
        assert function != null;
        function.invoke(skript);
    }
    
    @Test
    public void handles() throws Throwable {
        final Member function = script.getFunction("handles");
        assert function != null;
        function.invoke(skript);
    }
    
    @Test
    public void maths() throws Throwable {
        final Member function = script.getFunction("maths");
        assert function != null;
        function.invoke(skript);
    }
    
}