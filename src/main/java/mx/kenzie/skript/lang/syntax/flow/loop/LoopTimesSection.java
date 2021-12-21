package mx.kenzie.skript.lang.syntax.flow.loop;

import mx.kenzie.foundation.MethodBuilder;
import mx.kenzie.foundation.WriteInstruction;
import mx.kenzie.foundation.compiler.State;
import mx.kenzie.skript.api.syntax.Section;
import mx.kenzie.skript.compiler.*;
import mx.kenzie.skript.compiler.structure.LoopTree;
import mx.kenzie.skript.compiler.structure.PreVariable;
import mx.kenzie.skript.compiler.structure.SectionMeta;
import mx.kenzie.skript.error.ScriptCompileError;
import mx.kenzie.skript.lang.element.StandardElements;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

public class LoopTimesSection extends Section {
    
    public LoopTimesSection() {
        super(SkriptLangSpec.LIBRARY, StandardElements.SECTION, "loop %Number% times");
    }
    
    @Override
    public void preCompile(Context context, Pattern.Match match) throws Throwable {
        final LoopTree tree = new LoopTree(context.getSection(1));
        context.createTree(tree);
    }
    
    @Override
    public void preCompileInline(Context context, Pattern.Match match) throws Throwable {
        final LoopTree tree = new LoopTree(context.getSection());
        context.createTree(tree);
    }
    
    @Override
    public void compileInline(Context context, Pattern.Match match) throws Throwable {
        if (!(context.getTree(context.getSection()) instanceof LoopTree tree))
            throw new ScriptCompileError(context.lineNumber(), "Illegal mid-statement flow break.");
        final MethodBuilder method = context.getMethod();
        assert method != null;
        method.writeCode(WriteInstruction.cast(CommonTypes.NUMBER));
        method.writeCode(WriteInstruction.invokeVirtual(Number.class.getMethod("intValue")));
        final PreVariable variable = new PreVariable(null);
        variable.internal = true;
        context.forceUnspecVariable(variable);
        final int slot = context.slotOf(variable);
        final Label top = new Label();
        tree.setTop(top);
        tree.slot = slot;
        method.writeCode(WriteInstruction.storeSmall(slot)); // store loop number
        method.writeCode((writer, visitor) -> visitor.visitLabel(top));
        method.writeCode(WriteInstruction.incrementSmall(slot, -1));
        context.setState(CompileState.CODE_BODY);
    }
    
    @Override
    public void compile(Context context, Pattern.Match match) throws Throwable {
        if (!(context.getTree(context.getSection(1)) instanceof LoopTree tree))
            throw new ScriptCompileError(context.lineNumber(), "Illegal mid-statement flow break.");
        context.setState(CompileState.CODE_BODY);
        final MethodBuilder method = context.getMethod();
        assert method != null;
        method.writeCode(WriteInstruction.cast(CommonTypes.NUMBER));
        method.writeCode(WriteInstruction.invokeVirtual(Number.class.getMethod("intValue")));
        final PreVariable variable = new PreVariable(null);
        variable.internal = true;
        context.forceUnspecVariable(variable);
        final int slot = context.slotOf(variable);
        final Label top = new Label();
        final Label end = tree.getEnd().use();
        tree.setTop(top);
        tree.slot = slot;
        method.writeCode(WriteInstruction.storeSmall(slot)); // store loop number
        method.writeCode((writer, visitor) -> visitor.visitLabel(top));
        method.writeCode(WriteInstruction.loadSmall(slot));
        method.writeCode((writer, visitor) -> visitor.visitJumpInsn(Opcodes.IFLE, end));
        method.writeCode(WriteInstruction.incrementSmall(slot, -1));
        context.setState(CompileState.CODE_BODY);
    }
    
    @Override
    public Pattern.Match match(String thing, Context context) {
        if (!thing.startsWith("loop ")) return null;
        if (!thing.endsWith(" times")) return null;
        return super.match(thing, context);
    }
    
    @Override
    public boolean allowedIn(State state, Context context) {
        return super.allowedIn(state, context)
            && context.getSection() != null
            && context.getMethod() != null;
    }
    
    @Override
    public void onSectionExit(Context context, SectionMeta meta) {
        if (!(context.getTree(context.getSection()) instanceof LoopTree tree))
            throw new ScriptCompileError(context.lineNumber(), "Unable to balance loop flow tree.");
        context.setState(CompileState.CODE_BODY);
        final MethodBuilder method = context.getMethod();
        final Label top = tree.getTop();
        final int slot = tree.slot;
        method.writeCode(WriteInstruction.loadSmall(slot));
        method.writeCode((writer, visitor) -> visitor.visitJumpInsn(Opcodes.IFGT, top));
        method.writeCode(tree.getEnd().instruction());
    }
    
}