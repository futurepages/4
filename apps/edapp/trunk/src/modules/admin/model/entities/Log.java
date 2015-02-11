package modules.admin.model.entities;

import modules.admin.model.core.NotLoggeable;
import modules.admin.model.entities.enums.LogType;
import net.vidageek.mirror.dsl.Mirror;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.brazil.DateUtil;
import org.futurepages.util.Is;
import org.futurepages.util.ObjectContainer;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.StringUtils;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
//@org.hibernate.annotations.Table(appliesTo = "admin_log", indexes = {@Index(name = "idx_beanId", columnNames = {"idName", "idValue"})})
public class Log implements Serializable {

	public static final char ESCAPE = '#';
	public static final char EQUAL = '=';
	public static final char ATTR = '@';

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dateTime;

	@Lob
	private String logContent;

	@Lob
	private String obs;

	private String className;

	@Enumerated(EnumType.STRING)
	private LogType logType;

	@ManyToOne
	private User agent;
	private String ipHost;
    private String idValue;
    private String idName;

    @Transient
	private ArrayList<String> parserError = null;

    @Transient
	private ArrayList<ObjectContainer<String, String>> regs = new ArrayList<ObjectContainer<String, String>>();

    @Transient
	private HashMap<String, String> regsHash = new HashMap<String, String>();


	public Log() {
	}

	/**
	 * Log de Sistema. Ou seja: agent = NULL
	 */
	public Log(String logContent, String obs) {
		this.dateTime = Calendar.getInstance();
		this.logContent = logContent;
		this.logType = LogType.SYSTEM;
		this.obs = obs;
		System.out.println(StringUtils.concat("[", DateUtil.viewDateTime(dateTime, "dd/MM/yyyy - HH:mm:ss"), "] SYSTEM: ", logContent, ((obs != null) ? " (" + obs + ")" : "")));
	}

	/**
	 * Log de CRUD
	 */
	public Log(Object bean, LogType logType, User agent, String ipHost) {
		this.dateTime = Calendar.getInstance();
		this.logType = logType;
		this.agent = agent;
		this.ipHost = ipHost;
		generateLogContent(bean);
	}

	/**
	 * Log de modificaçao de Bean (porém, Não-CRUD) com observação.
	 */
	public Log(Object bean, LogType logType, User agent, String ipHost, String obs) {
		this(bean, logType, agent, ipHost);
		this.obs = obs;
	}

	/**
	 * Log de atividade que não envolve bean.
	 */
	public Log(String logContent, LogType logType, User agent, String ipHost) {
		this.dateTime = Calendar.getInstance();
		this.logContent = logContent;
		this.logType = logType;
		this.agent = agent;
		this.ipHost = ipHost;
	}

	/**
	 * Log de atividade que envolve bean com descrição da atividade.
	 */
	public Log(Object bean, String logContent, LogType logType, User agent, String ipHost, String obs) {
		this.dateTime = Calendar.getInstance();
		this.className = bean.getClass().getName();
		this.logContent = logContent;
		this.logType = logType;
		this.agent = agent;
		this.ipHost = ipHost;
		this.obs = obs;
	}

	/**
	 * Log que envolve bean
	 */
	public Log(Object bean, String logContent, LogType logType, User agent, String ipHost) {
		this.dateTime = Calendar.getInstance();
		this.className = bean.getClass().getName();
		this.logContent = logContent;
		this.logType = logType;
		this.agent = agent;
		this.ipHost = ipHost;
	}

