/*
 * File: ClusterMember.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of 
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.tools.runtime.coherence;

import com.oracle.tools.runtime.Application;
import com.oracle.tools.runtime.ApplicationConsole;
import com.oracle.tools.runtime.LifecycleEventInterceptor;

import com.oracle.tools.runtime.java.AbstractJavaApplication;
import com.oracle.tools.runtime.java.JavaApplication;
import com.oracle.tools.runtime.java.JavaProcess;

import java.util.Properties;

import java.util.concurrent.TimeUnit;

import javax.management.MBeanInfo;
import javax.management.ObjectName;

/**
 * A {@link ClusterMember} is a specialized {@link com.oracle.tools.runtime.java.SimpleJavaApplication} to
 * represent Coherence-based Cluster Members at runtime.
 * <p>
 * Copyright (c) 2010. All Rights Reserved. Oracle Corporation.<br>
 * Oracle is a registered trademark of Oracle Corporation and/or its affiliates.
 *
 * @author Brian Oliver
 */
public class ClusterMember extends AbstractJavaApplication<ClusterMember, JavaProcess>
{
    /**
     * The MBean name of the Coherence Cluster MBean.
     */
    public static final String MBEAN_NAME_CLUSTER = "Coherence:type=Cluster";

    /**
     * The Cluster Size MBean Attribute. (for ClusterNodeMBean)
     */
    public static final String MBEAN_ATTRIBUTE_CLUSTER_SIZE = "ClusterSize";

    /**
     * The Local Member (Node) Id MBean Attribute. (for ClusterMBean)
     */
    public static final String MBEAN_ATTRIBUTE_LOCAL_MEMBER_ID = "LocalMemberId";

    /**
     * The Role Name MBean Attribute (for ClusterNodeMBean)
     */
    public static final String MBEAN_ATTRIBUTE_ROLE_NAME = "RoleName";

    /**
     * The Site Name MBean Attribute (for ClusterNodeMBean)
     */
    public static final String MBEAN_ATTRIBUTE_SITE_NAME = "SiteName";


    /**
     * Construct a {@link ClusterMember}.
     *
     * @param process               the {@link Process} representing the {@link ClusterMember}
     * @param name                  the name of the {@link ClusterMember}
     * @param console               the {@link ApplicationConsole} that will be used for I/O by the
     *                              realized {@link Application}. This may be <code>null</code> if not required
     * @param environmentVariables  the environment variables used when starting the {@link ClusterMember}
     * @param systemProperties      the system properties provided to the {@link ClusterMember}
     * @param isDiagnosticsEnabled  should diagnostic information be logged/output
     * @param defaultTimeout        the default timeout duration
     * @param defaultTimeoutUnits   the default timeout duration {@link TimeUnit}
     * @param interceptors          the {@link LifecycleEventInterceptor}s
     */
    ClusterMember(JavaProcess                                        process,
                  String                                             name,
                  ApplicationConsole                                 console,
                  Properties                                         environmentVariables,
                  Properties                                         systemProperties,
                  boolean                                            isDiagnosticsEnabled,
                  long                                               defaultTimeout,
                  TimeUnit                                           defaultTimeoutUnits,
                  Iterable<LifecycleEventInterceptor<ClusterMember>> interceptors)
    {
        super(process,
              name,
              console,
              environmentVariables,
              systemProperties,
              isDiagnosticsEnabled,
              defaultTimeout,
              defaultTimeoutUnits,
              interceptors);
    }


    /**
     * Obtains the Coherence Cluster {@link MBeanInfo} for the {@link ClusterMember}.
     * <p>
     * If the JMX infrastructure in the {@link JavaApplication} is not yet
     * available, it will block at wait for the default application timeout
     * until it becomes available.
     *
     * @return a {@link MBeanInfo}
     *
     * @throws com.oracle.tools.deferred.UnresolvableInstanceException
     *                                        when the resource is not available
     * @throws UnsupportedOperationException  when JMX is not enabled for the
     *                                        {@link JavaApplication}
     */
    public MBeanInfo getClusterMBeanInfo()
    {
        try
        {
            return getMBeanInfo(new ObjectName(MBEAN_NAME_CLUSTER));
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException("Could not retrieve the Coherence Cluster MBean", e);
        }
    }


