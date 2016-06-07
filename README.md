$ mvn clean verify
$ mvn dependency:copy-dependencies
$ cp target/spring-jmx-0.0.1-SNAPSHOT.jar target/dependency/
$ java -Dcom.sun.management.jmxremote -jar target/dependency/spring-jmx-0.0.1-SNAPSHOT.jar 

$ sudo update-alternatives --config java
Find your current JVM (for instance /usr/lib/jvm/java-8-oracle/jre/bin/java)

$ /usr/lib/jvm/java-8-oracle/bin/jconsole

DOCS:

http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#jmx
http://docs.oracle.com/javase/tutorial/jmx/index.html
https://www.youtube.com/watch?v=Qzjs18pJQeU [ Spring Boot for DevOps ]