	@Override
	public String toString() {

		int cont = 0;
		StringBuilder sb = new StringBuilder("[");

		if (!Is.empty(className)) {
			sb.append("{className:").append(className).append("}");
			cont++;
		}
		if (!Is.empty(idName) && !Is.empty(idValue)) {
			if (cont > 0) {
				sb.append(", ");
			}
			sb.append("{(idName:").append(idName).append("), (").append("idValue:").append(idValue).append(")}");
			cont++;
		}
		if (!Is.empty(agent)) {
			if (cont > 0) {
				sb.append(", ");
			}
			sb.append("{agent: ").append(agent).append("}");
			cont++;
		}
		if (!Is.empty(logType)) {
			if (cont > 0) {
				sb.append(", ");
			}
			sb.append("{operType: ").append(logType).append("}");
			cont++;
		}
		if (dateTime != null) {
			if (cont > 0) {
				sb.append(", ");
			}
			sb.append("{dateTime: ").append(DateUtil.viewDateTime(dateTime, "dd/MM/yyyy - HH:mm:ss")).append("}");
			cont++;
		}
		if (!Is.empty(ipHost)) {
			if (cont > 0) {
				sb.append(", ");
			}
			sb.append("{ipHost: ").append(ipHost).append("}");
			cont++;
		}
		if (!Is.empty(obs)) {
			if (cont > 0) {
				sb.append(", ");
			}
			sb.append("{obs: ").append(obs).append("}");
			cont++;
		}

		sb.append("]");

		return sb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getDateTime() {
		return dateTime;
	}

	public void setDateTime(Calendar dateTime) {
		this.dateTime = dateTime;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

	public User getAgent() {
		return agent;
	}

	public void setAgent(User agent) {
		this.agent = agent;
	}

	public String getIpHost() {
		return ipHost;
	}

	public void setIpHost(String ipHost) {
		this.ipHost = ipHost;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	/*
	 * Especiona e trata a string caso ela contenha caracteres especiais.
	 */
	public static String generateField(String str) {
		StringBuilder reg = new StringBuilder();

		// Se a string for vazia retorna a string "null".
		if (str == null || str.equals("")) {
			return "null";
		}

		// Varre a string em busca de caracteres especiais.
		// Caso encontre, adiciona o caractere de escape
		// para não ser confundido com o caractere especial
		// do log
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ESCAPE) {
				reg.append(str.charAt(i));
			} else {
				reg.append(ESCAPE).append(ESCAPE); // ##
			}
		}

		return reg.toString();
	}

	/*
	 * Concatena a String com o nome do atributo e o
	 * respectivo valor já com os caracteres especiais.
	 */
	public static String toLog(String attr, String value) {
		StringBuilder reg = new StringBuilder();

		reg.append(ESCAPE).append(ATTR);

		reg.append(generateField(attr)).append(ESCAPE).append(EQUAL).append(generateField(value)).append(ESCAPE).append(ATTR);

		return reg.toString();
	}

	public static String toLog(HashMap<String, String> mapa) {

		StringBuilder sb = new StringBuilder();

		for (String chave : mapa.keySet()) {
			sb.append(toLog(chave, mapa.get(chave)));
		}

		return sb.toString();
	}

	/*
	 * Retorna uma lista de tupla (ObjectContainer) com os pares
	 * {<nome do atributo>, <valor do atributo>}.
	 *
	 * Tem como uma entrada a string do log com os caracteres especiais.
	 */
	public ArrayList<ObjectContainer<String, String>> fromLog() {

		if (logContent == null || logContent.equals("")) {
			return null;
		}

        if (!regs.isEmpty()) {
            return regs;
        }

		parserError = new ArrayList<String>();
		ObjectContainer<String, String> tupla = null;
		StringBuilder buffer = null;
		boolean isAttr = false, isValue = false;
		char ch;

		// Laço que varre a string do log e desmembra em
		// atributos e valores.
		for (int i = 0; i < logContent.length(); i++) {
			ch = logContent.charAt(i);

			switch (ch) {
			case ESCAPE: // caso o caractere lido seja "#"
				i++;
				char c = logContent.charAt(i);
				if (c == ESCAPE) { // caso o próximo caractere seja "#"
					buffer.append(ESCAPE); // adiciona # ao buffer
				} else if (c == ATTR) { // caso o próximo caractere seja "@"
					if (isAttr == false) { // se verdade cria uma nova tupla (um tributo será
						isAttr = true;     // lido nos próximos laços
						buffer = new StringBuilder();
						tupla = new ObjectContainer<String, String>();
					} else if (isAttr == true && isValue == true) { // se verdade coloca o valor
						isAttr = false;                             // do buffer no valor da tupla
						isValue = false;                            // i.e. foi lido um valor
						tupla.setValue2(buffer.toString());

						regs.add(tupla);
						regsHash.put(tupla.getValue1(), tupla.getValue2());
					} else { // Erro!
						parserError.add("Erro: Está faltando um valor para o atributo " + buffer.toString() + "\n");
					}
				} else if (c == EQUAL) { // caso o próximo caractere seja "="
					if (isAttr == false) { // Erro!
						parserError.add("Erro: Falta um atributo\n");
					} else if (isAttr == true && isValue == false) { // se verdade coloca o valor
						tupla.setValue1(buffer.toString());          // do buffer no atributo
						buffer = new StringBuilder();                // i.e. foi lido um atributo
						isValue = true;
					} else if (isAttr == true && isValue == true) { // Erro!
						parserError.add("Erro: tentando adicionar mais de um valor para o atributo " + tupla.getValue1() + "\n");
					}
				} else { // Caso não seja um caractere especial, concatena no buffer
					buffer.append(c); // lançar erro? na verdade era esperado um dos seguintes
				}                     // caracteres: "#", "@", "=".
				break;

			default: // caso o caractere não seja "#", concatena no buffer.
				if(buffer==null){
					buffer =  new StringBuilder();
				}
				buffer.append(ch);
				break;
			}
		}

		return regs;
	}

	public ArrayList<ObjectContainer<String, String>> getFromLog() {
		return fromLog();
	}

	public String getPlainStringFromLog() {
		ArrayList<ObjectContainer<String, String>> registros = fromLog();
		StringBuilder sb = new StringBuilder("");

		if (registros != null && !registros.isEmpty()) {
			for (ObjectContainer<String, String> reg : registros) {
				sb.append(reg.getValue1()).append("=").append(reg.getValue2()).append("; ");
			}
		}

		return sb.toString();
	}

	public void setParserError(ArrayList<String> parserError) {
		this.parserError = parserError;
	}

	public ArrayList<String> getParserError() {
		return parserError;
	}

	private boolean isNotLoggeable(Field field) {

		if (field.isAnnotationPresent(NotLoggeable.class)) {
			NotLoggeable notLoggeable = field.getAnnotation(NotLoggeable.class);

			LogType [] lts = notLoggeable.when();
			for (LogType lt : lts) {
				if ((lt == this.logType) || (lt == LogType.ALL)) {
					return true;
				}
			}
		}
		if (field.isAnnotationPresent(Transient.class)) {
			return true;
		}
		if (field.isAnnotationPresent(Formula.class)) {
			return true;
		}

		return false;
	}

	private void generateLogOfField(StringBuilder conteudo, Object obj, Field field) {

		// executa caso o field não seja anotado com NotLoggeable, nem Transient e nem Formula
        int modifier = field.getModifiers();
		if (!isNotLoggeable(field) && !Modifier.isFinal(modifier)) {

			Object fieldObj = null;

			field.setAccessible(true);

			if (!field.isAnnotationPresent(Id.class)) {
				try {
					fieldObj = field.get(obj);

					if (fieldObj != null) {
						Class fieldClass = fieldObj.getClass();

						Method toString = fieldClass.getDeclaredMethod("toString");

						// concatena em conteudo (log)
						if (ReflectionUtil.isAnnotationPresentInHierarchy(fieldObj.getClass(), Entity.class)) { // caso seja uma entidade concatena o valor do id junto ao valor retornado pelo toString
							conteudo.append(toLog(field.getName(), StringUtils.concat("{", procuraNaHierarquia(fieldObj), ":", (String) toString.invoke(fieldObj), "}")));
						} else { // caso contrario, concatena apenas o valor retornado por toString
							conteudo.append(toLog(field.getName(), (String) toString.invoke(fieldObj)));
						}

					} else {
						// caso o valor do field seja vazio
						conteudo.append(toLog(field.getName(), "null"));
					}
				} catch (NoSuchMethodException ex) { // caso o field não possua um toString
					if (field.isAnnotationPresent(Temporal.class)) {
						conteudo.append(toLog(field.getName(), DateUtil.viewDateTime(fieldObj, "dd/MM/yyyy - HH:mm:ss")));

					} else if (field.isAnnotationPresent(Enumerated.class)) {
						Enumerated enumm = field.getAnnotation(Enumerated.class);
						EnumType enumType = enumm.value();

						if (enumType.equals(EnumType.ORDINAL)) {
							conteudo.append(toLog(field.getName(), String.valueOf(((Enum)fieldObj).ordinal())));

						} else {
							if (fieldObj instanceof Collection) {
								Collection collection = (Collection) fieldObj;
								List values = new ArrayList();
								for (Object e: collection) {
									values.add(((Enum) e).name());
								}
								conteudo.append(toLog(field.getName(), values.toString()));
							} else {
								conteudo.append(toLog(field.getName(), ((Enum) fieldObj).name()));
							}
						}

					} else {
						// caso o field seja um array, uma collection, um map, ou um
						// objeto de uma classe que não implemente o método "toString"
						conteudo.append(toLog(field.getName(), getObjValue(fieldObj)));
					}

				} catch (IllegalAccessException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalAccessException> " + ex.getMessage());
				} catch (IllegalArgumentException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalArgumentException> " + ex.getMessage());
				} catch (InvocationTargetException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <InvocationTargetException> " + ex.getMessage());
				}
			} else {
				try {
					idValue = String.valueOf(field.get(obj));
					idName = field.getName();
				}  catch (IllegalAccessException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalAccessException> " + ex.getMessage());
				}
			}
		}

	}


	/*
	 * Gera, a partir de um objeto qualquer, uma string de log contendo os
	 * caracteres especiais com todos os campos não anotados com @NotLoggeable
	 */
	public final void generateLogContent(Object object) {

		Object obj = object;
		Class<?> clss = obj.getClass();
		StringBuilder conteudo = new StringBuilder();
		className = clss.getName();

		List<Field> fields = new Mirror().on(clss).reflectAll().fields();
		/*
		 * Observe que na chamada ao método generateLogOfField o objeto referenciado por 'conteudo' é alterado dentro deste método
		 * (passagem de parâmetro por referência), mas como continua com a mesma referência tem acesso às alterações.
		 */
		for (Field field : fields) {
			generateLogOfField(conteudo, obj, field);
		}
		logContent = conteudo.toString();
	}

	/*
	 * Gera, a partir de um objeto "qualquer", uma string de log contendo os
	 * caracteres especiais de todos os field que contenham o nome passado como parâmetro
	 */
	public final void generateLogContent(Object object, boolean widthId, String ...fieldsNames) {

		ArrayList<String> listFieldName = new ArrayList<String>();
		listFieldName.addAll(Arrays.asList(fieldsNames));

		Object obj = object;
		Class<?> clss = obj.getClass();
		className = clss.getName();
		List<Field> fields = new Mirror().on(clss).reflectAll().fields();
		StringBuilder conteudo = new StringBuilder();
		for (Field field : fields) {
			if(listFieldName.contains(field.getName()) || (widthId && field.isAnnotationPresent(Id.class))){
				generateLogOfField(conteudo, obj, field);
				listFieldName.remove(field.getName());
			}
		}

		logContent = conteudo.toString();
	}

	/*
	 * Gera, a partir de um objeto "qualquer", uma string de log contendo os
	 * caracteres especiais de todos os field que contenham o nome passado como parâmetro
	 */
	public final void generateLogContent(Object object, String ...fieldsNames) {
		generateLogContent(object, false, fieldsNames);
	}

    private String procuraNaHierarquia(Object obj) {

        Class clss = obj.getClass();
        //TODO: here

        if (ReflectionUtil.isAnnotationPresentInHierarchy(clss, Entity.class)) {
            do {
                Field[] fields = clss.getDeclaredFields();

                for (Field field : fields) {
                    if (field.isAnnotationPresent(Id.class)) {

                        field.setAccessible(true);

                        //StringBuilder sb = new StringBuilder();

                        try {
                            Object objField = field.get(obj);

                            return String.valueOf(objField);
                            //sb.append(String.valueOf(objField));

                        } catch (IllegalAccessException ex) {
                            System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalAccessException> " + ex.getMessage());
                        } catch (IllegalArgumentException ex) {
                            System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalArgumentException> " + ex.getMessage());
                        }
                        break;
                    }
                }
            } while ((clss = clss.getSuperclass()) != null);
        } else {
            if (clss.isPrimitive()) {        // Estranho! Apesar o field ser um tipo primitivo, não passa por aqui
                return String.valueOf(obj);  // É impresso por toString.invoke();
            } else if(obj instanceof Calendar || obj instanceof Date)  {
                return DateUtil.viewDateTime(obj, "dd/MM/yyyy - HH:mm:ss");
            } else if (clss.isEnum()) {
                return ((Enum)obj).name();
            } else if (obj instanceof Map || clss.isArray() || obj instanceof Collection) {
                return getObjValue(obj);
            } else {
                try {
                    Method toString = clss.getDeclaredMethod("toString");

                    return (String) toString.invoke(obj);
                } catch (NoSuchMethodException ex) {
                    return "<NO_ID>";
                } catch (IllegalAccessException ex) {
                    System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalAccessException> " + ex.getMessage());
                } catch (InvocationTargetException ex) {
                    System.out.println("modules.admin.beans.Log.generateLogContent: <InvocationTargetException> " + ex.getMessage());
                }
            }
        }

        return "<NO_ID>";
    }

    /*
	private String procuraNaHierarquia(Object obj) {

		Class clss = obj.getClass();

		if (clss.isAnnotationPresent(Entity.class)) {
			List<Field> fields = new Mirror().on(this.getClass()).reflectAll().fieldsMatching(new Matcher<Field>() {
				public boolean accepts(Field arg0) {
					return arg0.isAnnotationPresent(Id.class);
				}
			});
			if(!fields.isEmpty()){
				try{
					Object objField = fields.get(0).get(obj).toString();
					return String.valueOf(objField);
				} catch (IllegalAccessException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalAccessException> " + ex.getMessage());
				} catch (IllegalArgumentException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalArgumentException> " + ex.getMessage());
				}
			}
		} else {
			if (clss.isPrimitive()) {        // Estranho! Apesar o field ser um tipo primitivo, nãoo passa por aqui
				return String.valueOf(obj);  // é impresso por toString.invoke();
			} else if(obj instanceof Calendar || obj instanceof Date)  {
				return DateUtil.viewDateTime(obj, "dd/MM/yyyy - HH:mm:ss");
			} else if (clss.isEnum()) {
				return ((Enum)obj).name();
			} else if (obj instanceof Map || clss.isArray() || obj instanceof Collection) {
				return getObjValue(obj);
			} else {
				try {
					Method toString = clss.getDeclaredMethod("toString");
					return (String) toString.invoke(obj);

				} catch (NoSuchMethodException ex) {
					return "<NO_ID>";
				} catch (IllegalAccessException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <IllegalAccessException> " + ex.getMessage());
				} catch (InvocationTargetException ex) {
					System.out.println("modules.admin.beans.Log.generateLogContent: <InvocationTargetException> " + ex.getMessage());
				}
			}
		}

		return "<NO_ID>";
	}
    */

	/*
	 * Metodo chamado caso o field seja um array, uma collection ou um objeto
	 * que não implemente o método "toString".
	 */
	public String getObjValue(Object obj) {


		Class clss = obj.getClass();
		Collection objs = null;

		if (obj instanceof Map) {
			Map map = (Map) obj;
			objs = map.values();
		}else if (clss.isArray()) {
			objs = arrayAsList(obj);
		}else if (obj instanceof Collection){
			objs = (Collection) obj;
		} else {
			if (obj != null) {
				return procuraNaHierarquia(obj);
			} else {
				return "null";
			}
		}


		StringBuilder log = new StringBuilder("[");
		int cont = 0;

		for (Object ob : objs) {

			if (ob != null) {
				if (cont > 0 && cont < objs.size()) {
					log.append(",");
				}

				log.append(procuraNaHierarquia(ob));

				cont++;
			} else {
				if (cont > 0 && cont < (objs.size())) {
					log.append(",");
				}
				log.append("null");
				cont++;
			}
		}

		log.append("]");
		return log.toString();
	}

	private List arrayAsList(Object obj) {
		List objs;
		try {
			Object[] array = (Object[]) obj;
			objs = Arrays.asList(array);
		} catch (ClassCastException e) {
			objs = new ArrayList<Object>();
			for (int i = 0; i < Array.getLength(obj); i++) {
				objs.add(Array.get(obj, i));
			}
		}
		return objs;
	}


	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}
}
