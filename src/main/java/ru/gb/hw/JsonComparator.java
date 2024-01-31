package ru.gb.hw;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonComparator {

    public static void main(String[] args) {
        try {
            File file1 = new File("./first.json");
            File file2 = new File("./second.json");

            JsonNode tree1 = readJsonFile(file1);
            JsonNode tree2 = readJsonFile(file2);

            List<String> differences = compareJsonTrees(tree1, tree2);

            if (differences.isEmpty()) {
                System.out.println("JSON files are identical.");
            } else {
                System.out.println("Differences found:");
                for (String difference : differences) {
                    System.out.println(difference);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonNode readJsonFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(file);
    }

    private static List<String> compareJsonTrees(JsonNode tree1, JsonNode tree2) {
        List<String> differences = new ArrayList<>();
        findDifferences(tree1, tree2, "", differences);
        return differences;
    }

    private static void findDifferences(JsonNode node1, JsonNode node2, String path, List<String> differences) {
        if (node1.isObject()) {
            for (Iterator<String> it = node1.fieldNames(); it.hasNext(); ) {
                String field = it.next();
                findDifferences(node1.get(field), node2.get(field), path + "/" + field, differences);
            }
        } else if (node1.isArray()) {
            for (int i = 0; i < node1.size(); i++) {
                findDifferences(node1.get(i), node2.get(i), path + "[" + i + "]", differences);
            }
        }

        if (!node1.equals(node2)) {
            differences.add("Difference at path: " + path);
            differences.add("Value in file1: " + node1);
            differences.add("Value in file2: " + node2);
        }

    }
}
