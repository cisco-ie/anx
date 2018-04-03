# ANX
ADT Netconf client (ANC) and explorer (ANX)

## Explorer
ANX is a graphical explorer for YANG models supported by a NETCONF device or service orchestrator. Features include:
* Retrieving all YANG models supported by a device or orchestrator using the NETCONF monitoring standard.
* Parsing the YANG models (using ODL yangtools) and outputting a tree with all the nodes, which the user can expand / collapse.
* Filtering the model tree by module name and searching the names and descriptions of the YANG nodes in it (e.g. “neighbor count” or “bgp” “neighbor count”).
* Downloading a ZIP-Archive of all YANG-models supported by the device or orchestrator.
* Showing details and generating metadata for a YANG node, e.g. the description, the (sensor-)path, a subtree-filter (for NETCONF development) etc.
* Telemetry support tools to edit sensor groups and show live data using GRPC.
* Browsing and searching live (operational) data for selected YANG models.


### Quickstart note

You can easily build and run using docker:

docker build -t anx .
docker run --name anx -d -p 9269:8080 anx

Afterwards access port 9269 of the docker host using a browser. You can use ANX to connect to any NETCONF / Yang
enabled device or orchestrator supporting NETCONF Monitoring [RFC 6022](https://tools.ietf.org/html/rfc6022). Enter
its hostname or IP-address in the "NETCONF Host"-field (optionally followed by a colon and the NETCONF over SSH port)
and input the username and password into the corresponding fields and click "Connect".

Note: You need at least 2-3 GB of RAM on your Docker (Virtual) Machine to run this application. In case you are running it on your
laptop, please increase the RAM assigned to Docker to 3 GB. See https://docs.docker.com/docker-for-windows/#advanced or
https://docs.docker.com/docker-for-mac/#advanced


### Docker container
The easiest way to build and deploy the viewer is using the included Dockerfile.
If you have docker-compose installed the application can be built and run by issuing a simple `docker-compose up -d`
and will be available on port 9269. 

### WAR deployment
Alternatively maven can be used to build a WAR-file which can be deployed using Jetty,
Tomcat or a similar servlet container. 

For building use the following command:
* `mvn package`

The resulting war-file can subsequently be found under `explorer/target`. 



## ANC - Java Netconf client library
ANC is the basis of the explorer and offers abstraction for most of the features of NETCONF.
It is packaged as a maven artifact so it can be installed using `mvn install` in the `anc` directory. 

