package es.udc.ws.app.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

import java.lang.reflect.InvocationTargetException;

public class ClientTripServiceFactory {

    private final static String CLASS_NAME_PARAMETER =
            "ClientTripServiceFactory.className";
    private static Class<ClientTripService> serviceClass=null;

    private ClientTripServiceFactory(){

    }
    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientTripService> getServiceClass(){
        if (serviceClass ==null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientTripService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;
    }
    public static ClientTripService getService(){
        try{
            return (ClientTripService) getServiceClass().getDeclaredConstructor().newInstance();
        }catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e){
            throw new RuntimeException(e);
        }
    }
}
