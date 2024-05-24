package scotiaFid.handler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scotiaFid.util.LogsContext;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
    private static final Logger logger = LogManager.getLogger(CustomExceptionHandler.class);
    private final ExceptionHandler wrapped;

    CustomExceptionHandler(ExceptionHandler exception) {
        this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();
        while (i.hasNext()) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context
                    = (ExceptionQueuedEventContext) event.getSource();

            // get the exception from context
            Throwable t = context.getException();

            final FacesContext fc = FacesContext.getCurrentInstance();
            final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
            final NavigationHandler nav = fc.getApplication().getNavigationHandler();

            //here you do what ever you want with exception
            try {
                LogsContext.FormatoNormativo();
                //log error ?
                //logger.error("Ha ocurrido una excepción critica: " + t);
                if (t instanceof ViewExpiredException) {
                    logger.error("La sesión/vista ha expirado y no se pudo realizar su solicitud: " + t.getMessage());
                    requestMap.put("javax.servlet.error.message", "Sesión expirada, por favor intente de nuevo...");
                    String errorPageLocation = "scotiaFid/vista/vistaExpiro.html";
                    fc.setViewRoot(fc.getApplication().getViewHandler().createView(fc, errorPageLocation));
                    fc.getPartialViewContext().setRenderAll(true);
                    fc.renderResponse();
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/scotiaFid/vista/vistaExpiro.html");
                } else {
                    //redirect error page
                    logger.info("Ha ocurrido la siguiente excepción: " + t.getMessage());
                }
            } catch (IOException | java.lang.IllegalStateException ex) {
                LogsContext.FormatoNormativo();
                logger.error("No fue posible acceder al menu principal debido a que la sesión ya habia expirado .- " + ex.getMessage());
            } finally {
                i.remove();
            }
        }
        //parent hanle
        getWrapped().handle();
    }
}