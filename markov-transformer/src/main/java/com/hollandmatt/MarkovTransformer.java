package com.hollandmatt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * Transforms a body of text according to a Markov Chain algorith.
 */
public class MarkovTransformer {
    private final static Random r = new Random();

    private final int prefixLength;

    /** Map of prefix -> list of possible suffixes. Some kind of graph data structure could also be used here. */
    private Map<String, List<String>> dictionary;

    public MarkovTransformer(int prefixLength) {
        if (prefixLength < 1) throw new IllegalArgumentException("Prefix length can't be less than 1");
        this.prefixLength = prefixLength;
    }

    /** Train the markov chain on a new text */
    public void train(String trainingSetFilename) {
        try {
            String[] words = getWordsFromTextFile(trainingSetFilename);
            if (this.dictionary == null) {
                this.dictionary = new HashMap<>();
            }
            this.dictionary = buildDictionary(words, this.dictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Read a file from disk and return all the words in contains, in order */
    private String[] getWordsFromTextFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        byte[] bytes = Files.readAllBytes(path);
        String[] words = new String(bytes).trim().split(" ");
        return words;
    }

    /** Build a map of prefix -> list of possible suffixes */
    private Map<String, List<String>> buildDictionary(String[] words, Map<String, List<String>> dict) {
        for (int i = 0; i < (words.length - prefixLength); ++i) {
            StringBuilder key = new StringBuilder(words[i]);
            for (int j = i + 1; j < i + prefixLength; ++j) {
                key.append(' ').append(words[j]);
            }
            String value = (i + prefixLength < words.length) ? words[i + prefixLength] : "";
            if (!dict.containsKey(key.toString())) {
                ArrayList<String> list = new ArrayList<>();
                list.add(value);
                dict.put(key.toString(), list);
            } else {
                dict.get(key.toString()).add(value);
            }
        }

        return dict;
    }

    /** Build a text based on the current training dictionary. The starting point is chosen at random. */
    public String create(int outputSize) {
        int n = 0;

        // Grab a random starting point
        int rn = r.nextInt(this.dictionary.size());
        String prefix = (String) this.dictionary.keySet().toArray()[rn];

        // Create the output with this starting point as its only content
        List<String> output = new ArrayList<>(Arrays.asList(prefix.split(" ")));

        while (true) {
            // read possible suffixes for this prefix string
            List<String> suffixes = this.dictionary.get(prefix);

            // determine which suffix to use, based on equal probability
            if (suffixes.size() == 1) {
                if (Objects.equals(suffixes.get(0), "")) {
                    return output.stream().reduce("", (a, b) -> a + " " + b);
                }
                output.add(suffixes.get(0));
            } else {
                rn = r.nextInt(suffixes.size());
                output.add(suffixes.get(rn));
            }

            if (output.size() >= outputSize) {
                // we have enough, so return the current output
                return output.stream().limit(outputSize).reduce("", (a, b) -> a + " " + b);
            }

            // still work to do, set the prefix as the last part of the current output
            n++;
            prefix = output.stream().skip(n).limit(this.prefixLength).reduce("", (a, b) -> a + " " + b).trim();
        }
    }
}
