package br.com.betterplace.web.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public abstract class CasPrivateController {

    private static final Logger LOG = Logger.getLogger(CasPrivateController.class);

    protected HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

//    @ExceptionHandler(CasPermissionDeniedException.class)
//    public void handleCasPermissionDeniedException(final HttpServletResponse response) throws IOException {
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
//    }
    
//    @ExceptionHandler(CasInvalidTicketException.class)
//    public ModelAndView handleCasInvalidTicketException(final HttpServletResponse response) throws IOException {
//        ModelAndView out = new ModelAndView();
//        out.addObject("response", "Ticket inválido.");
//        return out;
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExeption(final HttpServletResponse response, Exception e) throws IOException {
        LOG.error(null, e);
        return new ResponseEntity<String>("não foi possível completar sua operação, por favor contacte um administrador do sistema.", HttpStatus.BAD_REQUEST);
    }
}