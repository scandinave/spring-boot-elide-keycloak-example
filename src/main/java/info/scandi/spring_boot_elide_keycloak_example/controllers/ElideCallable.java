package info.scandi.spring_boot_elide_keycloak_example.controllers;

import com.yahoo.elide.Elide;

/**
 * We'll implement this interface as a lambda to make working with Elide easier
 */
public interface ElideCallable {
    String call(final Elide elide, final String path);
}