package mx.kenzie.skript.compiler;

import mx.kenzie.foundation.compiler.State;

public enum CompileState implements State {
    ROOT,
    MEMBER_BODY,
    CODE_BODY,
    STATEMENT,
    IMPORT_BODY,
    AREA_BODY,
    HEADER
}
