package org.futurepages.menta.core.tags.build;

/**
 *
 * @author Danilo
 */
public enum ContentTypeEnum {

    JSP("JSP"),
    EMPTY("empty"),
    SCRIPTLESS("scriptless");
    String descricao;

    private ContentTypeEnum(String desc) {
        this.descricao = desc;
    }
}
