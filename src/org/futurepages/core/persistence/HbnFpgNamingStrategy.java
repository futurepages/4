package org.futurepages.core.persistence;

import org.futurepages.core.config.Apps;
import org.futurepages.util.ModuleUtil;
import org.futurepages.util.The;
import org.hibernate.AssertionFailure;
import org.hibernate.cfg.DefaultNamingStrategy;
import org.hibernate.internal.util.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HbnFpgNamingStrategy extends DefaultNamingStrategy {

	private HashMap<String, ArrayList<String>> mapOfClasses = new HashMap();

	public void putClass(Class clss){
		if(mapOfClasses.get(clss.getSimpleName().toLowerCase())==null){
			mapOfClasses.put(clss.getSimpleName().toLowerCase(),new ArrayList());
		}
		mapOfClasses.get(clss.getSimpleName().toLowerCase()).add(ModuleUtil.moduleId(clss));
	}

	@Override
	public String classToTableName(String tableName) {
        tableName = StringHelper.unqualifyEntityName(tableName);
		List<String> modules = mapOfClasses.get(tableName.toLowerCase());

		tableName = The.concat((ModuleUtil.isAppId(modules.get(0))?modules.get(0).substring(Apps.APPS_PACK.length()):modules.get(0)),"_",tableName.toLowerCase());
		modules.remove(0);
//		System.out.println("TABLE_NAME: " + tableName);
		return tableName;
	}


	@Override
    public String propertyToColumnName(String propertyName) {
        return StringHelper.unqualify(propertyName);
    }

	@Override
    public String tableName(String tableName) {
        return tableName;
    }
	@Override
    public String columnName(String columnName) {
        return columnName;
    }

	@Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
        String result =  (ModuleUtil.moduleId(ownerEntity)+"_"+StringHelper.unqualify(ownerEntity)+"_"+StringHelper.unqualify(propertyName)).toLowerCase();
//		System.out.println("COLLECTION_TABLE_NAME: "+result);
		return result;
    }

	@Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        String result =  this.columnName(joinedColumn);
//		System.out.println("JOIN_KEY_COLUMN_NAME: "+result);
		return result;
    }

	@Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        String header = propertyName != null?StringHelper.unqualify(propertyName):propertyTableName;
        if(header == null) {
            throw new AssertionFailure("NammingStrategy not properly filled");
        } else {
            String result = The.uncapitalizedWord(this.columnName(header) + "_" + referencedColumnName);
//	        System.out.println("FOREIGN KEY COLUMN NAME: "+result);
	        return result;
        }
    }

	@Override
    public String logicalColumnName(String columnName, String propertyName) {
        String result = StringHelper.isNotEmpty(columnName)?columnName:StringHelper.unqualify(propertyName);
//        System.out.println("LOGICAL COLUMN NAME: "+result);
		return result;
    }

	@Override
    public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
		String result = (tableName != null?tableName:ownerEntityTable + "_" + (associatedEntityTable != null?associatedEntityTable:StringHelper.unqualify(propertyName))).toLowerCase();
//        System.out.println("LOGICAL COLLECTION TABLE NAME: "+result);
		return result;
    }

	@Override
    public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
        String result = The.uncapitalizedWord(StringHelper.isNotEmpty(columnName) ? columnName : propertyName + "_" + The.uncapitalizedWord(referencedColumn));
//		System.out.println("LOGICAL COLLECTION COLUMN NAME: "+result);
		return (result);
    }
}
