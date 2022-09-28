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
 * Package encapsulating addresses, services to be polled
 *  for these addresses, etc..
 */

@XmlRootElement(name="package")
@XmlAccessorType(XmlAccessType.NONE)
public class PackageXml implements Serializable {

    /**
     * Name or identifier for this package.
     */
    @XmlAttribute(name="name")
    private String m_name;

    /**
     * A rule which addresses belonging to this package must pass. This
     * package is applied only to addresses that pass this filter.
     */
    @XmlElement(name="filter")
    private String m_filter;

    /**
     * Addresses in this package
     */
    @XmlElement(name="specific")
    private List<String> m_specifics = new ArrayList<>();

    /**
     * Range of addresses in this package.
     */
    @XmlElement(name="include-range")
    private List<AddressRangeXml> m_includeRanges = new ArrayList<>();

    /**
     * Range of addresses to be excluded from this package.
     */
    @XmlElement(name="exclude-range")
    private List<AddressRangeXml> m_excludeRanges = new ArrayList<>();

    /**
     * RRD parameters for response time data.
     */
    @XmlElement(name="rrd")
    private RrdXml m_rrd;

    /**
     * Services to be polled for addresses belonging to this package.
     */
    @XmlElement(name="service")
    private List<ServiceXml> m_services = new ArrayList<>();

    /**
     * Scheduled outages. If a service is found down during this period, it is
     * not reported as down.
     */
    @XmlElement(name="outage-calendar")
    private List<String> m_outageCalendars = new ArrayList<>();

    /**
     * Downtime model. Determines the rate at which addresses are to be polled
     * when they remain down for extended periods.
     */
    @XmlElement(name="downtime")
    private List<DowntimeXml> m_downtimes = new ArrayList<>();

    /**
     * Name or identifier for this package.
     */
    public String getName() {
        return m_name;
    }

    public void setName(final String name) {
        m_name = name;
    }

    /**
     * A rule which addresses belonging to this package must pass. This
     * package is applied only to addresses that pass this filter.
     */
    public String getFilter() {
        return m_filter;
    }

    public void setFilter(final String filter) {
        m_filter = filter;
    }

    public List<String> getSpecifics() {
        if (m_specifics == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_specifics);
        }
    }

    public void setSpecifics(final List<String> specifics) {
        m_specifics = new ArrayList<>(specifics);
    }

    public List<AddressRangeXml> getIncludeRanges() {
        if (m_includeRanges == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_includeRanges);
        }
    }

    public void setIncludeRanges(final List<AddressRangeXml> includeRanges) {
        m_includeRanges = new ArrayList<>(includeRanges);
    }

    public List<AddressRangeXml> getExcludeRanges() {
        if (m_excludeRanges == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_excludeRanges);
        }
    }

    public void setExcludeRanges(final List<AddressRangeXml> excludeRanges) {
        m_excludeRanges = new ArrayList<>(excludeRanges);
    }

    /**
     * RRD parameters for response time data.
     */
    public RrdXml getRrd() {
        return m_rrd;
    }

    public void setRrd(final RrdXml rrd) {
        m_rrd = rrd;
    }

    public List<ServiceXml> getServices() {
        if (m_services == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_services);
        }
    }

    public void setServices(final List<ServiceXml> services) {
        m_services = new ArrayList<>(services);
    }

    public List<String> getOutageCalendars() {
        if (m_outageCalendars == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_outageCalendars);
        }
    }

    public void setOutageCalendars(final List<String> outageCalendars) {
        m_outageCalendars = new ArrayList<>(outageCalendars);
    }

    public List<DowntimeXml> getDowntimes() {
        if (m_downtimes == null) {
            return Collections.emptyList();
        } else {
            return Collections.unmodifiableList(m_downtimes);
        }
    }

    public void setDowntimes(final List<DowntimeXml> downtimes) {
        m_downtimes = new ArrayList<>(downtimes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_downtimes == null) ? 0 : m_downtimes.hashCode());
        result = prime * result + ((m_excludeRanges == null) ? 0 : m_excludeRanges.hashCode());
        result = prime * result + ((m_filter == null) ? 0 : m_filter.hashCode());
        result = prime * result + ((m_includeRanges == null) ? 0 : m_includeRanges.hashCode());
        result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
        result = prime * result + ((m_outageCalendars == null) ? 0 : m_outageCalendars.hashCode());
        result = prime * result + ((m_rrd == null) ? 0 : m_rrd.hashCode());
        result = prime * result + ((m_services == null) ? 0 : m_services.hashCode());
        result = prime * result + ((m_specifics == null) ? 0 : m_specifics.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PackageXml)) {
            return false;
        }
        PackageXml other = (PackageXml) obj;
        if (m_downtimes == null) {
            if (other.m_downtimes != null) {
                return false;
            }
        } else if (!m_downtimes.equals(other.m_downtimes)) {
            return false;
        }
        if (m_excludeRanges == null) {
            if (other.m_excludeRanges != null) {
                return false;
            }
        } else if (!m_excludeRanges.equals(other.m_excludeRanges)) {
            return false;
        }
        if (m_filter == null) {
            if (other.m_filter != null) {
                return false;
            }
        } else if (!m_filter.equals(other.m_filter)) {
            return false;
        }
        if (m_includeRanges == null) {
            if (other.m_includeRanges != null) {
                return false;
            }
        } else if (!m_includeRanges.equals(other.m_includeRanges)) {
            return false;
        }
        if (m_name == null) {
            if (other.m_name != null) {
                return false;
            }
        } else if (!m_name.equals(other.m_name)) {
            return false;
        }
        if (m_outageCalendars == null) {
            if (other.m_outageCalendars != null) {
                return false;
            }
        } else if (!m_outageCalendars.equals(other.m_outageCalendars)) {
            return false;
        }
        if (m_rrd == null) {
            if (other.m_rrd != null) {
                return false;
            }
        } else if (!m_rrd.equals(other.m_rrd)) {
            return false;
        }
        if (m_services == null) {
            if (other.m_services != null) {
                return false;
            }
        } else if (!m_services.equals(other.m_services)) {
            return false;
        }
        if (m_specifics == null) {
            if (other.m_specifics != null) {
                return false;
            }
        } else if (!m_specifics.equals(other.m_specifics)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Package[name=" + m_name +
                ",filter=" + m_filter +
                ",specifics=" + m_specifics +
                ",includeRanges=" + m_includeRanges +
                ",excludeRanges=" + m_excludeRanges +
                ",rrd=" + m_rrd +
                ",services=" + m_services +
                ",outageCalendars=" + m_outageCalendars +
                ",downtimes=" + m_downtimes +
                "]";
    }
}
