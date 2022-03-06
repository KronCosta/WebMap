import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static String URL = "http://www.playback.ru/";

    public static void main(String[] args) {
        SkillMap.firstURL(URL);

        int numOfThread = Runtime.getRuntime().availableProcessors();
        ForkJoinPool pool = new ForkJoinPool(numOfThread);
        for (String url : SkillMap.homePageSites) {
            pool.invoke(new SkillMap(url));
        }
    }
//        recordInFile(SkillMap.allSites);


//    public static void recordInFile(Set<String> link) {
//        try {
//            PrintWriter writer = new PrintWriter("src/main/resources/SkillMap.txt");
//            String tabulation = "\t";
//            StringBuilder tabulationPlusTabulation = new StringBuilder();
//            for (String site : link){
//                writer.write(tabulationPlusTabulation + site + "\n");
//                tabulationPlusTabulation.append(tabulation);
//            }
//
//            writer.flush();
//            writer.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

}
