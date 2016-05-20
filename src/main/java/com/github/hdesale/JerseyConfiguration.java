package com.github.hdesale;

import com.github.hdesale.api.ShopResourceImplV1;
import com.github.hdesale.domain.location.LocationNotFoundException;
import com.google.common.util.concurrent.UncheckedExecutionException;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Jersey configuration.
 *
 * @author Hemant
 */
@Configuration
public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        register(JacksonFeature.class);
        register(LoggingFilter.class);
        register(ShopResourceImplV1.class);
        register(AppExceptionMapper.class);
    }

    @Provider
    private static class AppExceptionMapper implements ExceptionMapper<Throwable> {

        private static final Logger log = LoggerFactory.getLogger(AppExceptionMapper.class);

        @Override
        public Response toResponse(Throwable ex) {
            String error = ex.getMessage();
            log.error(error);

            Response.ResponseBuilder rb;
            if ((ex instanceof UncheckedExecutionException &&
                    ex.getCause() instanceof LocationNotFoundException) ||
                    ex instanceof IllegalArgumentException ||
                    ex instanceof LocationNotFoundException) {
                rb = Response.status(Response.Status.BAD_REQUEST);
            } else {
                rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
            }
            return rb.type(MediaType.TEXT_PLAIN).entity(error).build();
        }
    }
}
