package ru.alexanna.carcloud.service.terminal.protocol;

import io.netty.buffer.ByteBuf;

public interface PackageParser {
    void parse(ByteBuf byteBuf);
    ByteBuf getResponse();
}
