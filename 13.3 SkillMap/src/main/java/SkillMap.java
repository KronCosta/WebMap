

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

import static java.lang.Thread.sleep;

public class SkillMap extends RecursiveTask<List<String>> {
    private final String pageUrl;
    private final String[] linksDetails = formatMassive();
    public static Set<String> allSites = ConcurrentHashMap.newKeySet();
    public static ArrayList<String> homePageSites = new ArrayList<>();

    public SkillMap(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public static void firstURL(String url) {
        try {
            sleep((long) (Math.random() * (150 - 100 + 1)));
            Document doc = Jsoup.connect(url).get();
            System.out.println(doc);
            Elements elements = doc.select("a");
            for (Element element : elements) {
                String site = element.absUrl("href");
                if (site.trim().length() == 0) {
                    break;
                } else  {
                    homePageSites.add(site);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> compute() { //потоки
        List<String> urlsForkJoin = urlLinks(pageUrl);
        List<SkillMap> secondForkJoins = new ArrayList<>();
        for (String site : urlsForkJoin) {
            if (!newSiteFilter(site) && site.contains(Main.URL)) {
                allSites.add(site);
                System.out.println(site);
                SkillMap forkJoin = new SkillMap(site);
                forkJoin.fork();
                secondForkJoins.add(forkJoin);
            }
        }
        for (SkillMap task : secondForkJoins) {
            urlsForkJoin.addAll(task.join());
        }
        return urlsForkJoin;
    }

    public List<String> urlLinks(String url) {
        List<String> urls = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Thread.sleep(125);
            Elements elements = doc.select("a");
            for (Element element : elements) {
                String site = element.absUrl("href");

                urls.add(site);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return urls;
    }

    public String[] formatMassive() {
        String[] linksDetails = new String[8];
        linksDetails[0] = ".jpg";
        linksDetails[1] = ".png";
        linksDetails[2] = ".gif";
        linksDetails[3] = ".pdf";
        linksDetails[4] = ".bmp";
        linksDetails[5] = "#";
        linksDetails[6] = ".apk";
        linksDetails[7] = " ";

        return linksDetails;
    }

    public boolean newSiteFilter(String site) { //потоки
        if (allSites.contains(site)) {
            return true;
        }
        for (String linksDetail : linksDetails) {
            if (site.contains(linksDetail)) {
                return true;
            }
        }
        return false;
    }

}


