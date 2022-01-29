package dev.lightdream.originalpanel.dto;

import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.dto.JdaField;

import java.util.ArrayList;
import java.util.Arrays;

public class JDAConfig {

    public JdaEmbed notificationEmbed = new JdaEmbed(0, 255, 0, "New Notification - %notification%", "", "", Arrays.asList(
            new JdaField("URL", "%url%", true)
    ), new ArrayList<>());

}
