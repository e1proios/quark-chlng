package chlng.e1proios;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

@ApplicationScoped
public class BlacklistService {

    @Inject
    MongoClient mongoClient;

    public boolean isIbanBlacklisted(String rcvdIban) {
        return this.getBlacklistCollection().countDocuments(Filters.eq("iban", rcvdIban)) > 0;
    }

    public String[] getBlacklistedIbans() {
        List<String> blacklistedIbans = new ArrayList<>();

        try (var cursor = this.getBlacklistCollection().find().iterator()) {
            while (cursor.hasNext()) {
                blacklistedIbans.add(cursor.next().getString("iban"));
            }
        }
        return blacklistedIbans.toArray(new String[0]);
    }

    private MongoCollection<Document> getBlacklistCollection() {
        return mongoClient
            .getDatabase("blacklist")
            .getCollection("ibans");
    }
}