    /**
     * Obtains the {@link Cluster} size based on the reported JMX ClusterSize.
     * <p>
     * If the JMX infrastructure in the {@link JavaApplication} is not yet
     * available, it will block at wait for the default application timeout
     * until it becomes available.
     *
     * @return the number of {@link ClusterMember}s in the {@link Cluster}
     *
     * @throws com.oracle.tools.deferred.UnresolvableInstanceException
     *                                        when the resource is not available
     * @throws UnsupportedOperationException  when JMX is not enabled for the
     *                                        {@link JavaApplication}
     */
    public int getClusterSize()
    {
        try
        {
            return getMBeanAttribute(new ObjectName(MBEAN_NAME_CLUSTER), MBEAN_ATTRIBUTE_CLUSTER_SIZE, Integer.class);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException("Could not retrieve the Coherence Cluster MBean Attribute", e);
        }
    }


    /**
     * Obtains the local member id for the {@link ClusterMember} (from the
     * ClusterMBean).
     * <p>
     * @return the LocalMemberId
     */
    public int getLocalMemberId()
    {
        try
        {
            return getMBeanAttribute(new ObjectName(MBEAN_NAME_CLUSTER),
                                     MBEAN_ATTRIBUTE_LOCAL_MEMBER_ID,
                                     Integer.class);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException("Could not retrieve the Coherence Cluster MBean Attribute", e);
        }
    }


    /**
     * Obtains the Coherence Service {@link MBeanInfo} for the {@link ClusterMember}.
     * <p/>
     * If the JMX infrastructure in the {@link JavaApplication} is not yet
     * available, it will block at wait for the default application timeout
     * until it becomes available.
     *
     * @param serviceName  the name of the service
     * @param nodeId       the nodeId on which the service is defined
     *
     * @return a {@link MBeanInfo}
     *
     * @throws com.oracle.tools.deferred.UnresolvableInstanceException
     *                                        when the resource is not available
     * @throws UnsupportedOperationException  when JMX is not enabled for the
     *                                        {@link JavaApplication}
     */
    public MBeanInfo getServiceMBeanInfo(String serviceName,
                                         int    nodeId)
    {
        try
        {
            return getMBeanInfo(new ObjectName(String.format("Coherence:type=Service,name=%s,nodeId=%d", serviceName,
                                                             nodeId)));
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException(String
                .format("Could not retrieve the Coherence Service MBean [%s]", serviceName),
                                                    e);
        }
    }


    /**
     * Obtains the role name for the local member.
     *
     * @return the site name for the local member
     *
     * @throws com.oracle.tools.deferred.UnresolvableInstanceException
     *                                        when the resource is not available
     * @throws UnsupportedOperationException  when JMX is not enabled for the
     *                                        {@link JavaApplication}
     */
    public String getRoleName()
    {
        try
        {
            return getMBeanAttribute(new ObjectName(String.format("Coherence:type=Node,nodeId=%d", getLocalMemberId())),
                                     MBEAN_ATTRIBUTE_ROLE_NAME,
                                     String.class);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException("Could not retrieve the Coherence Cluster Node MBean", e);
        }
    }


    /**
     * Obtains the site name for the local member.
     *
     * @return the site name for the local member
     *
     * @throws com.oracle.tools.deferred.UnresolvableInstanceException
     *                                        when the resource is not available
     * @throws UnsupportedOperationException  when JMX is not enabled for the
     *                                        {@link JavaApplication}
     */
    public String getSiteName()
    {
        try
        {
            return getMBeanAttribute(new ObjectName(String.format("Coherence:type=Node,nodeId=%d", getLocalMemberId())),
                                     MBEAN_ATTRIBUTE_SITE_NAME,
                                     String.class);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new UnsupportedOperationException("Could not retrieve the Coherence Cluster Node MBean", e);
        }
    }
}
