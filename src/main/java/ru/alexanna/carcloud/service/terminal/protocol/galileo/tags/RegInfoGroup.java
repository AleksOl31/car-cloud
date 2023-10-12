package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags;

public class RegInfoGroup extends TagGroupType {
    @Override
    TagGroup getTagGroup() {
        return TagGroup.REG_INFO;
    }
}
