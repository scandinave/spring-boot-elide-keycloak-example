package info.scandi.spring_boot_elide_keycloak_example.controllers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import info.scandi.spring_boot_elide_keycloak_example.check.IsOwner;
import info.scandi.spring_boot_elide_keycloak_example.services.ElideSettingsBuilderAugmented;
import info.scandi.spring_boot_elide_keycloak_example.services.KeycloakPermissionExecutor;
import info.scandi.spring_boot_elide_keycloak_example.services.OidcUtils;
import com.yahoo.elide.Elide;
import com.yahoo.elide.ElideSettings;
import com.yahoo.elide.ElideSettingsBuilder;
import com.yahoo.elide.core.DataStore;
import com.yahoo.elide.datastores.jpa.JpaDataStore;
import com.yahoo.elide.datastores.jpa.transaction.NonJtaTransaction;
import com.yahoo.elide.security.checks.Check;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestController
public class ElideController {

    private static final String JSON_VND_API = "application/vnd.api+json";

    @Autowired
    private EntityManagerFactory emf;

    /**
     * Converts a plain map to a multivalued map
     * 
     * @param input The original map
     * @return A MultivaluedMap constructed from the input
     */
    private MultivaluedMap<String, String> fromMap(final Map<String, String> input) {
        return new MultivaluedHashMap<String, String>(input);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET, produces = ElideController.JSON_VND_API, consumes = ElideController.JSON_VND_API, value = {
            "/{entity}", "/{entity}/{id}/relationships/{entity2}", "/{entity}/{id}/{child}", "/{entity}/{id}" })
    @Transactional
    public String jsonApiGet(@RequestParam final Map<String, String> allRequestParams, final HttpServletRequest request,
            final Principal principal) {
        /*
         * Here we pass through the data Spring has provided for us in the parameters,
         * then making use of Java 8 Lambdas to do something useful.
         */
        return elideRunner(request, (elide, path) -> elide.get(path, fromMap(allRequestParams), principal).getBody());

    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.POST, produces = ElideController.JSON_VND_API, consumes = ElideController.JSON_VND_API, value = {
            "/{entity}", "/{entity}/{id}/relationships/{entity2}" })
    @Transactional
    public String jsonApiPost(@RequestBody final String body, final HttpServletRequest request,
            final Principal principal) {
        /*
         * There is not much extra work to do here over what we have already put in
         * place for the get request. Our callback changes slightly, but we are still
         * just passing objects from Spring to Elide.
         */
        System.out.println(body);
        return elideRunner(request, (elide, path) -> elide.post(path, body, principal).getBody());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.DELETE, produces = ElideController.JSON_VND_API, consumes = ElideController.JSON_VND_API, value = {
            "/{entity}/{id}", "/{entity}/{id}/relationships/{entity2}" })
    @Transactional
    public String jsonApiDelete(@RequestBody final String body, final HttpServletRequest request,
            final Principal principal) {
        return elideRunner(request, (elide, path) -> elide.delete(path, body, principal).getBody());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.PATCH, produces = ElideController.JSON_VND_API, consumes = ElideController.JSON_VND_API, value = {
            "/{entity}/{id}", "/{entity}/{id}/relationships/{entity2}" })
    @Transactional
    public String jsonApiPatch(@RequestBody final String body, final HttpServletRequest request,
            final Principal principal) {
        /*
         * Note that the patch operation here is the standard update, not the JSON Patch
         * extension (http://jsonapi.org/extensions/jsonpatch/)
         */
        return elideRunner(request, (elide, path) -> elide
                .patch("application/vnd.api+json", "application/vnd.api+json", path, body, principal).getBody());
    }

    private String elideRunner(final HttpServletRequest request, final ElideCallable elideCallable) {
        /*
         * This gives us the full path that was used to call this endpoint.
         */
        final String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        final KeycloakSecurityContext keycloakSecurityContext = (KeycloakSecurityContext) request
                .getAttribute(KeycloakSecurityContext.class.getName());

        DataStore dataStore = new JpaDataStore(() -> emf.createEntityManager(), (em) -> new NonJtaTransaction(em));
        // EntityDictionary dictionary = new EntityDictionary(checks);
        ElideSettings settings = new ElideSettingsBuilderAugmented(dataStore)
                .withParams("keycloak", keycloakSecurityContext)
                .withPermissionExecutor(KeycloakPermissionExecutor.class).build();
        Elide elide = new Elide(settings);

        final String fixedPath = restOfTheUrl.replaceAll("^/", "");

        /*
         * Now that the boilerplate initialisation is done, we let the caller do
         * something useful
         */
        return elideCallable.call(elide, fixedPath);

    }
}