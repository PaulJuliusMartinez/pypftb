package com.abcanthur.website.codegenhack;

import org.jooq.tools.StringUtils;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;
import org.jooq.util.IdentityDefinition;
import org.jooq.util.SchemaDefinition;

public class DepluralizeTableNameStrategy extends DefaultGeneratorStrategy {

	public String getDepluralizedName(String name) {
		if (Character.toLowerCase(name.charAt(name.length() - 1)) == 's') {
			return name.substring(0,  name.length() - 1);
		}
		return name;
	}
	
	@Override
	public String getJavaClassName(final Definition definition, final Mode mode) {
        // [#2032] Intercept default catalog
        if (definition instanceof CatalogDefinition && ((CatalogDefinition) definition).isDefaultCatalog()) {
            return "DefaultCatalog";
        }

        // [#2089] Intercept default schema
        else if (definition instanceof SchemaDefinition && ((SchemaDefinition) definition).isDefaultSchema()) {
            return "DefaultSchema";
        }

		StringBuilder result = new StringBuilder();

        // [#4562] Some characters should be treated like underscore
		String rootName = StringUtils.toCamelCase(
				getDepluralizedName(definition.getOutputName())
                .replace(' ', '_')
                .replace('-', '_')
                .replace('.', '_')
		);
		
        result.append(rootName);

        if (mode == Mode.RECORD) {
            result.append("Record");
        }
        else if (mode == Mode.DAO) {
            result.append("Dao");
        }
        else if (mode == Mode.INTERFACE) {
            result.insert(0, "I");
        }

        return result.toString();
	}
	
}
