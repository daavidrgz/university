package es.udc.ws.app.model.booking;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlBookingDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "SqlBookingDaoFactory.className";
    private static SqlBookingDao dao = null;

    private SqlBookingDaoFactory(){}

    @SuppressWarnings("rawtypes")
    private static SqlBookingDao getInstance(){
        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlBookingDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public synchronized static SqlBookingDao getDao(){
        if(dao == null)
            dao = getInstance();

        return dao;
    }
}
