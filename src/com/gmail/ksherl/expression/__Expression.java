/*
 * Created on Dec 1, 2006
 * Dec 1, 2006 3:17:35 PM
 */
package com.gmail.ksherl.expression;

import java.io.PrintStream;
import java.util.ArrayList;

import com.gmail.ksherl.asm816.SymbolTable;

public interface __Expression
{
    public String toString();
    public Integer Value();
    public void PrintValue(PrintStream ps);
    public ArrayList Serialize(String root);
    public boolean isConst();
    
    public __Expression Simplify(SymbolTable st, boolean deep);
    
}
