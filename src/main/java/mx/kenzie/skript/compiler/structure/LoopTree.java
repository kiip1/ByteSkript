package mx.kenzie.skript.compiler.structure;

import mx.kenzie.foundation.MethodBuilder;
import mx.kenzie.skript.api.SyntaxElement;
import mx.kenzie.skript.compiler.Context;
import mx.kenzie.skript.error.ScriptCompileError;
import mx.kenzie.skript.lang.syntax.flow.conditional.ElseIfSection;
import mx.kenzie.skript.lang.syntax.flow.conditional.ElseSection;
import org.objectweb.asm.Label;

public class LoopTree extends ProgrammaticSplitTree {
    
    private final SectionMeta owner;
    private boolean open;
    private Label top;
    private final MultiLabel end;
    public int slot;
    
    public LoopTree(SectionMeta owner) {
        this.owner = owner;
        this.open = true;
        this.end = new MultiLabel();
    }
    
    public MultiLabel getEnd() {
        return end;
    }
    
    public Label getTop() {
        return top;
    }
    
    public void setTop(Label next) {
        this.top = next;
    }
    
    @Override
    public SectionMeta owner() {
        return owner;
    }
    
    @Override
    public void start(Context context) {
    
    }
    
    @Override
    public void branch(Context context) {
    
    }
    
    @Override
    public void close(Context context) {
        this.open = false;
        final MethodBuilder method = context.getMethod();
        if (method == null) throw new ScriptCompileError(context.lineNumber(), "Loop block left unclosed.");
        method.writeCode(end.instruction());
        context.removeTree(this);
    }
    
    @Override
    public boolean permit(SyntaxElement element) {
        return element instanceof ElseIfSection || element instanceof ElseSection;
    }
    
    @Override
    public boolean isOpen() {
        return open;
    }
    
}