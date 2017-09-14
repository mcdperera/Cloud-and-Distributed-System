
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Charmal
 */
public class PackOfCard {

    static ArrayList<String> packOfCards;

    public PackOfCard() {

        this.packOfCards = new ArrayList<>();
        this.packOfCards.add("c2");
        this.packOfCards.add("c3");
        this.packOfCards.add("c4");
        this.packOfCards.add("c5");
        this.packOfCards.add("c6");
        this.packOfCards.add("c7");
        this.packOfCards.add("c8");
        this.packOfCards.add("c9");
        this.packOfCards.add("c10");
        this.packOfCards.add("c11");
        this.packOfCards.add("c12");
        this.packOfCards.add("c13");
        this.packOfCards.add("c14");
        this.packOfCards.add("d2");
        this.packOfCards.add("d3");
        this.packOfCards.add("d4");
        this.packOfCards.add("d5");
        this.packOfCards.add("d6");
        this.packOfCards.add("d7");
        this.packOfCards.add("d8");
        this.packOfCards.add("d9");
        this.packOfCards.add("d10");
        this.packOfCards.add("d11");
        this.packOfCards.add("d12");
        this.packOfCards.add("d13");
        this.packOfCards.add("d14");
        this.packOfCards.add("h2");
        this.packOfCards.add("h3");
        this.packOfCards.add("h4");
        this.packOfCards.add("h5");
        this.packOfCards.add("h6");
        this.packOfCards.add("h7");
        this.packOfCards.add("h8");
        this.packOfCards.add("h9");
        this.packOfCards.add("h10");
        this.packOfCards.add("h11");
        this.packOfCards.add("h12");
        this.packOfCards.add("h13");
        this.packOfCards.add("h14");
        this.packOfCards.add("s2");
        this.packOfCards.add("s3");
        this.packOfCards.add("s4");
        this.packOfCards.add("s5");
        this.packOfCards.add("s6");
        this.packOfCards.add("s7");
        this.packOfCards.add("s8");
        this.packOfCards.add("s9");
        this.packOfCards.add("s10");
        this.packOfCards.add("s11");
        this.packOfCards.add("s12");
        this.packOfCards.add("s13");
        this.packOfCards.add("s14");

    }

    public static ArrayList<String> getSetofCards(int cardSize) {
        ArrayList<String> setofCards = new ArrayList<>();

        for (int i = 0; i < cardSize; i++) {
            setofCards.add(drawFromDeck());
        }
        return setofCards;

    }

    public static String drawFromDeck() {
        String card = "";
        Random generator = new Random();
        int index = generator.nextInt(packOfCards.size());

        card = packOfCards.get(index).toLowerCase();

        packOfCards.remove(index);

        return card;
    }

}
