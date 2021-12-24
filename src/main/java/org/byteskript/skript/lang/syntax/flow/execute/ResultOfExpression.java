/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.lang.syntax.flow.execute;

import mx.kenzie.foundation.MethodBuilder;
import mx.kenzie.foundation.WriteInstruction;
import mx.kenzie.mirror.MethodAccessor;
import org.byteskript.skript.api.note.ForceExtract;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.lang.element.StandardElements;
import org.byteskript.skript.lang.syntax.flow.lambda.RunnableSection;
import org.byteskript.skript.runtime.internal.Member;

import java.lang.reflect.Method;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class ResultOfExpression extends SimpleExpression {
    
    public ResultOfExpression() {
        super(SkriptLangSpec.LIBRARY, StandardElements.EXPRESSION, "[the ]result of %Executable%");
    }
    
    @Override
    public Pattern.Match match(String thing, Context context) {
        return super.match(thing, context);
    }
    
    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        final ElementTree tree = context.getLine().nested()[0];
        final MethodBuilder method = context.getMethod();
        assert method != null;
        if (tree.current() instanceof RunnableSection) {
            final Method target = Runnable.class.getMethod("run");
            method.writeCode(WriteInstruction.invokeInterface(target));
            context.setState(CompileState.CODE_BODY);
            return;
        }
        final Method target = ResultOfExpression.class.getMethod("run", Object.class);
        this.writeCall(method, target, context);
        context.setState(CompileState.CODE_BODY);
    }
    
    @ForceExtract
    public static Object run(Object thing)
        throws Throwable {
        if (thing instanceof Method method)
            return method.invoke(null);
        else if (thing instanceof MethodAccessor<?> runnable)
            return runnable.invoke();
        else if (thing instanceof Member runnable)
            return runnable.invoke();
        else if (thing instanceof Runnable runnable) {
            runnable.run();
            return null;
        } else if (thing instanceof Supplier<?> runnable)
            return runnable.get();
        else if (thing instanceof Future future)
            return future.get();
        return thing;
    }
    
}
