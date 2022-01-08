package dev.lightdream.originalpanel.dto;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Requests {

    public List<Long> requests = new ArrayList<>();

    public boolean isRateLimited(RateLimit rateLimit) {
        return getRateLimitedRequests(rateLimit).size() > rateLimit.amount;
    }

    public List<Long> getRateLimitedRequests(RateLimit rateLimit) {

        List<Long> output = new ArrayList<>();

        requests.forEach(request -> {
            if (request >= System.currentTimeMillis() - rateLimit.interval) {
                output.add(request);
            }
        });
        return output;
    }

}
