# Advanced Netconf Explorer
Advanced Netconf explorer and Netconf client library for Java

This is a graphical explorer for YANG models supported by a NETCONF device or service orchestrator. Features include:
* Retrieving all YANG models supported by a device or orchestrator using the NETCONF monitoring standard.
* Parsing the YANG models (using ODL yangtools) and outputting a tree with all the nodes, which the user can expand / collapse.
* Filtering the model tree by module name and searching the names and descriptions of the YANG nodes in it (e.g. “neighbor count” or “bgp” “neighbor count”).
* Downloading a ZIP-Archive of all YANG-models supported by the device or orchestrator.
* Showing details and generating metadata for a YANG node, e.g. the description, the (sensor-)path, a subtree-filter (for NETCONF development) etc.
* GNMI and IOS XR Telemetry support tools to edit sensor groups and show live data using GRPC.
* Browsing and searching live (operational) data for selected YANG models.

## Setup
### Using Docker

You can easily build and run using docker:
* `docker build -t netconf-explorer .`
* `docker run --name netconf-exlorer -d -p 9269:8080 netconf-explorer`

If you have docker-compose installed this can be shortened to:
* `docker-compose up -d`

Note: You need at least 2-3 GB of RAM on your Docker (Virtual) Machine to run this application. In case you are running it on your
laptop, please increase the RAM assigned to Docker to 3 GB. See https://docs.docker.com/docker-for-windows/#advanced or
https://docs.docker.com/docker-for-mac/#advanced


### Using JDK and Maven
If you have a working Java development environment with maven on your machine, you can also launch the explorer with an embedded webserver using:
* `mvn -e -f explorer/pom.xml jetty:run`

You can also create a WAR file for deployment in an application server using
* `mvn package`

## Using the Explorer

Access port 9269 (or 8080 for the embedded webserver) of the host using a browser. You can then use the explorer to connect to any NETCONF / Yang
enabled device or orchestrator supporting NETCONF Monitoring [RFC 6022](https://tools.ietf.org/html/rfc6022).

1. Enter a hostname or IP-address in the "NETCONF Host"-field (optionally followed by a colon and the NETCONF over SSH port) and input the username and password into the corresponding fields and click "Login". 

2. The explorer will now download and parse all available YANG models. This process may take a minute or two.

3. The following start screen is divided in two parts. On the left-hand side you have a menu listing all YANG models including a simple name-based search and the option to show an individual YANG model in source or download all YANG models as a ZIP-file. On the right-hand side you have the data model tree which allows you to browse and search within the data model (the search will match against the YANG field names and descriptions). If you click on an element details will be shown on the left-hand side.

4. By selecting one or more nodes in the model tree so that they are highlighted in blue, you can use the "Show Data" function to retrieve and visualize the corresponding operational or configurational data from the device. The model tree will then be replaced by the data tree for the selected values and the search bar will let you search by both model names and also values. Again by clicking a node, details will be shown on the left-hand side.

5. For IOS-XR telemetry you will be able to view and edit sensors groups by using the Telemetry Tools on the left-hand side. Select or type the name of a sensor group and use Edit to make changes to it. If you have previously selected a node in the model browser, its sensor path will be prepopulated in the sensor group editor for convenience. If your device runs a 64-bit version of IOS XR, you can also view a JSON-encoding of the live feed of the Telemetry data exactly as it is sent to your telemetry collector.



## ANC - Java Netconf client library
ANC is the basis of the explorer and offers abstraction for most of the features of NETCONF.
It is packaged as a maven artifact so it can be installed using `mvn install` in the `anc` directory. 

