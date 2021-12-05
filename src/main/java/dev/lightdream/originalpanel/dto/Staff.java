package dev.lightdream.originalpanel.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Staff implements Comparable<Staff> {

    public String uuid;
    public String username;
    public String rank;


    @Override
    public String toString() {
        return "Staff{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }

    @Override
    public int compareTo(Staff e) {
        return this.username.compareTo(e.username);
    }
}
