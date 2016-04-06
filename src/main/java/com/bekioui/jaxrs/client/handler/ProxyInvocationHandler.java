package com.bekioui.jaxrs.client.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.core.Response;

public class ProxyInvocationHandler implements InvocationHandler {

    private final Object resteasyProxy;

    public ProxyInvocationHandler(Object resteasyProxy) {
        this.resteasyProxy = resteasyProxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(resteasyProxy, args);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ResponseProcessingException) {
                Throwable throwable = e.getTargetException().getCause();
                throwable.addSuppressed(handle(((ResponseProcessingException) e.getTargetException()).getResponse()));
                throw throwable;
            }
            throw e.getTargetException();
        }
    }

    private WebApplicationException handle(Response response) {
        int status = response.getStatus();

        if (status >= 300 && status < 400) {
            return new RedirectionException(response);
        }

        switch (status) {
        case 400:
            return new BadRequestException(response);
        case 401:
            return new NotAuthorizedException(response);
        case 403:
            return new ForbiddenException(response);
        case 404:
            return new NotFoundException(response);
        case 405:
            return new NotAllowedException(response);
        case 406:
            return new NotAcceptableException(response);
        case 415:
            return new NotSupportedException(response);
        case 500:
            return new InternalServerErrorException(response);
        case 503:
            return new ServiceUnavailableException(response);
        default:
            break;
        }

        if (status >= 400 && status < 500) {
            return new ClientErrorException(response);
        }

        if (status >= 500) {
            return new ServerErrorException(response);
        }

        return new WebApplicationException(response);
    }

}
