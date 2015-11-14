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
        builder.append("    <"+ TAG + ">\n");
        builder.append("        "+buildTagElement(NAME,         tag.getName())+"\n");
        builder.append("        "+buildTagElement(TAG_CLASS,    tag.getTagClass().getName())+"\n");
        builder.append("        "+buildTagElement(BODY_CONTENT, tag.getContentType().descricao)+"\n");
        builder.append("        "+buildTagElement(DISPLAY_NAME, tag.getDisplayName())+"\n");
        for (TagAttributeBean att : tag.getSortedAttributes()) {
            builder.append(builderAttributeDeclaration(att));
        }
        builder.append("    </"+TAG+">\n");
        return builder.toString();
    }

    protected static String buildTagElement(String element, String value) {
        return "<" + element + ">" +
                value +
                "</" + element + ">";
    }

    /**
     * Monta a declaracao para <attribute>
     * @param attribute
     * @return
     */
    private static String builderAttributeDeclaration(TagAttributeBean attribute) {

        StringBuilder builder = new StringBuilder();
        builder.append("        <" + ATTRIBUTE + ">\n");
        builder.append("            " +buildTagElement(NAME, attribute.getName())+"\n");
        builder.append("            " +buildTagElement(REQUIRED, booleanToString(attribute.isRequired()))+"\n");
        builder.append("            " +buildTagElement(RTEXPRVALUE, booleanToString(attribute.isRtexprvalue()))+"\n");
        builder.append("            " +buildTagElement(TYPE, attribute.getType().getName())+"\n");
        builder.append("        </" + ATTRIBUTE + ">\n");
        return builder.toString();
    }

    private static String booleanToString(boolean bol) {
        if (bol) {
            return "true";
        }
        return "false";
    }
}