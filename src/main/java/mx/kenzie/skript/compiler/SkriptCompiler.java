package mx.kenzie.skript.compiler;

import mx.kenzie.foundation.Type;
import mx.kenzie.foundation.language.Compiler;
import mx.kenzie.foundation.language.PostCompileClass;
import mx.kenzie.skript.api.Library;

import java.io.InputStream;

public abstract class SkriptCompiler implements Compiler<SkriptLangSpec> {
    
    @Override
    public SkriptLangSpec getLanguage() {
        return SkriptLangSpec.INSTANCE;
    }
    
    public abstract Class<?> load(byte[] bytecode, String name);
    
    public abstract PostCompileClass[] compile(InputStream stream, Type name);
    
    public abstract PostCompileClass[] compile(InputStream file, String path);
    
    public abstract PostCompileClass[] compile(String file, Type path);
    
    public abstract boolean addLibrary(Library library);
    
    public abstract boolean removeLibrary(Library library);
    
}
