/*
 * Copyright (c) 2021 ByteSkript org (Moderocky)
 * View the full licence information and permissions:
 * https://github.com/Moderocky/ByteSkript/blob/master/LICENSE
 */

package org.byteskript.skript.lang.syntax.literal;

import mx.kenzie.foundation.MethodBuilder;
import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.WriteInstruction;
import org.byteskript.skript.api.syntax.Literal;
import org.byteskript.skript.compiler.CommonTypes;
import org.byteskript.skript.compiler.Context;
import org.byteskript.skript.compiler.Pattern;
import org.byteskript.skript.compiler.SkriptLangSpec;
import org.byteskript.skript.lang.element.StandardElements;

import java.util.regex.Matcher;

public class IntegerLiteral extends Literal<Integer> {
    
    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("^-?\\d+(?![\\d.#LlFfDd])");
    private static final int LOW = 48, HIGH = 57;
    
    public IntegerLiteral() {
        super(SkriptLangSpec.LIBRARY, StandardElements.EXPRESSION, "int literal");
    }
    
    @Override
    public boolean allowAsInputFor(Type type) {
        return CommonTypes.INTEGER.equals(type) || CommonTypes.NUMBER.equals(type) || CommonTypes.OBJECT.equals(type);
    }
    
    @Override
    public void compile(Context context, Pattern.Match match) {
        final String string = match.matcher().group();
        assert string.length() > 0;
        final MethodBuilder method = context.getMethod();
        assert method != null;
        final Integer value = Integer.valueOf(match.matcher().group());
        method.writeCode(WriteInstruction.loadConstant(value));
        try {
            method.writeCode(WriteInstruction.invokeStatic(Integer.class.getMethod("valueOf", int.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Integer parse(String input) {
        return Integer.valueOf(input);
    }
    
    @Override
    public Pattern.Match match(String thing, Context context) {
        final char c = thing.charAt(0);
        if (c != '-' && (c < LOW || c > HIGH)) return null;
        final Matcher matcher = PATTERN.matcher(thing);
        if (matcher.find()) return new Pattern.Match(matcher);
        return null;
    }
}
