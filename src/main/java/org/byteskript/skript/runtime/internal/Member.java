/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.runtime.internal;

import mx.kenzie.mirror.MethodAccessor;
import mx.kenzie.mirror.Mirror;
import org.byteskript.skript.error.ScriptRuntimeError;
import org.byteskript.skript.runtime.Script;
import org.byteskript.skript.runtime.Skript;
import org.byteskript.skript.runtime.threading.ScriptRunner;
import org.byteskript.skript.runtime.threading.ScriptThread;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Future;

public class Member {
    
    protected final Script script;
    protected final boolean async;
    protected MethodAccessor<?> invoker;
    protected MethodAccessor<?> verifier;
    protected int parameters;
    
    public Member(Script script, Method method, boolean async) {
        this.script = script;
        this.async = async;
        this.parameters = method.getParameterCount();
        final Mirror<?> mirror = Mirror.of(script.mainClass()).useProvider(Skript.LOADER);
        this.invoker = mirror.method(method);
        this.verifier = mirror.method(method.getName() + "_verify", method.getParameterTypes());
    }
    
    public static Object runAsync(Object thing, Object args) {
        final Object[] arguments;
        final Thread current = Thread.currentThread();
        if (!(current instanceof ScriptThread thread))
            throw new ScriptRuntimeError("Cannot create background process from non-script thread.");
        if (args instanceof Collection<?> collection) arguments = collection.toArray();
        else if (args instanceof Object[] array) arguments = array;
        else arguments = new Object[]{args};
        thread.skript.runOnAsyncThread((Instruction<?>) () -> {
            if (thing instanceof Method method)
                method.invoke(null, arguments);
            else if (thing instanceof Member method)
                method.invoke(arguments);
            else if (thing instanceof MethodAccessor<?> method)
                method.invoke(arguments);
        });
        return null;
    }
    
    public Object invoke(Object... arguments) {
        return invoker.invoke(arguments);
    }
    
    public static Object runAsync(Object thing) {
        final Thread current = Thread.currentThread();
        if (!(current instanceof ScriptThread thread))
            throw new ScriptRuntimeError("Cannot create background process from non-script thread.");
        thread.skript.runOnAsyncThread((Instruction<?>) () -> {
            if (thing instanceof Method method)
                method.invoke(null);
            else if (thing instanceof Member method)
                method.invoke();
            else if (thing instanceof MethodAccessor<?> method)
                method.invoke();
            else if (thing instanceof Runnable runnable)
                runnable.run();
            else if (thing instanceof Future future)
                future.get();
        });
        return null;
    }
    
    public static MethodAccessor<Object> findFunction(Object owner, String name) {
        return Mirror.of(owner).useProvider(Skript.LOADER).method(name);
    }
    
    public static MethodAccessor<Object> getFunction(Object source, String pattern) {
        final String clean = pattern.trim();
        final Object owner;
        final int arguments;
        final String name;
        if (clean.contains("(")) {
            final String params = clean.substring(clean.indexOf('(') + 1, clean.indexOf(')')).trim();
            if (params.isEmpty()) arguments = 0;
            else arguments = count(params);
            name = clean.substring(0, clean.indexOf('(')).trim();
        } else {
            arguments = 0;
            if (clean.contains(" ")) name = clean.substring(0, clean.indexOf(' ')).trim();
            else name = clean.trim();
        }
        if (clean.contains(" from ")) {
            final String result = clean.substring(clean.indexOf(" from ") + 6).trim();
            try {
                owner = Class.forName(result.replace('/', '.'));
            } catch (ClassNotFoundException ex) {
                throw new ScriptRuntimeError("Unable to find script '" + result + "'", ex);
            }
        } else owner = source;
        return findFunction(owner, name, arguments);
    }
    
    private static int count(String pattern) {
        int count = 0;
        for (String s : pattern.split(",")) {
            if (s.isEmpty() || s.isBlank()) continue;
            count++;
        }
        return count;
    }
    
    public static MethodAccessor<Object> findFunction(Object owner, String name, Number arguments) {
        final Class<?>[] parameters = new Class[arguments.intValue()];
        Arrays.fill(parameters, Object.class);
        return Mirror.of(owner).useProvider(Skript.LOADER).method(name, parameters);
    }
    
    public Script getScript() {
        return script;
    }
    
    public Future<?> run(Skript skript, Object... arguments) {
        final ScriptRunner runner = new ScriptRunner() {
            @Override
            public void start() {
                invoker.invoke(arguments);
            }
            
            @Override
            public Class<? extends CompiledScript> owner() {
                return script.mainClass();
            }
        };
        return skript.runScript(runner);
    }
    
    public void verify() {
        if (verifier == null) return;
        verifier.invoke(new Object[parameters]);
    }
    
}
