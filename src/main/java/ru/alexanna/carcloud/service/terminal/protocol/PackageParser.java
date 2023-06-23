package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;
import ru.alexanna.carcloud.dto.DecodedResultPacket;

public interface PackageParser {
    DecodedResultPacket parse(ByteBuf byteBuf);
//    ByteBuf getResponse();
}
