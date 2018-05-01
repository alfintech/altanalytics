package io.altanalytics.data.external.cryptocompare.recorder;

import io.altanalytics.data.external.cryptocompare.client.CryptoCompareSocialMediaClient;
import io.altanalytics.domain.social.SocialStats;
import io.altanalytics.persistence.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ismail on 26/04/2018.
 */
@EnableScheduling
@Component
public class CryptoCompareSocialMediaRecorder {

    private static final Logger LOG = LoggerFactory.getLogger(CryptoCompareSocialMediaRecorder.class);

    @Autowired
    protected Publisher publisher;

    @Value("${social.stats.id.list}")
    protected String[] idList;

    @Value("${social.stats.symbols.list}")
    protected String[] symbolList;

    @Value("${recorder.socials.active}")
    private boolean active;

    private List<SocialStats> socialStatsList;

    @Autowired
    public CryptoCompareSocialMediaClient socialMediaClient;

    private Map<String, String> symbolIdMap;

    @PostConstruct
    public void init() throws Exception {
        System.out.println("trying to intialise.");
        if(active) {
            LOG.debug("Initialising social media client");
            process();
        }
    }

    public void process() throws Exception {
        constructSymbolIdMap();
    }

    private void constructSymbolIdMap() {
        symbolIdMap = new HashMap<>();

        for(int i=0; i<symbolList.length; i++) {
            if(symbolList[i]!=null && idList[i]!=null) {
                symbolIdMap.put(symbolList[i], idList[i]);
            }
        }
        socialStatsList = new ArrayList<>(symbolList.length);
    }


    @Scheduled(cron = "${social.media.recorder.live.schedule}")
    public void fetchStats() throws Exception {
        LOG.debug("Started fetching social stats for coins");
        for (String id: idList){
            LOG.debug("fetching social stats for coin id: "+id);
            SocialStats stats = socialMediaClient.fetch(id);
            socialStatsList.add(stats);
            LOG.debug("fetched social stats for coin id: "+id);
        }
        LOG.debug("Publishing social stats for all coins");
        publisher.publishSocialStats(socialStatsList);
        LOG.debug("Finished publishing social stats for all coins");


    }
}
