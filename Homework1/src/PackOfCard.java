
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * The class contains the pack of cards operations.
 *
 * @author Charmal
 */
public class PackOfCard {

    /**
     * the pack of cards.
     */
    static LinkedHashMap<String, Integer> packOfCards;

    /**
     * Construct the pack of cards.
     */
    public PackOfCard() {

        this.packOfCards = new LinkedHashMap<String, Integer>() {
        };

        this.packOfCards.put("c2", 1);
        this.packOfCards.put("c3", 2);
        this.packOfCards.put("c4", 3);
        this.packOfCards.put("c5", 4);
        this.packOfCards.put("c6", 5);
        this.packOfCards.put("c7", 6);
        this.packOfCards.put("c8", 7);
        this.packOfCards.put("c9", 8);
        this.packOfCards.put("c10", 9);
        this.packOfCards.put("c11", 10);
        this.packOfCards.put("c12", 11);
        this.packOfCards.put("c13", 12);
        this.packOfCards.put("c14", 13);
        this.packOfCards.put("d2", 14);
        this.packOfCards.put("d3", 15);
        this.packOfCards.put("d4", 16);
        this.packOfCards.put("d5", 17);
        this.packOfCards.put("d6", 18);
        this.packOfCards.put("d7", 19);
        this.packOfCards.put("d8", 20);
        this.packOfCards.put("d9", 21);
        this.packOfCards.put("d10", 22);
        this.packOfCards.put("d11", 23);
        this.packOfCards.put("d12", 24);
        this.packOfCards.put("d13", 25);
        this.packOfCards.put("d14", 26);
        this.packOfCards.put("h2", 27);
        this.packOfCards.put("h3", 28);
        this.packOfCards.put("h4", 29);
        this.packOfCards.put("h5", 30);
        this.packOfCards.put("h6", 31);
        this.packOfCards.put("h7", 32);
        this.packOfCards.put("h8", 33);
        this.packOfCards.put("h9", 34);
        this.packOfCards.put("h10", 35);
        this.packOfCards.put("h11", 36);
        this.packOfCards.put("h12", 37);
        this.packOfCards.put("h13", 38);
        this.packOfCards.put("h14", 39);
        this.packOfCards.put("s2", 40);
        this.packOfCards.put("s3", 41);
        this.packOfCards.put("s4", 42);
        this.packOfCards.put("s5", 43);
        this.packOfCards.put("s6", 44);
        this.packOfCards.put("s7", 45);
        this.packOfCards.put("s8", 46);
        this.packOfCards.put("s9", 47);
        this.packOfCards.put("s10", 48);
        this.packOfCards.put("s11", 49);
        this.packOfCards.put("s12", 50);
        this.packOfCards.put("s13", 51);
        this.packOfCards.put("s14", 52);

    }

    /**
     * Returns the set of cards that request by param.
     *
     * @param cardSize
     * @return
     */
    public static ArrayList<String> getSetofCards(int cardSize) {

        ArrayList<String> setofCards = new ArrayList<>();

        LinkedHashMap<String, Integer> selectedPackOfCards
                = new LinkedHashMap<String, Integer>() {
        };

        for (int i = 0; i < cardSize; i++) {
            Map.Entry<String, Integer> draw = drawFromDeck();

            if (draw != null) {
                selectedPackOfCards.put(draw.getKey(), draw.getValue());
            } else {
                i--;
            }
        }

        List<Map.Entry<String, Integer>> entries
                = new ArrayList<>(selectedPackOfCards.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return a.getValue().compareTo(b.getValue());
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());

            setofCards.add(entry.getKey());
        }

        return setofCards;

    }

    /**
     * Random select a card.
     */
    private static Map.Entry<String, Integer> drawFromDeck() {

        Map.Entry<String, Integer> selectedEntry = null;

        Random generator = new Random();

        int index = generator.nextInt(packOfCards.size());

        Iterator iterator = packOfCards.entrySet().iterator();

        int n = 0;

        while (iterator.hasNext()) {

            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) iterator.next();

            if (n == index) {

                selectedEntry = entry;

            }

            n++;
        }

        packOfCards.remove(selectedEntry.getKey(), selectedEntry.getValue());

        return selectedEntry;
    }

}
