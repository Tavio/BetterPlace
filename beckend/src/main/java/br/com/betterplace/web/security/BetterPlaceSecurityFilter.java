package br.com.betterplace.web.security;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

public class BetterPlaceSecurityFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(BetterPlaceSecurityFilter.class);

    public static final String CAS_AUTH_MAP_ATTRIBUTE = "cas.auth.map";

//    protected CasApi casApi;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext spring = (ApplicationContext) filterConfig
                .getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
//        this.casApi = spring.getBean(CasApi.class);
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) _request;
        HttpServletResponse response = (HttpServletResponse) _response;
        Pattern p = Pattern.compile("\\/private\\/{1,1}(.[^\\/]*)\\/");
        Matcher m = p.matcher(request.getRequestURL());
        if (!m.find()) {
            chain.doFilter(request, response);
        } else {
//            String ticket = m.group(1);
//            AuthMap authMap = null;
//            AuthInfo authInfo = null;
//            try {
//                authInfo = this.casApi.validateTicket(ticket);
//                authMap = authInfo.getAuthMap();
//                if (authMap != null && authMap.getUser() != null) {
//                    request.setAttribute(CAS_AUTH_MAP_ATTRIBUTE, authMap);
//                    chain.doFilter(request, response);
//                    LOGGER.debug("ticket: " + ticket + " - authentication success");
//                } else if (authInfo.getAuthFail() != null && authInfo.getAuthFail().getCode() == 1) {
//                    LOGGER.debug(authInfo.getAuthFail().getMessage());
//                    throw new CasApiException(authInfo.getAuthFail().getMessage());
//                } else {
//                    throw new Exception(authInfo.getAuthFail().getMessage());
//                }
//            } catch(CasApiException ex) {
//                response.sendError(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
//                LOGGER.debug("ticket: " + ticket + " - authentication fail - send error: " + ex.getMessage());
//            } catch (Exception e) {
//                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, e.getMessage());
//                LOGGER.debug("ticket: " + ticket + " - authentication fail - send error: " + e.getMessage());
//            }
        }

    }

    @Override
    public void destroy() {
    }
}
