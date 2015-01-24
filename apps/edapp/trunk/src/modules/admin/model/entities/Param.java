package modules.admin.model.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import modules.admin.model.entities.enums.ParamEnum;
import modules.admin.model.entities.enums.ParamValueType;

@Entity
@Table(name="admin_param")
public class Param implements Serializable{
    
    @Id
    @Column(length=40,nullable=false)
    private String paramId;

    @Column(length=100)
    private String title;
    
    @Lob
    private String val;

    private int maxLength;

    @Enumerated(EnumType.STRING)
    private ParamValueType valueType;

    public Param() {
    }
	
	public Param(String paramId, ParamValueType valueType, String val, String titulo, int maxLength) {
        this.paramId = paramId;
        this.val = val;
        this.valueType = valueType;
        this.maxLength = maxLength;
        this.title = titulo;
    }

    public Param(ParamEnum paramEnum, ParamValueType valueType, String val, String titulo, int maxLength) {
        this.paramId = paramEnum.getId();
        this.val = val;
        this.valueType = valueType;
        this.maxLength = maxLength;
        this.title = titulo;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getParamId() {
        return paramId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public ParamValueType getValueType() {
        return valueType;
    }

    public void setValueType(ParamValueType valueType) {
        this.valueType = valueType;
    }

	@Override
    public String toString() {
        return val;
    }

    public Object getConvertedValue() {
        if (valueType == ParamValueType.HTML || valueType == ParamValueType.LONG_TEXT || valueType == ParamValueType.SIMPLE_TEXT) {
            return val;
        } else if (valueType == ParamValueType.INT) {
            return Integer.valueOf(val);
        } else if (valueType == ParamValueType.BOOLEAN) {
            return Boolean.valueOf(val);
        } else { //(valueType == ParamValueType.DOUBLE)
            return Double.valueOf(val);
        }
    }
}
