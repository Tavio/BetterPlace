package br.com.betterplace.web;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.apache.log4j.Logger;
import org.springframework.jmx.support.JmxUtils;

/**
 * This classe uses the simple JMX interface provided by 
 * Tomcat to check if the URIEncoding parameter is properly set
 * in its HTTP connectors.
 * If it is not, the code will attempt to change it through JMX too. 
 */
public class ServletContainerSettingsEnsurance {

    private final static Logger LOGGER = Logger.getLogger(ServletContainerSettingsEnsurance.class);

    public void safellyVerifyServlet() {
        MBeanServer mBeanServer = JmxUtils.locateMBeanServer();
        HTTPConnectorCheck httpConnectorCheck = new HTTPConnectorCheck();
        httpConnectorCheck.peform(mBeanServer);
    }

    private static class HTTPConnectorCheck implements ManagedBeanCheck {

    private static final String HTTP_CONNECTOR_DEFAULT_PORT = "8080";
    private static final String URI_ENCODING_MBEAN_ATTRIBUTE = "URIEncoding";
    private static final String EXPTECTED_URI_ENCODING_VALUE = "UTF-8";

    @Override
    public void peform(MBeanServer mBeanServer) {
        ObjectName connectorObjectName;
        try {
        connectorObjectName = new ObjectName("Catalina:type=Connector,port="
            + HTTP_CONNECTOR_DEFAULT_PORT);
        this.perform(mBeanServer, connectorObjectName);
        } catch (MalformedObjectNameException e) {
        // Swallowed on purpose
        }
    }

    private void perform(MBeanServer mBeanServer, ObjectName connectorObjectName) {
        try {
            Object attributeValue = mBeanServer.getAttribute(connectorObjectName, URI_ENCODING_MBEAN_ATTRIBUTE);
            String stringValue = safeToString(attributeValue);
            if (stringValue == null || !stringValue.equals(EXPTECTED_URI_ENCODING_VALUE)) {
                LOGGER.warn("Value of MBean Attribute '" + URI_ENCODING_MBEAN_ATTRIBUTE + "' is " + (stringValue == null ? "unset" : stringValue));
                this.changeAttributeValue(mBeanServer, connectorObjectName);
            } else {
                LOGGER.info("Checked: Value of MBean Attribute '" + URI_ENCODING_MBEAN_ATTRIBUTE + "' is " + stringValue + " in " + connectorObjectName.toString());
            }
        } catch (AttributeNotFoundException | InstanceNotFoundException | MBeanException | ReflectionException e) {
            LOGGER.warn("Could not verify MBean Attribute '" + URI_ENCODING_MBEAN_ATTRIBUTE + "' in " + connectorObjectName.toString());
        }
    }
    
        private void changeAttributeValue(MBeanServer mBeanServer, ObjectName connectorObjectName) {
            Attribute attribute = new Attribute(URI_ENCODING_MBEAN_ATTRIBUTE, EXPTECTED_URI_ENCODING_VALUE);
            try {
                mBeanServer.setAttribute(connectorObjectName, attribute);
                LOGGER.warn("Changed value of of MBean Attribute '"
                        + attribute.getName() + "' to '" + attribute.getValue()
                        + "' in " + connectorObjectName.toString());
            } catch (InstanceNotFoundException | InvalidAttributeValueException | AttributeNotFoundException | ReflectionException | MBeanException e) {
                final String logMessage = "Failed to change value of MBean Attribute '"
                        + attribute.getName() + "' to '" + attribute.getValue()
                        + "' in " + connectorObjectName.toString();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(logMessage, e);
                } else {
                    LOGGER.warn(logMessage);
                }
            }
        }
    }

    private static interface ManagedBeanCheck {
        void peform(MBeanServer mBeanServer);
    }

    private static String safeToString(Object object) {
        if (object != null) {
            if (object.getClass().isAssignableFrom(String.class)) {
                return (String) object;
            }
        }
        return null;
    }
}