package org.futurepages.enums;

import org.futurepages.enums.MonthEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class MesEnumTest {

    @Test
    public void get() {
        assertEquals("Deveria ser 'janeiro'" ,"janeiro", MonthEnum.get(1));
        assertEquals("Deveria ser 'dezembro'","dezembro", MonthEnum.get(12));
    }

    @Test
    public void quantidadeDeDias() {
        assertEquals("Janeiro deveria possuir 31 dias.", 31, MonthEnum.daysCount(2006, 1));
        assertEquals("Dezembro deveria possuir 31 dias.", 31, MonthEnum.daysCount(2007, 1));
        assertEquals("Julho deveria possuir 31 dias.", 31, MonthEnum.daysCount(2008, 7));
        assertEquals("Este fevereiro não deveria ser de um ano bisexto.", 28, MonthEnum.daysCount(2009, 2));
        assertEquals("Este fevereiro deveria ser de um ano bisexto.", 29,  MonthEnum.daysCount(2008, 2));
    }


}