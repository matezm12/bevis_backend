package com.bevis.versioning;

import com.bevis.versioning.exception.VersionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Slf4j
@Service
public class VersionHandlerInterceptor extends HandlerInterceptorAdapter {

    @Value("${api.rest.min-supported-version}")
    private String minSupportedApiVersion;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requiredVersion = request.getHeader(VersionizingConstants.VERSION_HEADER);
        if (Objects.nonNull(requiredVersion) && minSupportedApiVersion.compareTo(requiredVersion) > 0) {
            log.error("Minimum supported rest api version is: {}", minSupportedApiVersion);
            throw new VersionException("Minimum supported rest api version is " + minSupportedApiVersion);
        }
        return super.preHandle(request, response, handler);
    }
}
