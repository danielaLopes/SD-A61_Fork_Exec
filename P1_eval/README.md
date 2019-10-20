# ForkExec
Evaluation tests for Part 1 (P1)


Instructions using Maven
------------------------

First compile all modules:

	mvn clean install -DskipTests

To run a simple ping using exec plugin, defining the group identifier to T00:

    cd ...-ws-cli
    mvn exec:java -Dgroup.id=T00

To compile and run integration tests:

    cd ...-ws-cli
	mvn verify -Dgroup.id=T00

To compile and run a specific integration test suite:

    cd ...-ws-cli
	mvn verify -Dit.test=PingIT -Dgroup.id=T00

(more integration test running options at http://maven.apache.org/surefire/maven-failsafe-plugin/examples/single-test.html)

All properties defined in the Maven files can be overriden in the command line using the -D switch.

---
leic-sod@disciplinas.tecnico.ulisboa.pt
