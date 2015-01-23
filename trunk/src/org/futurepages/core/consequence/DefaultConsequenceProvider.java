package org.futurepages.core.consequence;

import org.futurepages.consequences.Forward;
import org.futurepages.core.config.Params;

/**
 * The default consequence provider used by Mentawai controller 
 * when autoView is set to true, which is the default anyways.
 * 
 * @author Sergio Oliveira Jr.
 */
public class DefaultConsequenceProvider implements ConsequenceProvider {
   
    /**
     * @byLeandro
     * Novo tipo de consequencia padrao caso nao seja definido.
     * Action.inner.fpg ==> Action.inner.page
     * Action.fpg ==> Action.page
     * 
     **/
	@Override
   public Consequence getConsequence(String action, Class<? extends Object> actionClass, String result, String innerAction) {

	   StringBuilder sb = new StringBuilder(128);
	   boolean isPretty = Params.get("PRETTY_URL").equals("true");

       if(isPretty){
            sb.append(Params.MODULES_PATH);
       }

	   sb.append("/").append(action);


       if (innerAction != null) {

		    sb.append(isPretty? "-":".").append(innerAction);

		}
       
        sb.append(".page");

		Consequence c = new Forward(sb.toString());

		return c;
   }
}
