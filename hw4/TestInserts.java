// Thesaurus Driver - to test basic inserts and rotations
public class TestInserts{

  public static void main(String[] args){

    Thesaurus t = new Thesaurus();

    // load up dictionary from a file
    TextReader synFile = new TextReader("syn1.txt");
    TextReader kb = new TextReader();

    String word, syn;

    while (synFile.ready()){
      word = synFile.readWord();
      syn = synFile.readWord();
      System.out.println("\nInserting: " + word);
      t.insert(word,syn);
      // display tree
      System.out.println("\nCurrent tree:\n");
      t.printTree();
    }

    // display current thesaurus again
    System.out.println("\n\n" + t.toString());

  }

}
/*

Inserting: happy

Current tree:

happy

Inserting: red

Current tree:

        red
happy

Inserting: yellow

Current tree:

        yellow
red
        happy

Inserting: sad

Current tree:

        yellow
                sad
red
        happy

Inserting: picky

Current tree:

        yellow
                sad
red
                picky
        happy

Inserting: nervous

Current tree:

        yellow
                sad
red
                picky
        nervous
                happy

Inserting: violet

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                happy

Inserting: blue

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                happy
                        blue

Inserting: amber

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: happy

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: red

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: red

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: sad

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: picky

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: nervous

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: blue

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber

Inserting: amber

Current tree:

                yellow
        violet
                sad
red
                picky
        nervous
                        happy
                blue
                        amber


amber   height: 0       frequency: 0
        tan
        ochre

blue    height: 1       frequency: 0
        aqua
        azure

happy   height: 0       frequency: 0
        cheerful
        delighted

nervous height: 2       frequency: 0
        edgy
        unsure

picky   height: 0       frequency: 0
        particular
        selective

red     height: 3       frequency: 0
        maroon
        magenta
        crimson

sad     height: 0       frequency: 0
        unhappy
        despondent

violet  height: 1       frequency: 0
        purple

yellow  height: 0       frequency: 0
        golden


Press any key to continue . . .
*/
