package com.ccbravo.pocs.configuration;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import org.springdoc.core.SpringDocProviders;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.customizers.RouterOperationCustomizer;
import org.springdoc.core.filters.OpenApiMethodFilter;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class DetailGroupOpenApiResource extends OpenApiResource {


    public DetailGroupOpenApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
            GenericResponseService responseBuilder, OperationService operationParser,
            Optional<List<OperationCustomizer>> operationCustomizers,
            Optional<List<OpenApiCustomiser>> openApiCustomisers,
            Optional<List<RouterOperationCustomizer>> routerOperationCustomizers,
            Optional<List<OpenApiMethodFilter>> methodFilters,
            SpringDocConfigProperties springDocConfigProperties,
            SpringDocProviders springDocProviders) {
        super("detail", openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, routerOperationCustomizers, methodFilters, springDocConfigProperties, springDocProviders);
    }


    @Override
    protected String getServerUrl(HttpServletRequest request, String apiDocsUrl) {
        return "";
    }

    public String getOpenApiJson() throws JsonProcessingException {
        return writeJsonValue(getOpenApi(Locale.getDefault()));
    }
}
