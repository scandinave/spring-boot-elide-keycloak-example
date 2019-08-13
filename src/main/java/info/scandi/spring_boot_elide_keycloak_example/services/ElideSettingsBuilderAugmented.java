package info.scandi.spring_boot_elide_keycloak_example.services;

import java.util.HashMap;
import java.util.Map;

import com.yahoo.elide.ElideSettings;
import com.yahoo.elide.ElideSettingsBuilder;
import com.yahoo.elide.core.DataStore;

public class ElideSettingsBuilderAugmented extends ElideSettingsBuilder {

    private Map<String, Object> params = new HashMap<String, Object>();

    public ElideSettingsBuilderAugmented(DataStore dataStore) {
        super(dataStore);
    }

    public ElideSettingsBuilder withParams(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    public ElideSettingsAugmented build() {
        final ElideSettings settings = super.build();
        return new ElideSettingsAugmented(settings.getAuditLogger(), settings.getDataStore(), settings.getDictionary(),
                settings.getMapper(), settings.getPermissionExecutor(), settings.getJoinFilterDialects(),
                settings.getSubqueryFilterDialects(), settings.getDefaultMaxPageSize(),
                settings.getDefaultMaxPageSize(), settings.isUseFilterExpressions(), settings.getUpdateStatusCode(),
                settings.isReturnErrorObjects(), settings.getSerdes(), params);
    }
}