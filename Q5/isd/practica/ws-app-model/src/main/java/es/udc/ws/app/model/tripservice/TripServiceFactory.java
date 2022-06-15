package es.udc.ws.app.model.tripservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class TripServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "TripServiceFactory.className";
    private static TripService service = null;

    private TripServiceFactory(){
    }

    @SuppressWarnings("rawtypes")
    private static TripService getInstance() {
        try{
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (TripService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized static TripService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;
    }
}
