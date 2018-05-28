package com.hollandmatt;

public class App {
    public static void main( String[] args ) {
        if (args.length < 3) {
            System.out.println("Not enough arguments supplied");
            System.out.println("Args: trainingSetFileName prefixLength outputLength");
            System.exit(1);
        }

        String trainingSetFilename = args[0];
        int prefixLength = Integer.valueOf(args[1]);
        int outputLength = Integer.valueOf(args[2]);

        // construct the transformer, supplying the prefix length to use
        MarkovTransformer transformer = new MarkovTransformer(prefixLength);

        // train the transformer on our training set
        transformer.train(trainingSetFilename);
        // this could be done multiple times...

        // get our output string
        String output = transformer.create(outputLength);

        // print it to the console
        System.out.println(output);

        System.exit(0);
    }
}
