package au.com.ibenta.pagination;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolverSupport;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.*;

@Configuration
public class WebFluxPageableResolverSupport extends HandlerMethodArgumentResolverSupport {

    private final PageableHandlerMethodArgumentResolver pageableResolver;

    public WebFluxPageableResolverSupport(final PageableHandlerMethodArgumentResolver pageableResolver) {
        super(ReactiveAdapterRegistry.getSharedInstance());
        this.pageableResolver = pageableResolver;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return pageableResolver.supportsParameter(parameter);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter,
                                        BindingContext bindingContext,
                                        ServerWebExchange exchange) {
        return Mono.just(pageableResolver.resolveArgument(
                parameter,
                null,
                new PageableResolverNativeWebRequest(exchange),
                null));
    }

    private static class PageableResolverNativeWebRequest implements NativeWebRequest {

        private final ServerHttpRequest request;

        private final ServerHttpResponse response;

        PageableResolverNativeWebRequest(final ServerWebExchange exchange) {
            this.request = exchange.getRequest();
            this.response = exchange.getResponse();
        }

        @Override
        public Object getNativeRequest() {
            return request;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getNativeRequest(@NonNull Class<T> requiredType) {
            if (requiredType.isAssignableFrom(ServerHttpRequest.class)) {
                return (T) request;
            }
            return null;
        }

        @Override
        public Object getNativeResponse() {
            return response;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getNativeResponse(@NonNull final Class<T> requiredType) {
            if (requiredType.isAssignableFrom(ServerHttpResponse.class)) {
                return (T) response;
            }
            return null;
        }

        @Override
        public String getHeader(final String headerName) {
            return Optional.ofNullable(request.getHeaders())
                    .map(headers -> headers.getFirst(headerName))
                    .orElse(null);
        }

        @Override
        public String[] getHeaderValues(String headerName) {
            List<String> values = Optional.ofNullable(request)
                    .map(HttpMessage::getHeaders)
                    .map(headers -> headers.get(headerName))
                    .orElse(Collections.emptyList());
            return values.toArray(new String[values.size()]);
        }

        @Override
        public Iterator<String> getHeaderNames() {
            return request.getHeaders().keySet().iterator();
        }

        @Override
        public String getParameter(String paramName) {
            return request.getQueryParams().getFirst(paramName);
        }

        @Override
        public String[] getParameterValues(String paramName) {
            List<String> values = request.getQueryParams().get(paramName);
            if (values == null) {
                return null;
            }
            return values.toArray(new String[values.size()]);
        }

        @Override
        public Iterator<String> getParameterNames() {
            return request.getQueryParams().keySet().iterator();
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = new HashMap<>();
            request.getQueryParams().keySet().forEach(name -> parameterMap.put(name, getParameterValues(name)));
            return parameterMap;
        }

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }

        @Override
        public String getContextPath() {
            return request.getPath().contextPath().value();
        }

        @Override
        public String getRemoteUser() {
            return null;
        }

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public boolean isUserInRole(String role) {
            return false;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public boolean checkNotModified(long lastModifiedTimestamp) {
            return false;
        }

        @Override
        public boolean checkNotModified(String etag) {
            return false;
        }

        @Override
        public boolean checkNotModified(String etag, long lastModifiedTimestamp) {
            return false;
        }

        @Override
        public String getDescription(boolean includeClientInfo) {
            return "";
        }

        @Override
        public Object getAttribute(String name, int scope) {
            return null;
        }

        @Override
        public void setAttribute(String name, Object value, int scope) {

        }

        @Override
        public void removeAttribute(String name, int scope) {

        }

        @Override
        public String[] getAttributeNames(int scope) {
            return new String[0];
        }

        @Override
        public void registerDestructionCallback(String name, Runnable callback, int scope) {

        }

        @Override
        public Object resolveReference(String key) {
            return null;
        }

        @Override
        public String getSessionId() {
            return request.getId();
        }

        @Override
        public Object getSessionMutex() {
            return request.getId();
        }
    }
}
