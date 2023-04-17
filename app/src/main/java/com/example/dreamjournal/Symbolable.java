package com.example.dreamjournal;

import com.example.dreamjournal.models.Symbol;

import java.util.ArrayList;

public interface Symbolable {


    public ArrayList<Symbol> getAllSymbols();

    public Symbol getSymbolById(long id);

    public Symbol insertSymbol(Symbol s) throws Exception ;

    public Symbol updateSymbol(Symbol s) throws Exception ;

    public int deleteSymbol(Symbol s);

}
