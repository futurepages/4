package modules.global.model.install;

import modules.global.model.entities.Banco;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.install.Installer;
/**
 *
 * @author Severiano Alves
 */
public class Bancos extends Installer  {

	@Override
	public void execute() throws Exception {
		System.out.println(" -> Instalando Bancos");
		instalaBancos();
	}

	public static void instalaBancos() {
		Dao.getInstance().save(new Banco(1, "BANCO DO BRASIL S.A.", "Banco do Brasil"));
		Dao.getInstance().save(new Banco(104, "CAIXA ECONÔMICA FEDERAL", "Caixa Econômica Federal"));
		Dao.getInstance().save(new Banco(756, "BANCO COOPERATIVO DO BRASIL S.A.", "SICOOB Juriscred (BANCOOB)"));
		Dao.getInstance().save(new Banco(33, "BANCO SANTANDER (BRASIL) S.A.", null));
		Dao.getInstance().save(new Banco(4, "BANCO DO NORDESTE DO BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(237, "BANCO BRADESCO S.A.", null));
		Dao.getInstance().save(new Banco(341, "BANCO ITAÚ S.A.", null));
		Dao.getInstance().save(new Banco(399, "HSBC BANK BRASIL S.A. - BANCO MULTIPLO", null));
		Dao.getInstance().save(new Banco(409, "UNIBANCO - UNIÃO DE BANCOS BRASILEIROS S.A.", null));
		Dao.getInstance().save(new Banco(3, "BANCO DA AMAZÔNIA S.A.", null));
		Dao.getInstance().save(new Banco(14, "BANCO NATIXIS BRASIL S.A", null));
		Dao.getInstance().save(new Banco(19, "BANCO AZTECA DO BRASIL S.A", null));
		Dao.getInstance().save(new Banco(21, "BANESTES S.A. BANCO DO ESTADO DO ESPIRITO SANTO", null));
		Dao.getInstance().save(new Banco(24, "BANCO DE PERNAMBUCO S.A.- BANDEPE", null));
		Dao.getInstance().save(new Banco(25, "BANCO ALFA S.A.", null));
		Dao.getInstance().save(new Banco(29, "BANCO BANERJ S.A.", null));
		Dao.getInstance().save(new Banco(31, "BANCO BEG S.A", null));
		Dao.getInstance().save(new Banco(36, "BRADESCO BBI", null));
		Dao.getInstance().save(new Banco(37, "BANCO DO ESTADO DO PARA S.A.", null));
		Dao.getInstance().save(new Banco(39, "BANCO DO ESTADO DO PIAUÍ", "Banco do Estado do Piauí", false));
		Dao.getInstance().save(new Banco(40, "BANCO CARGILL S.A.", null));
		Dao.getInstance().save(new Banco(41, "BANCO DO ESTADO DO RIO GRANDE DO SUL S.A.", null));
		Dao.getInstance().save(new Banco(44, "BANCO BVA S.A.", null));
		Dao.getInstance().save(new Banco(45, "BANCO OPPORTUNITY S.A.", null));
		Dao.getInstance().save(new Banco(47, "BANCO DO ESTADO DE SERGIPE S.A.", null));
		Dao.getInstance().save(new Banco(62, "HIPERCARD BANCO MÚLTIPLO S.A.", null));
		Dao.getInstance().save(new Banco(63, "BANCO IBI S.A.- BANCO MÚLTIPLO", null));
		Dao.getInstance().save(new Banco(64, "GOLDMAN SACHS DO BRASIL - BANCO MÚLTIPLO S.A.", null));
		Dao.getInstance().save(new Banco(65, "BANCO LEMON S.A.", null));
		Dao.getInstance().save(new Banco(66, "BANCO MORGAN STANLEY DEAN WITTER S.A.", null));
		Dao.getInstance().save(new Banco(69, "BPN BRASIL BANCO MÚLTIPLO S.A", null));
		Dao.getInstance().save(new Banco(70, "BRB - BANCO DE BRASILIA S.A.", null));
		Dao.getInstance().save(new Banco(72, "BANCO RURAL MAIS S.A", null));
		Dao.getInstance().save(new Banco(73, "BB - BANCO POPULAR DO BRASIL S.A", null));
		Dao.getInstance().save(new Banco(74, "BANCO J. SAFRA S.A", null));
		Dao.getInstance().save(new Banco(75, "BANCO CR2 S.A", null));
		Dao.getInstance().save(new Banco(76, "BANCO KDB S.A", null));
		Dao.getInstance().save(new Banco(77, "BANCO INTERMEDIUM S.A", null));
		Dao.getInstance().save(new Banco(78, "BANCO DO ESPIRITO SANTO - BES INVESTIMENTO DO BRASIL S.A", null));
		Dao.getInstance().save(new Banco(79, "BANCO JBS S.A", null));
		Dao.getInstance().save(new Banco(81, "CONCORDIA BANCO S.A", null));
		Dao.getInstance().save(new Banco(82, "BANCO TOPÁZIO S.A", null));
		Dao.getInstance().save(new Banco(83, "BANCO DA CHINA BRASIL S.A", null));
		Dao.getInstance().save(new Banco(84, "COOPERATIVA - UNICRED - NORTE DO PARANÁ", null));
		Dao.getInstance().save(new Banco(85, "COOPERATIVA CENTRAL DE CRÉDITO URBANO - CECRED", null));
		Dao.getInstance().save(new Banco(86, "OBOÉ CRÉDITO, FINANC. E INVEST. S.A", null));
		Dao.getInstance().save(new Banco(87, "COOPERATIVA DE CRÉDITO - UNICRED CENTRAL DE STA. CATARINA", null));
		Dao.getInstance().save(new Banco(88, "BANCO RANDON S.A", null));
		Dao.getInstance().save(new Banco(89, "COOPERATIVA DE CRÉDITO DA REGIÃO DA MOGIANA", null));
		Dao.getInstance().save(new Banco(90, "COOPERATIVA DE CRÉDITO DO ESTADO DE S. PAULO LTDA", null));
		Dao.getInstance().save(new Banco(92, "BRICKELL S.A CRÉDITO, FINANCIAMENTO E INVESTIMENTO", null));
		Dao.getInstance().save(new Banco(94, "BANCO PETRA S.A", null));
		Dao.getInstance().save(new Banco(96, "BANCO BMF S.A", null));
		Dao.getInstance().save(new Banco(107, "BANCO BBM S.A.", null));
		Dao.getInstance().save(new Banco(151, "BANCO NOSSA CAIXA S.A", null));
		Dao.getInstance().save(new Banco(168, "HSBC INVESTMENT BANK BRASIL S.A.- BANCO MÚLTIPLO", null));
		Dao.getInstance().save(new Banco(184, "BANCO ITAÚ BBA S.A.", null));
		Dao.getInstance().save(new Banco(204, "BANCO BRADESCO CARTÕES S.A.", null));
		Dao.getInstance().save(new Banco(208, "BANCO UBS PACTUAL S.A.", null));
		Dao.getInstance().save(new Banco(210, "DRESDNER BANK LATEINAMERIKA AKTIENGESELLSCHAFT", null));
		Dao.getInstance().save(new Banco(212, "BANCO MATONE S.A.", null));
		Dao.getInstance().save(new Banco(213, "BANCO ARBI S.A.", null));
		Dao.getInstance().save(new Banco(214, "BANCO DIBENS S.A.", null));
		Dao.getInstance().save(new Banco(215, "BANCO COMERCIAL E DE INVESTIMENTO SUDAMERIS S.A.", null));
		Dao.getInstance().save(new Banco(217, "BANCO JOHN DEERE S.A.", null));
		Dao.getInstance().save(new Banco(218, "BANCO BONSUCESSO S.A.", null));
		Dao.getInstance().save(new Banco(222, "BANCO CALYON BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(224, "BANCO FIBRA S.A.", null));
		Dao.getInstance().save(new Banco(225, "BANCO BRASCAN S.A.", null));
		Dao.getInstance().save(new Banco(229, "BANCO CRUZEIRO DO SUL S.A.", null));
		Dao.getInstance().save(new Banco(230, "UNICARD BANCO MÚLTIPLO S.A.", null));
		Dao.getInstance().save(new Banco(233, "BANCO GE CAPITAL S.A", null));
		Dao.getInstance().save(new Banco(241, "BANCO CLASSICO S.A.", null));
		Dao.getInstance().save(new Banco(243, "BANCO MÁXIMA S.A.", null));
		Dao.getInstance().save(new Banco(246, "BANCO ABC - BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(248, "BANCO BOA VISTA INTERATLANTICO S.A", null));
		Dao.getInstance().save(new Banco(249, "BANCO INVESTCRED UNIBANCO S.A.", null));
		Dao.getInstance().save(new Banco(250, "BANCO SCHAHIN", null));
		Dao.getInstance().save(new Banco(254, "PARANA BANCO S.A.", null));
		Dao.getInstance().save(new Banco(263, "BANCO CACIQUE S.A.", null));
		Dao.getInstance().save(new Banco(265, "BANCO FATOR S.A.", null));
		Dao.getInstance().save(new Banco(266, "BANCO CEDULA S.A.", null));
		Dao.getInstance().save(new Banco(267, "BANCO BBM - COM.C.IMOB.CFI S.A.", null));
		Dao.getInstance().save(new Banco(300, "BANCO DE LA NACION ARGENTINA", null));
		Dao.getInstance().save(new Banco(318, "BANCO BMG S.A.", null));
		Dao.getInstance().save(new Banco(320, "BANCO INDUSTRIAL E COMERCIAL S.A.", null));
		Dao.getInstance().save(new Banco(353, "BANCO SANTANDER (BRASIL) S.A.", null));
		Dao.getInstance().save(new Banco(366, "BANCO SOCIETE GENERALE BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(370, "BANCO WESTLB DO BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(376, "BANCO J.P. MORGAN S.A.", null));
		Dao.getInstance().save(new Banco(389, "BANCO MERCANTIL DO BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(394, "BANCO FINASA BMC S.A.", null));
		Dao.getInstance().save(new Banco(412, "BANCO CAPITAL S.A.", null));
		Dao.getInstance().save(new Banco(422, "BANCO SAFRA S.A.", null));
		Dao.getInstance().save(new Banco(453, "BANCO RURAL S.A.", null));
		Dao.getInstance().save(new Banco(456, "BANCO DE TOKYO MITSUBISHI BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(464, "BANCO SUMITOMO MITSUI BRASILEIRO S.A.", null));
		Dao.getInstance().save(new Banco(473, "BANCO CAIXA GERAL - BRASIL S.A", null));
		Dao.getInstance().save(new Banco(477, "CITIBANK N.A.", null));
		Dao.getInstance().save(new Banco(479, "BANCO ITAÚ BANK S.A.", null));
		Dao.getInstance().save(new Banco(487, "DEUTSCHE BANK S. A. - BANCO ALEMÃO", null));
		Dao.getInstance().save(new Banco(488, "JP MORGAN CHASE BANK NATIONAL ASSOCIATION", null));
		Dao.getInstance().save(new Banco(492, "ING BANK N.V.", null));
		Dao.getInstance().save(new Banco(494, "BANCO DE LA REPUBLICA ORIENTAL DEL URUGUAY", null));
		Dao.getInstance().save(new Banco(495, "BANCO DE LA PROVINCIA DE BUENOS AIRES", null));
		Dao.getInstance().save(new Banco(505, "BANCO CREDIT SUISSE (BRASIL) S.A.", null));
		Dao.getInstance().save(new Banco(600, "BANCO LUSO BRASILEIRO S.A.", null));
		Dao.getInstance().save(new Banco(604, "BANCO INDUSTRIAL DO BRASIL S. A.", null));
		Dao.getInstance().save(new Banco(610, "BANCO VR S.A.", null));
		Dao.getInstance().save(new Banco(611, "BANCO PAULISTA S.A.", null));
		Dao.getInstance().save(new Banco(612, "BANCO GUANABARA S.A.", null));
		Dao.getInstance().save(new Banco(613, "BANCO PECUNIA S.A", null));
		Dao.getInstance().save(new Banco(623, "BANCO PANAMERICANO S.A.", null));
		Dao.getInstance().save(new Banco(626, "BANCO FICSA S.A.", null));
		Dao.getInstance().save(new Banco(630, "BANCO INTERCAP S.A.", null));
		Dao.getInstance().save(new Banco(633, "BANCO RENDIMENTO S.A.", null));
		Dao.getInstance().save(new Banco(634, "BANCO TRIANGULO S.A.", null));
		Dao.getInstance().save(new Banco(637, "BANCO SOFISA S.A.", null));
		Dao.getInstance().save(new Banco(638, "BANCO PROSPER S.A.", null));
		Dao.getInstance().save(new Banco(641, "BANCO ALVORADA S.A", null));
		Dao.getInstance().save(new Banco(643, "BANCO PINE S.A.", null));
		Dao.getInstance().save(new Banco(652, "ITAÚ UNIBANCO - BANCO MÚLTIPLO S.A.", null));
		Dao.getInstance().save(new Banco(653, "BANCO INDUSVAL S.A.", null));
		Dao.getInstance().save(new Banco(654, "BANCO A.J. RENNER S.A.", null));
		Dao.getInstance().save(new Banco(655, "BANCO VOTORANTIM S.A.", null));
		Dao.getInstance().save(new Banco(707, "BANCO DAYCOVAL S.A.", null));
		Dao.getInstance().save(new Banco(719, "BANIF - BANCO INTERNACIONAL DO FUNCHAL (BRASIL) S.A.", null));
		Dao.getInstance().save(new Banco(720, "BANCO MAXINVEST S.A.", null));
		Dao.getInstance().save(new Banco(721, "BANCO CREDIBEL S.A.", null));
		Dao.getInstance().save(new Banco(734, "BANCO GERDAU S.A.", null));
		Dao.getInstance().save(new Banco(735, "BANCO POTTENCIAL S.A.", null));
		Dao.getInstance().save(new Banco(738, "BANCO MORADA S.A.", null));
		Dao.getInstance().save(new Banco(739, "BANCO BGN S.A.", null));
		Dao.getInstance().save(new Banco(740, "BANCO BARCLAYS S.A.", null));
		Dao.getInstance().save(new Banco(741, "BANCO RIBEIRAO PRETO S.A.", null));
		Dao.getInstance().save(new Banco(743, "BANCO SEMEAR S.A.", null));
		Dao.getInstance().save(new Banco(745, "BANCO CITIBANK S.A.", null));
		Dao.getInstance().save(new Banco(746, "BANCO MODAL S.A.", null));
		Dao.getInstance().save(new Banco(747, "BANCO RABOBANK INTERNATIONAL BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(748, "BANCO COOPERATIVO SICREDI S.A. - BANSICREDI", null));
		Dao.getInstance().save(new Banco(749, "BANCO SIMPLES S.A.", null));
		Dao.getInstance().save(new Banco(751, "DRESDNER BANK BRASIL S.A. BANCO MÚLTIPLO", null));
		Dao.getInstance().save(new Banco(752, "BANCO BNP PARIBAS BRASIL S.A.", null));
		Dao.getInstance().save(new Banco(753, "NBC BANK BRASIL S.A. BANCO MÚLTIPLO", null));
		Dao.getInstance().save(new Banco(757, "BANCO KEB DO BRASIL S.A.", null));


	}

}
