package com.example.demo.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.yahoo.elide.ElideSettings;
import com.yahoo.elide.audit.AuditLogger;
import com.yahoo.elide.core.DataStore;
import com.yahoo.elide.core.EntityDictionary;
import com.yahoo.elide.core.RequestScope;
import com.yahoo.elide.core.filter.dialect.JoinFilterDialect;
import com.yahoo.elide.core.filter.dialect.SubqueryFilterDialect;
import com.yahoo.elide.jsonapi.JsonApiMapper;
import com.yahoo.elide.security.PermissionExecutor;
import com.yahoo.elide.utils.coerce.converters.Serde;

import lombok.Getter;

public class ElideSettingsAugmented extends ElideSettings {

    @Getter
    private Map<String, Object> params;

    public ElideSettingsAugmented(AuditLogger auditLogger, DataStore dataStore, EntityDictionary dictionary,
            JsonApiMapper mapper, Function<RequestScope, PermissionExecutor> permissionExecutor,
            List<JoinFilterDialect> joinFilterDialects, List<SubqueryFilterDialect> subqueryFilterDialects,
            int defaultMaxPageSize, int defaultPageSize, boolean useFilterExpressions, int updateStatusCode,
            boolean returnErrorObjects, Map<Class, Serde> serdes, Map<String, Object> params) {
        super(auditLogger, dataStore, dictionary, mapper, permissionExecutor, joinFilterDialects,
                subqueryFilterDialects, defaultMaxPageSize, defaultPageSize, useFilterExpressions, updateStatusCode,
                returnErrorObjects, serdes);
        this.params = params;
    }

}