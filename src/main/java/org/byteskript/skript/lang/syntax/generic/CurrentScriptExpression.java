/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.lang.syntax.generic;

import mx.kenzie.foundation.MethodBuilder;
import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.WriteInstruction;
import org.byteskript.skript.api.syntax.SimpleExpression;
import org.byteskript.skript.compiler.CommonTypes;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.compiler.SkriptLangSpec;
import org.byteskript.skript.lang.element.StandardElements;

public class CurrentScriptExpression extends SimpleExpression {
    
    public CurrentScriptExpression() {
        super(SkriptLangSpec.LIBRARY, StandardElements.EXPRESSION, "[the ][current ]script");
    }
    
    @Override
    public Type getReturnType() {
        return CommonTypes.STRING;
    }
    
    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        final MethodBuilder method = context.getMethod();
        assert method != null;
        method.writeCode(WriteInstruction.loadConstant(method.finish().getName()));
    }
    
}
