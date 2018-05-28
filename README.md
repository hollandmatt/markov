# Markov Transformer

A Java application that transforms text according to a Markov Chain algorithm.

## Prerequisites

This application requires:

* Java 8 or later
* Maven

## Building

From the `markov-transformer` directory, run:

```
mvn package
```

This will compile the code and output a JAR file

## Running

From the `markov-transformer` directory, run:

```
./run.sh <trainingFilename> <prefixLength> <outputLength>
```

* trainingFilename: The text file to train the Markov chain with
* prefixLength: Length of the prefix
* outputLength: Length of the output text

Output will be printed to the console, or could be piped to a file.

The `markov-transformer/text` directory contains a few sample texts - these are classic novels 
and were obtained from [Project Gutenberg](http://www.gutenberg.org/). (As such they are in the public domain)

## Process

I used Maven to build the code as it provides a quick way of bootstrapping a new project and saved time writing build scripts etc.
I didn't need to pull in any external dependencies, but it also offers a fairly simple way of managing that.

I was only vaguely familiar with the Markov algorithm at the outset (Although we are all familiar with one use of it via the autocorrect on our smart phones!) and found [This site](http://setosa.io/ev/markov-chains/) a great resource for explaining the theory, and examples such as [this one](https://projects.haykranen.nl/markov/demo/) also provided inspiration.

The chain is represented using a HashMap where the key is the prefix
and the value is the list of possible suffixes. A suffix can be stored more than once in the list, so 
this weights the probabilities accordingly. For example, for prefix "car" we might have:

car -> [wash, park, wash, wash]

which is equivalent to:

car -> [ { wash, P: 0.75}, { park: P 0.25} ]

This means that picking one is a simple random number generation over the length of the list.

A graph data structure would also be an elegant way of storing the chain. I chose the Map mainly due to familiarity with the API vs attempting to find a good graph library for Java.

There are no unit tests for this code: it was difficult to work out how to write tests for something that is essentially random!
