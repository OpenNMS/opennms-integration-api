/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.xml.schema.poller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Service to be polled for addresses in this
 *  package.
 */

@XmlRootElement(name="service")
@XmlAccessorType(XmlAccessType.NONE)
public class ServiceXml implements Serializable {

    /**
     * Service name
     */
    @XmlAttribute(name="name")
    private String m_name;

    /**
     * Interval at which the service is to be polled
     */
    @XmlAttribute(name="interval")
    private Long m_interval;

    /**
     * Specifies if the service is user defined. Used specifically for UI
     * purposes.
     */
    @XmlAttribute(name="user-defined")
    private String m_userDefined = "false";

    /**
     * Status of the service. The service is polled only if this is set to
     * 'on'.
     */
    @XmlAttribute(name="status")
    private String m_status = "on";

    @XmlElement(name="pattern")
    private String m_pattern = null;

    /**
     * Parameters to be used for polling this service. E.g.: for polling HTTP,
     * the URL to hit is configurable via a parameter. Parameters are specific
     * to the service monitor.
     */
    @XmlElement(name="parameter")
    private List<ParameterXml> m_parameters = new ArrayList<>();

    /**
     * Service name
     */
    public String getName() {
        return m_name;
    }

    public void setName(final String name) {
        m_name = name;
    }

    /**
     * Interval at which the service is to be polled
     */
    public Long getInterval() {
        return m_interval == null? 0 : m_interval;
    }

    public void setInterval(final Long interval) {
        m_interval = interval;
    }

    /**
     * Specifies if the service is user defined. Used specifically for UI
     * purposes.
     */
    public String getUserDefined() {
        return m_userDefined == null? "false" : m_userDefined;
    }

    public void setUserDefined(final String userDefined) {
        m_userDefined = userDefined;
    }

    /**
     * Status of the service. The service is polled only if this is set to
     * 'on'.
     */
    public String getStatus() {
        return m_status == null? "on" : m_status;
    }

    public void setStatus(final String status) {
        m_status = status;
    }

    public String getPattern() {
        return m_pattern;
    }

    public void setPattern(final String pattern) {
        m_pattern = pattern;
    }

    public List<ParameterXml> getParameters() {
        if (m_parameters == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_parameters);
        }
    }

    public void setParameters(final List<ParameterXml> parameters) {
        m_parameters = new ArrayList<>(parameters);
    }

    @Override()
    public boolean equals(final Object obj) {
        if ( this == obj ) return true;
        
        if (obj instanceof ServiceXml) {
            final ServiceXml temp = (ServiceXml)obj;
            if (m_name != null) {
                if (temp.m_name == null) {
                    return false;
                } else if (!(m_name.equals(temp.m_name))) {
                    return false;
                }
            } else if (temp.m_name != null) {
                return false;
            }
            if (m_interval != null) {
                if (temp.m_interval == null) {
                    return false;
                } else if (!(m_interval.equals(temp.m_interval))) {
                    return false;
                }
            } else if (temp.m_interval != null) {
                return false;
            }
            if (m_userDefined != null) {
                if (temp.m_userDefined == null) {
                    return false;
                } else if (!(m_userDefined.equals(temp.m_userDefined))) {
                    return false;
                }
            } else if (temp.m_userDefined != null) {
                return false;
            }
            if (m_status != null) {
                if (temp.m_status == null) {
                    return false;
                } else if (!(m_status.equals(temp.m_status))) {
                    return false;
                }
            } else if (temp.m_status != null) {
                return false;
            }
            if (m_parameters != null) {
                if (temp.m_parameters == null) {
                    return false;
                } else if (!(m_parameters.equals(temp.m_parameters))) {
                    return false;
                }
            } else if (temp.m_parameters != null) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Overrides the Object.hashCode method.
     * <p>
     * The following steps came from <b>Effective Java Programming
     * Language Guide</b> by Joshua Bloch, Chapter 3
     * 
     * @return a hash code value for the object.
     */
    public int hashCode(
    ) {
        int result = 17;
        
        if (m_name != null) {
           result = 37 * result + m_name.hashCode();
        }
        if (m_interval != null) {
            result = 37 * result + m_interval.hashCode();
        }
        if (m_userDefined != null) {
           result = 37 * result + m_userDefined.hashCode();
        }
        if (m_status != null) {
           result = 37 * result + m_status.hashCode();
        }
        if (m_parameters != null) {
           result = 37 * result + m_parameters.hashCode();
        }
        
        return result;
    }

    @Override
    public String toString() {
        return "Service[name=" + m_name +
                ",interval=" + m_interval +
                ",userDefined=" + m_userDefined +
                ",status=" + m_status +
                ",parameters=" + m_parameters +
                "]";
    }
}
