/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018-2022 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2022 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the Apache License Version 2.0 as published
 * by the Apache Software Foundation.
 *
 
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Apache License Version 2.0 for more details.
 *
 * You should have received a copy of the Apache License Version 2.0
 * along with OpenNMS(R).  If not, see:
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.xml;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.events.AlarmData;
import org.opennms.integration.api.v1.config.events.AlarmType;
import org.opennms.integration.api.v1.config.events.AttributeType;
import org.opennms.integration.api.v1.config.events.CollectionGroup;
import org.opennms.integration.api.v1.config.events.EventDefinition;
import org.opennms.integration.api.v1.config.events.LogMessage;
import org.opennms.integration.api.v1.config.events.LogMsgDestType;
import org.opennms.integration.api.v1.config.events.ManagedObject;
import org.opennms.integration.api.v1.config.events.Mask;
import org.opennms.integration.api.v1.config.events.MaskElement;
import org.opennms.integration.api.v1.config.events.Parameter;
import org.opennms.integration.api.v1.config.events.UpdateField;
import org.opennms.integration.api.v1.config.events.Varbind;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.xml.schema.eventconf.Event;
import org.opennms.integration.api.xml.schema.eventconf.Events;
import org.opennms.integration.api.xml.schema.eventconf.LogDestType;
import org.opennms.integration.api.xml.schema.eventconf.Logmsg;
import org.opennms.integration.api.xml.schema.eventconf.Maskelement;

/**
 * Used to load XML event definitions from the class-path.
 *
 * @author jwhite
 * @since 1.0.0
 */
public class ClasspathEventDefinitionLoader extends ClasspathXmlLoader<Events> {

    public ClasspathEventDefinitionLoader(Class<?> clazz, String... fileNames) {
        super(clazz, Events.class, "events", fileNames);
    }

    public List<EventDefinition> getEventDefinitions() {
        return getObjects().stream()
                .flatMap(e -> toEventDefinitions(e).stream())
                .collect(Collectors.toList());
    }

    private static List<EventDefinition> toEventDefinitions(Events events) {
        return events.getEvents().stream()
                .map(ClasspathEventDefinitionLoader::toEventDefinition)
                .collect(Collectors.toList());
    }

    private static EventDefinition toEventDefinition(Event e) {
        final Severity severity = Severity.get(e.getSeverity());
        final LogMessage logMessage = toLogMessage(e.getLogmsg());
        final AlarmData alarmData = toAlarmData(e.getAlarmData());
        final Mask mask = toMask(e.getMask());
        final List<Parameter> parameters = e.getParameters().stream()
                .map(ClasspathEventDefinitionLoader::toParameter)
                .collect(Collectors.toList());
        final List<CollectionGroup> collectionGroups = e.getCollectionGroup().stream()
                .map(ClasspathEventDefinitionLoader::toCollectionGroup)
                .collect(Collectors.toList());
        final EventDefinition def = new EventDefinition() {
            @Override
            public int getPriority() {
                return e.getPriority();
            }

            @Override
            public String getUei() {
                return e.getUei();
            }

            @Override
            public String getLabel() {
                return e.getEventLabel();
            }

            @Override
            public Severity getSeverity() {
                return severity;
            }

            @Override
            public String getDescription() {
                return e.getDescr();
            }

            @Override
            public String getOperatorInstructions() {
                return e.getOperinstruct();
            }

            @Override
            public LogMessage getLogMessage() {
                return logMessage;
            }

            @Override
            public AlarmData getAlarmData() {
                return alarmData;
            }

            @Override
            public Mask getMask() {
                return mask;
            }

            @Override
            public List<Parameter> getParameters() {
                return parameters;
            }

            @Override
            public List<CollectionGroup> getCollectionGroup() {
                return collectionGroups;
            }
        };
        return def;
    }

    private static Parameter toParameter(org.opennms.integration.api.xml.schema.eventconf.Parameter p) {
        final Parameter parm = new Parameter() {
            @Override
            public String getName() {
                return p.getName();
            }

            @Override
            public String getValue() {
                return p.getValue();
            }

            @Override
            public boolean shouldExpand() {
                return p.getExpand();
            }
        };
        return parm;
    }

    private static Mask toMask(org.opennms.integration.api.xml.schema.eventconf.Mask eMask) {
        if (eMask == null) {
            return null;
        }
        final List<MaskElement> maskElements = eMask.getMaskelements().stream()
                .map(ClasspathEventDefinitionLoader::toMaskElemement)
                .collect(Collectors.toList());
        final List<Varbind> varbinds = eMask.getVarbinds().stream()
                .map(ClasspathEventDefinitionLoader::toVarbind)
                .collect(Collectors.toList());
        final Mask mask = new Mask() {
            @Override
            public List<MaskElement> getMaskElements() {
                return maskElements;
            }

            @Override
            public List<Varbind> getVarbinds() {
                return varbinds;
            }
        };
        return mask;
    }

    private static MaskElement toMaskElemement(Maskelement el) {
        final MaskElement maskElement = new MaskElement() {
            @Override
            public String getName() {
                return el.getMename();
            }

            @Override
            public List<String> getValues() {
                return el.getMevalues();
            }
        };
        return maskElement;
    }

    private static Varbind toVarbind(org.opennms.integration.api.xml.schema.eventconf.Varbind vb) {
        final Varbind varbind = new Varbind() {
            @Override
            public String getTextualConvention() {
                return vb.getTextualConvention();
            }

            @Override
            public Integer getNumber() {
                return vb.getVbnumber();
            }

            @Override
            public List<String> getValues() {
                return vb.getVbvalues();
            }
        };
        return varbind;
    }

