/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.fabiose.book.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
 
import org.hibernate.HibernateException;
import org.hibernate.engine.config.spi.ConfigurationService;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceRegistryAwareService;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.jboss.logging.Logger;

/**
 *
 * @author fabioestrela
 */
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider, ServiceRegistryAwareService {

    private final DriverManagerConnectionProviderImpl provider = new DriverManagerConnectionProviderImpl();

    private static final Logger log = Logger.getLogger(MultiTenantConnectionProviderImpl.class.getName());

    @Override
    public boolean isUnwrappableAs(Class arg0) {
        return provider.isUnwrappableAs(arg0);
    }

    @Override
    public <T> T unwrap(Class<T> arg0) {
        return provider.unwrap(arg0);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return provider.getConnection();
    }

    @Override
   public Connection getConnection(String tenantId) throws SQLException {
        Connection con = getAnyConnection();
        try {
          con.createStatement().execute("SET SCHEMA '" + tenantId + "'");
            log.info("Using " + tenantId + " as database schema");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new HibernateException("Could not alter connection for specific schema");
        }
        return con;
    }

    @Override
    public void releaseAnyConnection(Connection con) throws SQLException {
        provider.closeConnection(con);

    }

   public void releaseConnection(String tenantId, Connection con)
            throws SQLException {
        try {
            con.createStatement().execute("SET SCHEMA 'public'");
            System.out.println("Now, released " + tenantId);
        } catch (SQLException ex) {
            throw new HibernateException("Unable to reset");
        }
        provider.closeConnection(con);

    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public void injectServices(ServiceRegistryImplementor registry) {
        Map settings = registry.getService(ConfigurationService.class).getSettings();
        provider.configure(settings);
        provider.injectServices(registry);
    }

}
