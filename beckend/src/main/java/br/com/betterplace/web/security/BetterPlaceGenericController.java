package br.com.betterplace.web.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.betterplace.core.service.ServiceException;
import br.com.betterplace.web.adapter.Adapters;

public abstract class BetterPlaceGenericController extends CasPrivateController {

    protected static final String MAV_ATTR_COUNT = "count";

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<String> handleServiceException(final HttpServletResponse response, ServiceException e) throws IOException {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    protected <T, R> ResponseEntity<R[]> getArrayResponseEntity(List<T> modelList, Class<R> output) {
        if (modelList == null || modelList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<R[]>((R[]) Adapters.adaptArray(modelList.toArray(), output), HttpStatus.OK);
    }

    protected <T, R> ResponseEntity<List<R>> getListResponseEntity(List<T> modelList, Class<R> output) {
        if (modelList == null || modelList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<R>>((List<R>) Adapters.adaptList(modelList, output), HttpStatus.OK);
    }

    protected <T, R> ResponseEntity<R> getResponseEntity(T model, Class<R> output) {
        if (model == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<R>((R) Adapters.adapt(model, output), HttpStatus.OK);
    }

    protected <T, R> ResponseEntity<R> getFirstElementResponseEntity(List<T> modelList, Class<R> output) {
        if (modelList == null || modelList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<R>((R) Adapters.adapt(modelList.get(0), output), HttpStatus.OK);
    }
}