Example application that makes use of the EmbeddedMuleServer
to display Mule statistics on a Web page, which receives the
statistics as JSON data from a Jersey REST resource running
inside Jetty.

To run: 
  > mvn <enter>

Open your web browser and navigate to http://localhost:8080/

In order to see the gauges move, copy any file to target/data/in.
For example:

  > echo "this is a test" > target/data/in/test.txt

Enjoy!