    private static LogMessage toLogMessage(Logmsg log) {
        final LogMsgDestType dest = toLogMsgDestType(log.getDest());
        final LogMessage logMsg = new LogMessage() {
            @Override
            public String getContent() {
                return log.getContent();
            }

            @Override
            public LogMsgDestType getDestination() {
                return dest;
            }
        };
        return logMsg;
    }

    private static LogMsgDestType toLogMsgDestType(LogDestType dest) {
        if (dest != null) {
            switch(dest) {
                case LOGNDISPLAY:
                    return LogMsgDestType.LOGNDISPLAY;
                case DISPLAYONLY:
                    return LogMsgDestType.DISPLAYONLY;
                case LOGONLY:
                    return LogMsgDestType.LOGONLY;
                case SUPPRESS:
                    return LogMsgDestType.SUPPRESS;
                case DONOTPERSIST:
                    return LogMsgDestType.DONOTPERSIST;
                case DISCARDTRAPS:
                    return LogMsgDestType.DISCARDTRAPS;
            }
        }
        return LogMsgDestType.LOGNDISPLAY;
    }

    private static AlarmData toAlarmData(org.opennms.integration.api.xml.schema.eventconf.AlarmData alarm) {
        if (alarm == null) {
            return null;
        }
        final List<UpdateField> updateFields = alarm.getUpdateFields().stream()
                .map(ClasspathEventDefinitionLoader::toUpdateField)
                .collect(Collectors.toList());
        final AlarmType type = AlarmType.fromId(alarm.getAlarmType());
        final ManagedObject managedObject = toManagedObject(alarm.getManagedObject());
        final AlarmData alarmData = new AlarmData() {
            @Override
            public String getReductionKey() {
                return alarm.getReductionKey();
            }

            @Override
            public AlarmType getType() {
                return type;
            }

            @Override
            public String getClearKey() {
                return alarm.getClearKey();
            }

            @Override
            public boolean isAutoClean() {
                return alarm.getAutoClean();
            }

            @Override
            public List<UpdateField> getUpdateFields() {
                return updateFields;
            }

            @Override
            public ManagedObject getManagedObject() {
                return managedObject;
            }
        };
        return alarmData;
    }

    private static ManagedObject toManagedObject(org.opennms.integration.api.xml.schema.eventconf.ManagedObject mo) {
        if (mo == null) {
            return null;
        }
        final ManagedObject managedObject = new ManagedObject() {
            @Override
            public String getType() {
                return mo.getType();
            }
        };
        return managedObject;
    }

    private static UpdateField toUpdateField(org.opennms.integration.api.xml.schema.eventconf.UpdateField field) {
        final UpdateField updateField = new UpdateField() {
            @Override
            public String getName() {
                return field.getFieldName();
            }

            @Override
            public boolean isUpdatedOnReduction() {
                return field.getUpdateOnReduction();
            }
        };
        return updateField;
    }

    private static CollectionGroup toCollectionGroup(org.opennms.integration.api.xml.schema.eventconf.CollectionGroup eCollectionGroup) {
        if (eCollectionGroup == null) {
            return null;
        }
        final List<CollectionGroup.Collection> collections = eCollectionGroup.getCollection().stream()
                .map(ClasspathEventDefinitionLoader::toCollection)
                .collect(Collectors.toList());
        final CollectionGroup collectionGroup = new CollectionGroup() {
            @Override
            public String getName() {
                return eCollectionGroup.getName();
            }

            @Override
            public String getResourceType() {
                return eCollectionGroup.getResourceType();
            }

            @Override
            public String getInstance() {
                return eCollectionGroup.getInstance();
            }

            @Override
            public Rrd getRrd() {
                return toRrd(eCollectionGroup.getRrd());
            }

            @Override
            public List<Collection> getCollection() {
                return collections;
            }
        };
        return collectionGroup;
    }

    private static CollectionGroup.Rrd toRrd(org.opennms.integration.api.xml.schema.eventconf.CollectionGroup.Rrd eRrd) {
        if (eRrd == null) {
            return null;
        }
        final CollectionGroup.Rrd rrd = new CollectionGroup.Rrd(){
            @Override
            public Integer getStep() {
                return eRrd.getStep();
            }

            @Override
            public int getHeartBeat() {
                return eRrd.getHeartBeat();
            }

            @Override
            public List<String> getRras() {
                return eRrd.getRras();
            }

            @Override
            public File getBaseDir() {
                return eRrd.getBaseDir();
            }
        };
        return rrd;
    }

    private static CollectionGroup.Collection toCollection(org.opennms.integration.api.xml.schema.eventconf.CollectionGroup.Collection eCollection) {
        if (eCollection == null) {
            return null;
        }
        final List<CollectionGroup.ParamValue> paramValues = eCollection.getParamValue().stream()
                .map(ClasspathEventDefinitionLoader::toParamValue)
                .collect(Collectors.toList());
        final CollectionGroup.Collection collection = new CollectionGroup.Collection(){
            @Override
            public String getName() {
                return eCollection.getName();
            }

            @Override
            public String getRename() {
                return eCollection.getRename();
            }

            @Override
            public AttributeType getType() {
                return eCollection.getType();
            }

            @Override
            public List<CollectionGroup.ParamValue> getParamValue() {
                return paramValues;
            }
        };
        return collection;
    }

    private static CollectionGroup.ParamValue toParamValue(org.opennms.integration.api.xml.schema.eventconf.CollectionGroup.ParamValue eParamValue) {
        if (eParamValue == null) {
            return null;
        }
        final CollectionGroup.ParamValue paramValue = new CollectionGroup.ParamValue(){
            @Override
            public String getName() {
                return eParamValue.getName();
            }

            @Override
            public Double getValue() {
                return eParamValue.getValue();
            }
        };
        return paramValue;
    }
}
