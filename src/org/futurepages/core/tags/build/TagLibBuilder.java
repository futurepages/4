package org.futurepages.core.tags.build;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.futurepages.annotations.Tag;
import org.futurepages.core.config.Params;
import org.futurepages.exceptions.NotModuleException;
import org.futurepages.util.FileUtil;
import org.futurepages.util.The;

/**
 * Classe para a construção da declaração de taglib.
 * @author Danilo
 */
public class TagLibBuilder extends ModulesAutomation{

	protected static final String BASE_TAGLIB_URL = "/META-INF/futurepages.tld";
	private final String APP_TAGLIB_NAME = "taglib.tld";

	private final String TAGS_REPLACE_CONSTANT = "<!-- ${TAGS_REPLACE} -->";
	private final String TAGSFILE_REPLACE_CONSTANT = "<!-- ${TAGSFILE_REPLACE} -->";
	private final String TAGLIB_BASE_URI = "<uri>http://futurepages.org/taglib.tld</uri>";
	private final String TAGLIB_APP_URI = "<uri>futurepagesApp</uri>";
	private final String TAGLIB_BASE_DYSPLAYNAME = "<display-name>futurepages</display-name>";
	private final String TAGLIB_APP_DYSPLAYNAME = "<display-name>futurepages App</display-name>";


	public TagLibBuilder(File[] modules) {
		super(modules, "tags");
	}
	

	/**
	 * Construção da definição geral da declaração do arquivo taglib.tld
	 * @throws java.lang.Exception
	 */
	public void build() throws Exception {
		Map<String, String> contentMap = new HashMap<String, String>();
		String applicationTaglibPath = Params.get("WEBINF_PATH") + "/" + APP_TAGLIB_NAME;

		contentMap.put(TAGLIB_BASE_URI, TAGLIB_APP_URI);
		contentMap.put(TAGLIB_BASE_DYSPLAYNAME, TAGLIB_APP_DYSPLAYNAME);

		String tagDeclaration = buildTagLibsContentFromModules();
		tagDeclaration += buildTagLibsContentFromApp();

		contentMap.put(TAGS_REPLACE_CONSTANT, tagDeclaration);
		contentMap.put(TAGSFILE_REPLACE_CONSTANT, buildTagLibsContentFromFiles());

		URL baseTaglibURL = getClass().getResource(BASE_TAGLIB_URL);
		FileUtil.putKeyValue(contentMap, baseTaglibURL, applicationTaglibPath);
		System.out.println("[::taglibBuilder::] Arquivo de taglib criado: " + applicationTaglibPath);
	}

	private String buildTagLibsContentFromApp() {
		TagBean tag;
		List<Class<Object>> classes = super.getApplicationClasses(null,Tag.class);
		StringBuilder tagDeclarations = tagsDeclaration(classes);
		return "\n   <!-- Generated Application Tags From Root Application -->\n\n" + tagDeclarations.toString();
	}

	public static StringBuilder tagsDeclaration(List<Class<Object>> classes) {
		TagBean tag;
		StringBuilder tagDeclarations = new StringBuilder();
		ClassTagAnnotationReader reader = new ClassTagAnnotationReader();
		
		Collections.sort(classes, new Comparator<Class>(){
			@Override
			public int compare(Class c1, Class c2) {
				return c1.getSimpleName().compareTo(c2.getSimpleName());
			}
		});
		
		for (Class<?> tagClass : classes) {
			tag = reader.readTag(tagClass);
			if (tag != null) {
				tagDeclarations.append(TagDeclarationBuilder.build(tag));
			}
		}
		return tagDeclarations;
	}

	/**
	 * Construçao das declarações das tags criadas na aplicação
	 * @return String das declarações das tags criadas na aplicação
	 * @throws java.lang.ClassNotFoundException
	 */
	private String buildTagLibsContentFromModules() throws ClassNotFoundException, NotModuleException {
		StringBuilder tagDeclarations = new StringBuilder();
		tagDeclarations.append("\n   <!-- Generated Application Tags From Modules -->\n\n");

		Map<String, List<Class<Object>>> classes = getModulesDirectoryClasses(null, Tag.class);
			List<String> keyList = new ArrayList<String>(classes.keySet());
			Collections.sort(keyList);
			TagBean tag;
			ClassTagAnnotationReader reader = new ClassTagAnnotationReader();
			for (String moduleName : keyList) {
				tagDeclarations.append("\n   <!-- from module '"+moduleName+"' ...-->\n\n");
				for (Class<?> tagClass : classes.get(moduleName)) {
					tag = reader.readTag(tagClass);
					tagDeclarations.append(TagDeclarationBuilder.build(tag));
				}
			}
		return tagDeclarations.toString();
	}

	private String buildTagLibsContentFromFiles() throws ClassNotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append("\n   <!-- Generated Application Tags From Tag Files -->\n");
		File tagsDir = new File(Params.get("WEBINF_PATH") + "/" + getDirName());
		if (tagsDir.exists()) {
			List<File> tagsFiles = FileUtil.listFilesFromDirectory(tagsDir, true,".*\\.tag");
			for (File tagFile : tagsFiles) {
				String path = tagFile.getPath();
				if(path.contains("\\")){
					path = path.replaceAll("\\\\", "/");
				}
				int i = path.indexOf("/WEB-INF/tags/");
				path = path.substring(i);
				if(tagFile.isFile()){
					sb.append("\n   " +
							TagDeclarationBuilder.buildTagElement("tag-file",
									TagDeclarationBuilder.buildTagElement("name", The.firstTokenOf(tagFile.getName(), ".")) +
									TagDeclarationBuilder.buildTagElement("path", path)
							));
				}
			}
		}
		return sb.toString();
	}

}