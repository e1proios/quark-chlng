package chlng.e1proios;

import com.mongodb.client.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BlacklistService {

    @Inject
    MongoClient mongoClient;

    public String[] getBlacklistedIbans() {
        List<String> blacklistedIbans = new ArrayList<>();

        var collection = mongoClient
            .getDatabase("blacklist")
            .getCollection("ibans");

        try (var cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                blacklistedIbans.add(cursor.next().getString("iban"));
            }
        }
        return blacklistedIbans.toArray(new String[0]);
    }
}
