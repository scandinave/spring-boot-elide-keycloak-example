package com.example.demo.services;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Optional;

import com.yahoo.elide.ElideSettings;
import com.yahoo.elide.core.RequestScope;
import com.yahoo.elide.core.filter.expression.FilterExpression;
import com.yahoo.elide.security.ChangeSpec;
import com.yahoo.elide.security.PermissionExecutor;
import com.yahoo.elide.security.PersistentResource;
import com.yahoo.elide.security.permissions.ExpressionResult;
import com.yahoo.elide.security.permissions.ExpressionResultCache;
import com.yahoo.elide.security.permissions.PermissionExpressionBuilder;

import org.keycloak.KeycloakSecurityContext;

public class KeycloakPermissionExecutor implements PermissionExecutor {

    private RequestScope requestScope;
    private PermissionExpressionBuilder expressionBuilder;
    private HashMap userPermissionCheckCache;
    private HashMap checkStats;
    private boolean verbose;

    /**
     * Constructor.
     *
     * @param requestScope Request scope
     */
    public KeycloakPermissionExecutor(final RequestScope requestScope) {
        this(false, requestScope);
    }

    /**
     * Constructor.
     *
     * @param verbose      True if executor should produce verbose output to caller
     * @param requestScope Request scope
     */
    public KeycloakPermissionExecutor(boolean verbose, final RequestScope requestScope) {
        ExpressionResultCache cache = new ExpressionResultCache();

        this.requestScope = requestScope;
        this.expressionBuilder = new PermissionExpressionBuilder(cache, requestScope.getDictionary());
        this.userPermissionCheckCache = new HashMap<>();
        this.checkStats = new HashMap<>();
        this.verbose = verbose;
    }

    @Override
    public <A extends Annotation> ExpressionResult checkPermission(Class<A> annotationClass,
            PersistentResource resource) {

        String accessToken;
        try {
            accessToken = this.getKeycloakContext().getTokenString();
            String rpt = OidcUtils.requestRPT(accessToken);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <A extends Annotation> ExpressionResult checkPermission(Class<A> annotationClass,
            PersistentResource resource, ChangeSpec changeSpec) {
        return null;
    }

    @Override
    public <A extends Annotation> ExpressionResult checkSpecificFieldPermissions(PersistentResource<?> resource,
            ChangeSpec changeSpec, Class<A> annotationClass, String field) {
        return null;
    }

    @Override
    public <A extends Annotation> ExpressionResult checkSpecificFieldPermissionsDeferred(PersistentResource<?> resource,
            ChangeSpec changeSpec, Class<A> annotationClass, String field) {
        return null;
    }

    @Override
    public <A extends Annotation> ExpressionResult checkUserPermissions(Class<?> resourceClass,
            Class<A> annotationClass) {
        return null;
    }

    @Override
    public Optional<FilterExpression> getReadPermissionFilter(Class<?> resourceClass) {
        return null;
    }

    @Override
    public void executeCommitChecks() {

    }

    private KeycloakSecurityContext getKeycloakContext() throws Exception {
        ElideSettings settings = this.requestScope.getElideSettings();
        if (settings instanceof ElideSettingsAugmented) {
            return (KeycloakSecurityContext) ((ElideSettingsAugmented) settings).getParams().get("keycloak");
        } else {
            throw new Exception("KeycloakPermisionExecutor require ElideSettingsAugmented as Settings class");
        }
    }

}