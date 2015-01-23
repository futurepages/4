package org.futurepages.filters;

import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;


/**
 * Define o output com a chave "outKey". Este filtro retorna para o output
 * a string que denota a innerAction na chave passada como parâmetro no construtor.
 * @author leandro
 */
public class InnerActionOutputFilter implements Filter {

    private String outKey;


    public InnerActionOutputFilter(String outKey) {
        this.outKey = outKey;
    }

    public String filter(InvocationChain chain) throws Exception
    {
        if(chain.getInnerAction()!=null){

            chain.getAction().getOutput().setValue(outKey , chain.getInnerAction());
            
        }
        return chain.invoke();

    }

    public void destroy() {
    }
}