package org.futurepages.menta.core.tags.build;

/**
 *Classe para a montagem de declaração de uma Tag
 * @author Danilo
 */
public class TagDeclarationBuilder {

    private static final String TAG = "tag";
    private static final String NAME = "name";
    private static final String TAG_CLASS = "tag-class";
    private static final String BODY_CONTENT = "body-content";
    private static final String DISPLAY_NAME = "display-name";
    private static final String ATTRIBUTE = "attribute";
    private static final String REQUIRED = "required";
    private static final String RTEXPRVALUE = "rtexprvalue";
    private static final String TYPE = "type";

    /**
     * Construção de declaracao de uma tag
     * @return String : declaração da tag
     */

    public static String build(TagBean tag) {
        StringBuilder builder = new StringBuilder();
        builder.append("    <")   .append( TAG ).append(">\n");
        builder.append("        ").append(buildTagElement(NAME, tag.getName())).append("\n");
        builder.append("        ").append(buildTagElement(TAG_CLASS, tag.getTagClass().getName())).append("\n");
        builder.append("        ").append(buildTagElement(BODY_CONTENT, tag.getContentType().descricao)).append("\n");
        builder.append("        ").append(buildTagElement(DISPLAY_NAME, tag.getDisplayName())).append("\n");
        for (TagAttributeBean att : tag.getSortedAttributes()) {
            builder.append(builderAttributeDeclaration(att));
        }
        builder.append("    </").append(TAG).append(">\n");
        return builder.toString();
    }

    protected static String buildTagElement(String element, String value) {
        return "<" + element + ">" +
                value +
                "</" + element + ">";
    }

    /**
     * Monta a declaracao para <attribute>
     */
    private static String builderAttributeDeclaration(TagAttributeBean attribute) {
        return "        <" + ATTRIBUTE + ">\n" +
                "            " + buildTagElement(NAME, attribute.getName()) + "\n" +
                "            " + buildTagElement(REQUIRED, booleanToString(attribute.isRequired())) + "\n" +
                "            " + buildTagElement(RTEXPRVALUE, booleanToString(attribute.isRtexprvalue())) + "\n" +
                "            " + buildTagElement(TYPE, attribute.getType().getName()) + "\n" +
                "        </" + ATTRIBUTE + ">\n";
    }

    private static String booleanToString(boolean bol) {
        if (bol) {
            return "true";
        }
        return "false";
    }
}