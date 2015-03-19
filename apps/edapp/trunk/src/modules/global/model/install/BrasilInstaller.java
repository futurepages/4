package modules.global.model.install;

import modules.global.model.install.brasil.CidadesBrasileiras;
import modules.global.model.install.brasil.CidadesEstrangeiras;
import modules.global.model.install.brasil.EstadosBrasileiros;
import modules.global.model.install.brasil.Paises;
import modules.global.model.install.brasil.RegioesBrasileiras;
import org.futurepages.core.install.Installer;

public class BrasilInstaller extends Installer {

    public void execute() {
		System.out.println(" -> Instalando Países");
		Paises.instalaTodos();

		System.out.println(" -> Instalando Cidades Estrangeiras");
		CidadesEstrangeiras.instalaAlgumas();


		System.out.println(" -> Instalando Regiões");
		RegioesBrasileiras.instalaTodas();

		System.out.println(" -> Instalando Estados e Cidades Brasileiras");
        (new EstadosBrasileiros(new CidadesBrasileiras())).instalaTodos();

		
    }
}
