package org.futurepages.core.tags.cerne;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ListSorter implements Comparator<Object> {

   private String campo;
   
   public ListSorter(String campo) {
      this.campo = campo;
   }

   public int compare(Object primeiro, Object segundo) {

      if (primeiro == null) {
         return -1;

      } else {

         if (segundo == null) {
            return 1;

         } else {

            Method getMethod = null;

            try {
               getMethod = primeiro.getClass().getMethod("get".concat(campo.substring(0, 1).toUpperCase()).concat(campo.substring(1)), (Class[]) null);
            } catch (Exception e) {
               throw new IllegalStateException(e + " for ordenate.");
            }

            try {
               primeiro = getMethod.invoke(primeiro, (Object[]) null);
            } catch (Exception e) {
               primeiro = null;
            }

            try {
               segundo = getMethod.invoke(segundo, (Object[]) null);
            } catch (Exception e) {
               segundo = null;
            }

            if (primeiro == null && segundo == null)
               return 0;

            if (primeiro == null)
               return -1;

            if (segundo == null)
               return 1;

            // Number
            if (primeiro instanceof Number) {
               return new Double(String.valueOf(primeiro)).compareTo(new Double(String.valueOf(segundo)));
            }

            // Date
            if (primeiro instanceof Date) {
               return ((Date) primeiro).compareTo((Date) segundo);
            }

            // String
            if (primeiro instanceof String) {

               try {
                  Double d = Double.parseDouble(String.valueOf(primeiro));
                  return d.compareTo(new Double(String.valueOf(segundo)));

               } catch (Exception e) {
                  // REALY IS STRING

                  return String.valueOf(primeiro).compareToIgnoreCase(String.valueOf(segundo));
               }
            }

         }

      }

      return -1;
   }

   /**
    * Metodo responsavel por retorna uma lista de Object ordenada pelo nome do
    * campo informado
    * 
    * @param lista
    * @param campo
    * @param reverse
    * @return list sorted
    */
   public static List<Object> sort(List<Object> lista, String campo, boolean reverse) {
      
      if ((lista != null) && !lista.isEmpty() && (campo != null) && !(campo.trim().length() == 0)) {

         ListSorter o = new ListSorter(campo);
         
         Collections.sort(lista, o);

         if (reverse) Collections.reverse(lista);
      }

      return lista;
   }

}