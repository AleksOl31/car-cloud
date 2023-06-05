package ru.alexanna.carcloud.model;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
//@ToString
public class MonitoringPackage {
    private final RegInfo regInfo = new RegInfo();
    private final Device device = new Device();
    private final Navigation navigation = new Navigation();
    private final List<Integer> userTags = new ArrayList<>();

    @Override
    public String toString() {
        return "MonitoringPackage{" +
                "regInfo=" + regInfo + "," + "\r\n" +
                "device=" + device + "," + "\r\n" +
                "navigation=" + navigation + "," + "\r\n" +
                "userTags=" + userTags +
                '}' + "\r\n";
    }
}
