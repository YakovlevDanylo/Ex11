import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        //Create Scanner
        Scanner in = new Scanner(System.in);

        //Enter name of book by user
        System.out.print("Enter name of book: ");
        String bookName = in.nextLine();

        //Create new File with path src/{bookName}.txt
        File bookPath = new File("src/" + bookName + ".txt");

        //Create map with frequency of all words
        Map<String, Integer> wordFrequency = new HashMap<>();

        //Read text from file and write all unique words in Map and their frequency
        try (BufferedReader br = new BufferedReader(new FileReader(bookPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words =  line.replaceAll("[^а-яА-ЯёЁ ]", "").toLowerCase().split("\\s+");
                for (String word : words) {
                    if (word.length() > 2) {
                        wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Book not found");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Create new Map with top 10 words by its frequency
        Map<String, Integer> topTenWords = wordFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10).collect(Collectors
                        .toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        //Write to file {bookName}_statistic.txt
        try (FileWriter writer = new FileWriter("src/" + bookName + "_statistic.txt")) {
            for (Map.Entry<String, Integer> entry : topTenWords.entrySet()) {
                writer.write(entry.getKey() + " -> " + entry.getValue() + "\n");
            }
            writer.write("Загальна кількість унікальних слів: " + wordFrequency.size() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Print statistic in console
        System.out.println("Топ 10 частовикористованих слів");
        topTenWords.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println("Загальна кількість унікальних слів: " + wordFrequency.size());
    }
}