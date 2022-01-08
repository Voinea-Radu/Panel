package dev.lightdream.originalpanel.managers;

import dev.lightdream.originalpanel.dto.RateLimit;
import dev.lightdream.originalpanel.dto.Requests;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@NoArgsConstructor
public class RateLimiter {

    public HashMap<String, Requests> loginAttempts = new HashMap<>();

    public RateLimit loginAttemptRateLimit = new RateLimit(3, 60 * 1000L);

    public boolean attemptLogin(String user) {
        Requests requests = loginAttempts.getOrDefault(user, new Requests());
        requests.requests.add(System.currentTimeMillis());
        loginAttempts.put(user, requests);
        return !requests.isRateLimited(loginAttemptRateLimit);
    }

}
