package dev.lightdream.originalpanel.dto;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.JDAField;

import java.util.ArrayList;
import java.util.Arrays;


public class JDAConfig {

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public JDAEmbed notificationEmbed = new JDAEmbed(0, 255, 0, "New Notification - %notification%", "", "", Arrays.asList(
            new JDAField("URL", "%url%", true)
    ), new ArrayList<>());

}
