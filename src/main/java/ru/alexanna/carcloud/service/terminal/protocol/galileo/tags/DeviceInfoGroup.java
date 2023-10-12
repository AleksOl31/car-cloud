package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags;

public class DeviceInfoGroup extends TagGroupType{
    @Override
    TagGroup getTagGroup() {
        return TagGroup.DEVICE_INFO;
    }
}
