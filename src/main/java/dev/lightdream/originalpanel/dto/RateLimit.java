package dev.lightdream.originalpanel.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RateLimit {

    public int amount;
    public Long interval;

}
