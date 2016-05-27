package com.malsolo.springframework.samples.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.Descriptor;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.InvalidTargetObjectTypeException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.malsolo.springframework.samples.FileReplicatorConfig;
import com.malsolo.springframework.samples.replicator.FileReplicator;

public class MBeansRegistererNoSpring {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FileReplicatorConfig.class);

        FileReplicator documentReplicator = context.getBean(FileReplicator.class);

        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

        try {
            ObjectName objectName = new ObjectName("bean:name=documentReplicator");
            RequiredModelMBean mbean = new RequiredModelMBean();
            mbean.setManagedResource(documentReplicator, "objectReference");

            Descriptor srcDirDescriptor = new DescriptorSupport(
                    new String[] { "name=SrcDir", "descriptorType=attribute", "getMethod=getSrcDir", "setMethod=setSrcDir" });

            ModelMBeanAttributeInfo srcDirInfo = new ModelMBeanAttributeInfo("SrcDir", "java.lang.String", "Source directory", true, true,
                    false, srcDirDescriptor);

            Descriptor destDirDescriptor = new DescriptorSupport(
                    new String[] { "name=DestDir", "descriptorType=attribute", "getMethod=getDestDir", "setMethod=setSrcDir" });

            ModelMBeanAttributeInfo destDirInfo = new ModelMBeanAttributeInfo("DestDir", "java.lang.String", "Destination directory", true,
                    true, false, destDirDescriptor);

            ModelMBeanOperationInfo getSrcDirInfo = new ModelMBeanOperationInfo("Get source directory",
                    FileReplicator.class.getMethod("getSrcDir"));
            ModelMBeanOperationInfo setSrcDirInfo = new ModelMBeanOperationInfo("Set source directory",
                    FileReplicator.class.getMethod("setSrcDir", String.class));
            ModelMBeanOperationInfo getDestDirInfo = new ModelMBeanOperationInfo("Get destination directory",
                    FileReplicator.class.getMethod("getDestDir"));
            ModelMBeanOperationInfo setDestDirInfo = new ModelMBeanOperationInfo("Set destination directory",
                    FileReplicator.class.getMethod("setDestDir", String.class));
            ModelMBeanOperationInfo replicateInfo = new ModelMBeanOperationInfo("Replicate files",
                    FileReplicator.class.getMethod("replicate"));

            ModelMBeanInfo mbeanInfo = new ModelMBeanInfoSupport("FileReplicator", "File replicator",
                    new ModelMBeanAttributeInfo[] { srcDirInfo, destDirInfo }, null,
                    new ModelMBeanOperationInfo[] { getSrcDirInfo, setSrcDirInfo, getDestDirInfo, setDestDirInfo, replicateInfo }, null);
            mbean.setModelMBeanInfo(mbeanInfo);

            mbeanServer.registerMBean(mbean, objectName);

            System.in.read();

        } catch (MalformedObjectNameException | RuntimeOperationsException | MBeanException | InstanceNotFoundException
                | InvalidTargetObjectTypeException | NoSuchMethodException | SecurityException | InstanceAlreadyExistsException
                | NotCompliantMBeanException | IOException e) {
            e.printStackTrace();
        } finally {
            context.close();
        }
    }

}
