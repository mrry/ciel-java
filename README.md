Read me first
=============

This package contains the Java and Scala bindings for the CIEL
distributed execution engine. These bindings allow you to implement
CIEL tasks using the Java and Scala programming langauges.

Installing the CIEL Java bindings
---------------------------------

Before installing the CIEL Java bindings, you will need to install
the Java Development Kit version 5.0 or later, and Apache Maven.

* [Java](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](http://maven.apache.org/)

Once you have installed these prerequisites, you can build the
bindings and examples by typing the following in the current
directory:

```bash
$ mvn install
```

Next, copy the bindings to your JAR library folder. For example, using
`$HOME/lib/ciel`:

```bash
$ mkdir -p $HOME/lib/ciel
$ cp bindings/target/*.jar $HOME/lib/ciel
$ cp scala-bindings/target/*.jar $HOME/lib/ciel # optional for Scala.
```

Also copy the GSON JAR from your Maven repository to your JAR library
folder. For example, if your Maven repository is in
`~/.m2/repository`:

```bash
$ cp ~/.m2/repository/com/google/code/gson/gson/1.7.1/*.jar $HOME/lib/ciel
```

If you want to enable Scala support, also copy the Scala runtime
library to your JAR library folder:

```bash
$ cp ~/.m2/repository/org/scala-lang/scala-library/2.8.1/scala-library-2.8.1.jar $HOME/lib/ciel
```

Finally, update your CIEL configuration with the location of your JAR
library:

```bash
$ ciel config --set java.jar_lib $HOME/lib/ciel
```

Testing your installation
-------------------------

The following command runs a simple test job, which prints "Hello,
world!" on the terminal:

```bash
$ ciel java examples/target/ciel-examples-*.jar com.asgow.ciel.examples.HelloWorld
```

To run the Smith-Waterman example (on files named `<PATH_TO_X>` and `<PATH_TO_Y>`):

```bash
$ ciel java examples/target/ciel-examples-*.jar \
     com.asgow.ciel.examples.smithwaterman.SmithWaterman \
     <NUM_BLOCKS_X> <NUM_BLOCKS_Y> -P x=<PATH_TO_X> -P y=<PATH_TO_Y>
```