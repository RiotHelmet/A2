package A2;
// SynonymHandler

import static java.lang.System.in;

/****************************************************************

SynonymHandler handles information about synonyms for various
words.

The synonym data can be read from a file and handled in various
ways. These data consists of several lines, where each line begins
with a word, and this word is followed with a number of synonyms.

The synonym line for a given word can be obtained. It is possible
to add a synonym line, and to remove the synonym line for a given
word. Also a synonym for a particular word can be added, or
removed. The synonym data can be sorted. Lastly, the data can be
written to a given file.

Author: Fadil Galjic

****************************************************************/

import java.io.*;    // FileReader, BufferedReader, PrintWriter,
                     // IOException

class SynonymHandler
{
	// readSynonymData reads the synonym data from a given file
	// and returns the data as an array
    public static String[] readSynonymData (String synonymFile)
                                         throws IOException
    {
        BufferedReader reader = new BufferedReader(
	        new FileReader(synonymFile));
        
        int numberOfLines = 0;
        String synonymLine = reader.readLine();
        while (synonymLine != null)
        {
			numberOfLines++;
			synonymLine = reader.readLine();
		}
		reader.close();

		String[] synonymData = new String[numberOfLines];
        reader = new BufferedReader(new FileReader(synonymFile));
		for (int i = 0; i < numberOfLines; i++)
		    synonymData[i] = reader.readLine();
		reader.close();

		return synonymData;
    }

    // writeSynonymData writes a given synonym data to a given
    // file
    public static void writeSynonymData (String[] synonymData,
        String synonymFile) throws IOException
    {
        PrintWriter writer = new PrintWriter(synonymFile);
        for (String synonymLine : synonymData)
            writer.println(synonymLine);
        writer.close();

    }

    // synonymLineIndex accepts synonym data, and returns the
    // index of the synonym line corresponding to a given word.
    // If the given word is not present, an exception of
    // the type IllegalArgumentException is thrown.
	private static int synonymLineIndex (String[] synonymData,
        String word) throws IllegalArgumentException
    {
		int delimiterIndex = 0;
		String w = "";
		int i = 0;
		boolean wordFound = false;
		while (!wordFound  &&  i < synonymData.length)
		{
		    delimiterIndex = synonymData[i].indexOf('|');
		    w = synonymData[i].substring(0, delimiterIndex).trim();
		    if (w.equalsIgnoreCase(word))
				wordFound = true;
			else
				i++;
	    }

	    if (!wordFound)
	        throw new IllegalArgumentException(
		        word + " not present");

	    return i;
	}

    // getSynonymLine accepts synonym data, and returns
    // the synonym line corresponding to a given word.
    // If the given word is not present, an exception of
    // the type IllegalArgumentException is thrown.
    public static String getSynonymLine (String[] synonymData,
        String word) throws IllegalArgumentException
    {
		int index = synonymLineIndex(synonymData, word);

	    return synonymData[index];
	}

    // addSynonymLine accepts synonym data, and adds a given
    // synonym line to the data.
	public static String[] addSynonymLine (String[] synonymData,
	    String synonymLine)
	{
		String[] synData = new String[synonymData.length + 1];
		for (int i = 0; i < synonymData.length; i++)
		    synData[i] = synonymData[i];
		synData[synData.length - 1] = synonymLine;

	    return synData;
	}

    // removeSynonymLine accepts synonym data, and removes
    // the synonym line corresponding to a given word.
    // If the given word is not present, an exception of
    // the type IllegalArgumentException is thrown.
	public static String[] removeSynonymLine (String[] synonymData,
	    String word) throws IllegalArgumentException
	{

        String[] synData = new String[synonymData.length - 1];

        // synonymLineIndex() Already checks if the word is not present so its redundant to do it again. 
        int synIndex = synonymLineIndex(synonymData, word);
        
        for (int i = 0; i < synData.length; i++) {

            int x = (i < synIndex) ? i :  i + 1;

            synData[i] = synonymData[x];
        }

        return synData;
	}

    // getSynonyms returns synonyms in a given synonym line.

