package io.altanalytics.data.external.cryptocompare.recorder;

import io.altanalytics.data.external.cryptocompare.client.CryptoCompareSocialMediaClient;
import io.altanalytics.persistence.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ismail on 26/04/2018.
 */
public class CryptoCompareSocialMediaRecorder {

    @Autowired
    protected Publisher publisher;

    @Value("${social.stats.id.list}")
    protected Integer[] idList;

    @Value("${social.stats.symbols.list}")
    protected String[] symbolList;

    @Value("${recorder.socials.active}")
    private boolean active;

    @Autowired
    public CryptoCompareSocialMediaClient socialMediaClient;

    private Map<String, Integer> symbolIdMap;

    @PostConstruct
    public void init() throws Exception {
        if(active) {
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
    }
}
