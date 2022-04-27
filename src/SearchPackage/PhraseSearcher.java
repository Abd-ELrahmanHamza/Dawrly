package SearchPackage;

import java.util.*;

public class PhraseSearcher {
    private HashMap<String, HashMap<String, Pair<Integer, Integer, Double, Integer>>> invertedIndex;
    private List<String> docsWithAllOccurrence;
    private List<String> query;

    private List<String> goldenDocuments;

    private HashMap<String, Pair3<Double, String, String, String>> orderedDocs;

    public PhraseSearcher(HashMap<String, HashMap<String, Pair<Integer, Integer, Double, Integer>>> invertedIndex, HashMap<String, Pair3<Double, String, String, String>> orderedDocs, List<String> docsWithAllOccurrence, List<String> query) {
        this.invertedIndex = invertedIndex;
        this.docsWithAllOccurrence = docsWithAllOccurrence;
        this.query = query;
        this.orderedDocs = orderedDocs;
        goldenDocuments = new ArrayList<>();
        buildAllOccurrenceDocs();
        moveDocumentUp();
    }

    // TODO: implement a function that returns a list of documents in which the words appeared in same order
    private void buildAllOccurrenceDocs() {
        for (String document : docsWithAllOccurrence) {
            Integer[][] wordsIndices = new Integer[query.size()][];
            for (int i = 0; i < query.size(); i++) {
                List<Integer> index = new ArrayList<>(invertedIndex.get(query.get(i)).get(document).index);
                wordsIndices[i] = new Integer[index.size()];
                index.toArray(wordsIndices[i]);
            }
            for (int i = 0; i < query.size(); i++) {
                addToArray(wordsIndices[i], query.size() - i - 1);
            }
            System.out.println(Arrays.deepToString(wordsIndices));
            List<Integer> resultList = new ArrayList<>(List.of(wordsIndices[0]));
            for (int i = 0; i < query.size(); i++) {
                resultList.retainAll(List.of(wordsIndices[i]));
            }
            System.out.println(resultList);
            if (!resultList.isEmpty())
                goldenDocuments.add(document);
        }
    }

    private void addToArray(Integer[] list, int value) {
        for (int i = 0; i < list.length; i++) {
            list[i] += value;
        }
    }

    // TODO: Implement a function that make golden docs at the top of data structure (Built by the ranker)
    private void moveDocumentUp() {
        System.out.println("Before moving up");
        System.out.println(orderedDocs);
        for (String doc : goldenDocuments) {
            Pair3<Double, String, String, String> tempPair = orderedDocs.get(doc);
            Pair3<Double, String, String, String> tempPair2 = new Pair3<>(tempPair.score, tempPair.paragraph, tempPair.title, tempPair.word);
            orderedDocs.remove(doc);
            // insert at the beginning of  hashmap
            LinkedHashMap<String, Pair3<Double, String, String, String>> newMap = (LinkedHashMap<String, Pair3<Double, String, String, String>>) orderedDocs.clone();
            orderedDocs.clear();
            orderedDocs.put(doc, tempPair2);
            orderedDocs.putAll(newMap);
        }
        System.out.println("Before moving up");
        System.out.println(orderedDocs);
    }

    // TODO: Implement the interface
    public HashMap<String, Pair3<Double, String, String, String>> getOrderedDocs() {
        return this.orderedDocs;
    }
}
