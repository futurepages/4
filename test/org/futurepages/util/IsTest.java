package org.futurepages.util;

import static org.junit.Assert.*;

import org.futurepages.test.factory.StringFactory;
import org.junit.Test;

public class IsTest {

	@Test
	public void testValidStringKey_valido_simples() {
		assertTrue("login válido - maria", Is.validStringKey("maria"));
	}

	@Test
	public void testValidStringKey_valido_comSublinhado() {
		assertTrue("login válido - wiltin", Is.validStringKey("maria_silva"));
	}

	@Test
	public void testValidStringKey_valido_comPonto() {
		assertTrue("login válido - veronica", Is.validStringKey("vero.nica"));
	}

	@Test
	public void testValidStringKey_valido_hifen() {
		assertTrue("login válido - samila", Is.validStringKey("sam-ila"));
	}

  	@Test
	public void testValidStringKey_invalido_vogalComTil() {
		assertFalse("Com vogal acentuada  'ã': 'mão' INVÁLIDO.", Is.validStringKey("mãoasd"));
	}

	@Test
	public void testValidStringKey_invalido_espacos() {
		assertFalse("Com espaços 'maria de fatima'. INVÁLIDO", Is.validStringKey("maria de fatima"));
	}

	@Test
	public void testValidStringKey_invalido_cedilha_espaco() {
		assertFalse("Com cedilha 'ç': 'caça'", Is.validStringKey("caça caça"));
	}

	@Test
	public void testValidStringKey_invalido_vogalAcentuada() {
		assertFalse("Vogal acentuada:'´': 'cacíldas'  INVÁLIDO", Is.validStringKey("cacíldas"));
	}

	@Test
	public void testValidStringKey_invalido_til() {
		assertFalse("Com til'~'- 'm~oasdfasdf'", Is.validStringKey("m~oasdfasdf"));
	}

	@Test
	public void testValidStringKey_invalido_acentoAgudo() {
		assertFalse("Com acento agudo '´' ", Is.validStringKey("asdf´ajkl"));
	}
	
	@Test
	public void testValidStringKey_invalido_pequeno_3() {
		assertFalse("Com tres caracteres- 'qwe'", Is.validStringKey("qwe"));
	}

	@Test
	public void testValidStringKey_invalidoComArroba() {
		assertFalse("com @ não é válido", Is.validStringKey("usuario@gmail.com"));
	}

	@Test
	public void testValidStringKey_comNumeroValido() {
		assertTrue("iniciando com número é válido", Is.validStringKey("123login"));
	}

	@Test
	public void testValidStringKey_comNumeroInvalido() {
		assertFalse("iniciando com número e é inválido", Is.validStringKey("123login",5,30,false));
	}

	@Test
	public void testValidStringKey_comNumeroValido2() {
		assertTrue("iniciando com número e é inválido", Is.validStringKey("123login",5,30,true));
	}

	@Test
	public void testValidStringKey_invalido_muitoGrande_50() {
		assertFalse("Muito grnade- 'size 50'", Is.validStringKey(StringFactory.getRandom(50)));
	}

  	@Test
  	public void testValidStringKey_invalido_espaco() {
		assertFalse("Com espacos 'sad _asd' INVÁLIDO", Is.validStringKey("sad _asd"));
	}

  	@Test
  	public void testValidURL_SEM_PONTO() {
		assertFalse("sem ponto INVÁLIDO", Is.validURL("http://wwworkutcom"));
	}
  	
  	@Test
  	public void testValidURL_SEM_http() {
		assertFalse("com ponto sem http", Is.validURL("www.orkut.com"));
	}

  	@Test
  	public void testValidURL_COM_https() {
		assertTrue("com ponto com https", Is.validURL("https://www.orkut.com"));
	}

	@Test
  	public void validCapitalizedPersonName() {
		assertTrue("", Is.validCapitalizedPersonName("Leandro Santana Pereira"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro da Silva Pereira"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro dos Santos e Silva I"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro de Silva e Santos da Costa II"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro dos Santos Jr"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro dos Santos XIII"));
		assertTrue("", Is.validCapitalizedPersonName("Maria da Silva e João"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo di Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo Di Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo d'Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo d`Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D`arque"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D`Arque"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D'arque"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D'Arque"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo de Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("D´lamare Amorim Araújo"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro S'Aeimun"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro S`Aeimun"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro S’Aeimun"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro S'a Silva"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro S'am Silva"));
		assertTrue("", Is.validCapitalizedPersonName("Amorim Sá"));

		assertFalse("", Is.validCapitalizedPersonName("Leandro dos Santos jr"));
		assertFalse("", Is.validCapitalizedPersonName("Amorim sa"));
		assertFalse("", Is.validCapitalizedPersonName("Amorim SA"));
		assertFalse("", Is.validCapitalizedPersonName("Del´lamare Amorim Araújo"));
		assertFalse("", Is.validCapitalizedPersonName("TANIA Maria da Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro A Santana Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("A' Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro S'A"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro S' Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro 'A Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro 'a Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro AA'Aaaa Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro s'A"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro S'aM"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro S'M"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro sE Santana Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro sA Santana Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro dE Santana Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro dos santanA"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro dos Santos Xiii"));
		assertFalse("", Is.validCapitalizedPersonName("LEandro dos SAntos Xiii"));
		assertFalse("", Is.validCapitalizedPersonName("LEandro dos Santos Xiii"));
		assertFalse("", Is.validCapitalizedPersonName("Leonardo di CapriÓ"));
		assertFalse("", Is.validCapitalizedPersonName("Leonardo dU Ciul"));
		assertFalse("", Is.validCapitalizedPersonName("Leonardo dE Ciul"));
		assertFalse("", Is.validCapitalizedPersonName("Leonardo De Ciul"));
		assertFalse("", Is.validCapitalizedPersonName("leandro"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro"));
		assertFalse("", Is.validCapitalizedPersonName("LEANDRO"));
		assertFalse("", Is.validCapitalizedPersonName("lEANDRO dE sANTANA"));
		assertFalse("", Is.validCapitalizedPersonName("Leonardo diCaprio"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro dos Santos xiii"));
		assertFalse("", Is.validCapitalizedPersonName("leandro santana pereira"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro De Santana Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("LEANDRO SANTANA DE PEREIRA"));
		assertFalse("", Is.validCapitalizedPersonName("LEANDRO SANTANA PEREIRA"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro santana pereira"));
		assertFalse("", Is.validCapitalizedPersonName("lEANDRO DE sANTANA pEREIRA"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro santana de Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro Dos Santos goe"));
		assertFalse("", Is.validCapitalizedPersonName("TANIA Maria da Silva"));
		assertFalse("", Is.validCapitalizedPersonName("TANIA MARIA da Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro a'a Silva"));
		assertFalse("", Is.validCapitalizedPersonName("Leonardo dI Caprio"));

	}
}