	public static String[] getSynonyms (String synonymLine)
	{
        return synonymLine.split(" \\| ")[1].split(", ");
	}

    // addSynonym accepts synonym data, and adds a given
    // synonym for a given word.
    // If the given word is not present, an exception of
    // // the type IllegalArgumentException is thrown.
	public static void addSynonym (String[] synonymData,
	    String word, String synonym) throws IllegalArgumentException
	{
        // add code here

        String synonymLine = getSynonymLine(synonymData, word);

        synonymLine += ", " + synonym;

        int synonymIndex = synonymLineIndex(synonymData, word);

        synonymData[synonymIndex] = synonymLine;

	}

    // removeSynonym accepts synonym data, and removes a given
    // synonym for a given word.
    // If the given word or the given synonym is not present, an
    // exception of the type IllegalArgumentException is thrown.
    // If there is only one synonym for the given word, an
    // exception of the type IllegalStateException is thrown.
	public static void removeSynonym (String[] synonymData,
	    String word, String synonym)
	    throws IllegalArgumentException, IllegalStateException
	{
        // add code here

        int lineIndex = synonymLineIndex(synonymData, word);

        String synonymLine = getSynonymLine(synonymData, word);
        
        String[] synonyms = getSynonyms(synonymLine);

        Boolean wordFound = false;

        String[] newSynonyms = new String[synonyms.length - 1];

        int x = 0;
        
        for (int i = 0; i < synonyms.length; i++) {

            if(!synonyms[i].equals(synonym)) {
                newSynonyms[x] = synonyms[i];
                x++;
            } else {
                wordFound = true;
            }

        }

        if (!wordFound) throw new IllegalArgumentException(synonym + " is not present");


        String returnSynLine = word + " | ";

        for (int i = 0; i < newSynonyms.length; i++) {
            returnSynLine += (i > 0) ? ", " + newSynonyms[i] : newSynonyms[i] ;
        }

        if(synonyms.length == 1) throw new IllegalStateException(synonym + " cant be removed since its the only synonym");

        synonymData[lineIndex] = returnSynLine;
	}

    // sortIgnoreCase sorts an array of strings, using
    // the selection sort algorithm
    private static void sortIgnoreCase (String[] strings)
    {
        // add code here
        for (int j = 0; j < strings.length - 1; j++) {
            int smallestIndex = j;
            for (int i = j; i < strings.length; i++) {

                for (int k = 0; k < ((strings[i].length() < strings[smallestIndex].length()) ? strings[i].length() : strings[smallestIndex].length()); k++) {
                    smallestIndex = (strings[smallestIndex].charAt(k) < strings[i].charAt(k)) ? smallestIndex : i;

                    if(strings[smallestIndex].charAt(k) != strings[i].charAt(k)) break;
                }

            }

            String A = strings[j];
            strings[j] = strings[smallestIndex];
            strings[smallestIndex] = A;
        }
	}

    // sortSynonymLine accepts a synonym line, and sorts
    // the synonyms in this line
    private static String sortSynonymLine (String synonymLine)
    {
	    // add code here

        String[] synonyms = getSynonyms(synonymLine);

        String word = synonymLine.split(" \\| ")[0];

        sortIgnoreCase(synonyms);

        String returnSynLine = word + " | ";

        for (int i = 0; i < synonyms.length; i++) {
            returnSynLine += (i > 0) ? ", " + synonyms[i] : synonyms[i] ;
        }

        return returnSynLine;

	}

    // sortSynonymData accepts synonym data, and sorts its
    // synonym lines and the synonyms in these lines
	public static void sortSynonymData (String[] synonymData)
	{
        // add code here

        String[] words = synonymData.clone();

        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].split(" | ")[0];
        }

        sortIgnoreCase(words);

        String[] newSynonymData = new String[synonymData.length];

        for (int i = 0; i < newSynonymData.length; i++) {
                newSynonymData[i] = getSynonymLine(synonymData, words[i]);
        }

        for (int i = 0; i < synonymData.length; i++) 
        {
            synonymData[i] = sortSynonymLine(newSynonymData[i]);
        }

	}
}