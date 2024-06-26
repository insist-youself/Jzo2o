package com.jzo2o.knife4j.filter;

import com.jzo2o.common.utils.IoUtils;
import com.jzo2o.knife4j.response.SwaggerTransformServletResponse;
import com.jzo2o.knife4j.utils.ResponseWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SwaggerFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (((HttpServletRequest) servletRequest).getRequestURI().contains("/v2/api-docs")) {

            ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) servletResponse);
            filterChain.doFilter(servletRequest, responseWrapper);
            responseWrapper.getResponseData();
            byte[] responseData = SwaggerTransformServletResponse.getBody(responseWrapper.getResponseData());
            IoUtils.write(servletResponse.getOutputStream(), false, responseData);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
