package ru.alexanna.carcloud.dto;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class DecodedResultPacket {
    private final List<MonitoringPackage> monitoringPackages;
    private final ByteBuf response;
}
