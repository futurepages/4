package org.futurepages.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class SecurityTest {

    @Test
    public void md5() {
        String senha1 = "123mudar";
        String senhaCriptografada1 = "89794b621a313bb59eed0d9f0f4e8205";
        String senha2 = "fermojupi";
        String senhaCriptografada2 = "0d3661e1de8d1e70a0253a981fc533d9";
        assertEquals("A senha md5 para '123mudar' não confere.", senhaCriptografada1, Security.md5(senha1));
        assertEquals("A senha md5 para 'fermojupi' não confere.", senhaCriptografada2, Security.md5(senha2));
        
        try {
            Security.md5(null);
            fail("NullPointerException Esperado.");
        }catch (NullPointerException ex) {}
    }
}