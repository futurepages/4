package modules.admin.model.validators;

import edu.vt.middleware.dictionary.ArrayWordList;
import edu.vt.middleware.dictionary.WordListDictionary;
import edu.vt.middleware.dictionary.WordLists;
import edu.vt.middleware.dictionary.sort.ArraysSort;
import edu.vt.middleware.password.AlphabeticalCharacterRule;
import edu.vt.middleware.password.AlphabeticalSequenceRule;
import edu.vt.middleware.password.DictionarySubstringRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.QwertySequenceRule;
import edu.vt.middleware.password.RepeatCharacterRegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;
import edu.vt.middleware.password.WhitespaceRule;
import modules.admin.model.core.AdminConstants;
import modules.admin.model.entities.User;
import modules.admin.model.services.UserServices;
import org.futurepages.core.config.Apps;
import org.futurepages.core.exception.AppLogger;
import org.futurepages.core.validation.EntityValidator;
import org.futurepages.util.FileUtil;
import org.futurepages.util.Is;
import org.futurepages.util.The;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class UserValidator extends EntityValidator<UserServices,User> {

	@Override
	public void create(User user) {
		login(user);
		fullName(user);
		email(user);
		passwordSecurity(user);
	}

	@Override
	public void read(User user) {
	}

	@Override
	public void update(User user) {
		fullName(user);
		email(user);
		if(!Is.empty(user.getOldPassword()) || !Is.empty(user.getNewPassword())){
			newPassword(user);
		}
	}

	@Override
	public void delete(User user) {
	}

	public void fullName(User user) {
		if (Is.empty(user.getFullName())) {
			error("fullName", "Preencha o nome do usuário");
		}
	}

	public void email(User user) {
		if (Is.empty(user.getEmail())) {
			error("email", "Preencha o email do usuário");
		} else {
			if (!Is.validMail(user.getEmail())) {
				error("email", "E-mail inválido");
			}else if (services.dao().hasOtherWithEmail(user)) {
				error("email", "Este email já está cadastrado no sistema para outro usuário");
			}
		}
	}

	public void login(User user) {
		if (Is.empty(user.getLogin())) {
			error("login", "Preencha o campo do login");
		} else if (!Is.validStringKey(user.getLogin())) {
			error("login", "O login digitado é inválido");
		} else if (services.dao().hasOtherWithLogin(user)) {
			error("login", "Login digitado já existe");
		}
	}


	public void newPassword(User user) {
		if (Is.empty(user.getOldPassword()) || Is.empty(user.getNewPassword()) || Is.empty(user.getNewPasswordAgain())) {
			error("Se deseja alterar a senha, preencha todos os campos relativos a senha");
		}else{
			if (!(user.getEncriptPassword().equals(services.encriptedPassword(user, user.getOldPassword())))) {
				error("senhaAtual", "Senha atual não é válida");
			}

			if (!user.getNewPassword().equals(user.getNewPasswordAgain())) {
				error("novaSenha", "Senha de confirmação não confere");
			}
		}
		if(!Is.empty(user.getNewPassword())){
			passwordSecurity(user);
		}
	}

	public void newPasswordToForgottenPassword(User user, String newPassword, String confirmNewPassword) {

		if (Is.empty(newPassword) || Is.empty(confirmNewPassword)) {
			error("Preencha todos os dados");
		}

		if (!newPassword.equals(confirmNewPassword)) {
			error("Senha de confirmação não confere");
		}

		user.setPlainPassword(newPassword);
		passwordSecurity(user);

	}

	/*
	 * A senha deve ser diferente do login e de
	 * qualquer um dos tokens do nome completo
	 * e deve ser segura (várias regrinhas)
	 */
	public static String invalidPasswordMsg(User user) {
		String[] tokensNome;
		boolean hasError = false;
		String error = null;
		String errorSegurancaMinima = The.concat("Por questões de segurança, a senha deve possuir pelo menos ",
				String.valueOf(AdminConstants.MIN_SIZE_PASSWORD),
				" caracteres e não pode ser igual ao login e nem a um dos nomes do usuário");

		if (Is.empty(user.getPlainPassword())) {
			error =  "Preencha o campo da senha";
			hasError = true;
		} else if (user.getPlainPassword().length() < AdminConstants.MIN_SIZE_PASSWORD) {
			error = errorSegurancaMinima;
			hasError = true;
		} else if (user.getPlainPassword().equalsIgnoreCase(user.getLogin())) {
			error =  errorSegurancaMinima;
			hasError = true;
		} else {
			tokensNome = user.getFullName().split(" ");
			for (String token : tokensNome) {
				if (user.getPlainPassword().equalsIgnoreCase(token)) {
					error = errorSegurancaMinima;
					hasError = true;
					break;
				}
			}
		}
		if(!hasError){
			if(!passwordIsSecure(user.getPlainPassword(), 4)){ //busca tokens com pelo menos len-4 caracteres.
				error =  "A senha digitada não é segura. Evite sequências numéricas, caracteres repetidos, sequência de teclas vizinhas, espaços em branco e palavras conhecidas do dicionário";
			}
		}
		return error;
	}

    /*
     * Este método retorna true se a senha foi aceita ou false caso contrário.
     * Serão procuradas palavra no dicionário de tamanho no mínimo senha.length()-num
     * para saber se correspodem com alguma substring de senha, se correponder a senha
     * não será aceita, caso contrário ainda são avaliadas outras regras explicadas dentro do código.
     */
    public static boolean passwordIsSecure(String senha, int num) {
        AlphabeticalCharacterRule alphabeticalRule;
        WhitespaceRule whitespaceRule;
        QwertySequenceRule qwertySeqRule;
        AlphabeticalSequenceRule alphaSeqRule;
        RepeatCharacterRegexRule repeatCharRule;

        // Regra pra nao aceitar palavras contidas no dicionario de tamanho pelo menos o tamanho da senha menos 3
        dictRule.setWordLength(senha.length()-num);
        // Regra para a senha conter pelo menos um caractere alfabético
        alphabeticalRule = new AlphabeticalCharacterRule(1);
        // Regra pra não aceitar espaços em branco na senha
        whitespaceRule = new WhitespaceRule();
        // Regra pra não aceitar que a senha seja uma sequencia do teclado
        qwertySeqRule = new QwertySequenceRule(senha.length(), true);
        // Regra pra não aceitar que a senha que seja uma sequencia alfabética
        alphaSeqRule = new AlphabeticalSequenceRule(senha.length(), true);
        // Regra pra não aceitar que a senha seja contida somente de um mesmo caractere
        repeatCharRule = new RepeatCharacterRegexRule(senha.length());

        List<Rule> ruleList = new ArrayList<Rule>();
        ruleList.add(dictRule);
        ruleList.add(alphabeticalRule);
        ruleList.add(whitespaceRule);
        ruleList.add(qwertySeqRule);
        ruleList.add(alphaSeqRule);
        ruleList.add(repeatCharRule);

        PasswordValidator validator = new PasswordValidator(ruleList);
        PasswordData passwordData = new PasswordData(new Password(senha));
        RuleResult result = validator.validate(passwordData);

        if(!result.isValid()) {
//            for (String msg : validator.getMessages(result)) {
//                System.out.println(msg);
//            }
            return false;
        }
        else {
            return true;
        }
    }

    /* Dicionario carregado na memória pra não ter que toda vez que chamar testarForça
     precisar de novo do arquivo texto */
    private static DictionarySubstringRule dictRule;

    static {
        try {
            // Criacao do dicionario atraves do arquivo de texto que precisa ser no formato UTF-8
            ArrayWordList awl;
	        String dicPath = FileUtil.classRealPath(UserValidator.class)+"/res/dictionary_"+ Apps.get("LOCALE")+".txt";
            if(!(new File(dicPath)).exists()){
	            throw new Exception("Dictionary words for the locale '"+Apps.get("LOCALE")+"' not found: "+dicPath,null);
            }
	        awl = WordLists.createFromReader(
                new FileReader[] {new FileReader(dicPath)},
                true,
                new ArraysSort());
            WordListDictionary dict = new WordListDictionary(awl);
            // Regra do dicionario
            dictRule = new DictionarySubstringRule(dict);
            dictRule.setMatchBackwards(true);// com isso as palavras tambem sao colocadas ao contrario
        }
        catch (Exception e) {
			AppLogger.getInstance().execute(e);
        }
    }

	private void passwordSecurity(User user) {
		if(!Is.empty(user.getPlainPassword())){
			String invalidErrorMsg = invalidPasswordMsg(user);
			if(invalidErrorMsg!=null){
				error("password",invalidErrorMsg);
			}
		}
	}
}
