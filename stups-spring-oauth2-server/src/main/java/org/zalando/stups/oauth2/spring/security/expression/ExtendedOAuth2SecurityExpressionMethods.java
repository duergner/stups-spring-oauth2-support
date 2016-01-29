package org.zalando.stups.oauth2.spring.security.expression;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods;

/**
 * 
 * @author jbellmann
 *
 */
public class ExtendedOAuth2SecurityExpressionMethods extends OAuth2SecurityExpressionMethods {

    private Set<String> missingRealms = new LinkedHashSet<String>();

    private final Authentication authentication;

    @Override
    public boolean throwOnError(boolean decision) {
        if (!decision && !missingRealms.isEmpty()) {
            Throwable failure = new InsufficientScopeException("Insufficient realms for this resource", missingRealms);
            throw new AccessDeniedException(failure.getMessage(), failure);
        }
        // do not forget to call super
        return super.throwOnError(decision);
    }

    public ExtendedOAuth2SecurityExpressionMethods(Authentication authentication) {
        super(authentication);
        this.authentication = authentication;
    }

    public boolean hasRealm(String realm) {
        return hasAnyRealm(realm);
    }

    public boolean hasAnyRealm(String... realms) {
        boolean result = RealmOAuth2ExpressionUtils.hasAnyRealm(authentication, realms);
        if (!result) {
            missingRealms.addAll(Arrays.asList(realms));
        }
        return result;
    }

    public boolean hasInTokenInfo(String key, String value) {
        return false;
    }

}