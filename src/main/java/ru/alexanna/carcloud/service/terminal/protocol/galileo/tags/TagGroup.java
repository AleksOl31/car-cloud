package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag.*;



@Slf4j
public enum TagGroup {
    REG_INFO(HARD_VER, SOFT_VER, IMEI, DEVICE_ID),
    DEVICE_INFO(TIMESTAMP, RECORD_NUMBER, SUPPLY_VOLTAGE, BATTERY_VOLTAGE, DEVICE_TEMP, DEVICE_STATUS, EXTENDED_STATUS),
    NAVIGATION_INFO(LOCATION, HEIGHT, HDOP, MOTION_INFO, MILLISECONDS, TOTAL_MILEAGE, ACCELERATION),
    ANALOG_INPUTS(ANALOG_INPUT_0, ANALOG_INPUT_1, ANALOG_INPUT_2, ANALOG_INPUT_3, ANALOG_INPUT_4, ANALOG_INPUT_5,
            ANALOG_INPUT_6, ANALOG_INPUT_7, ANALOG_INPUT_8, ANALOG_INPUT_9, ANALOG_INPUT_10, ANALOG_INPUT_11,
            ANALOG_INPUT_12, ANALOG_INPUT_13, INPUT_STATUS, OUTPUT_STATUS),
    DIGITAL_PORTS(RS485_0, RS485_1, RS485_2, RS485_3, RS485_4, RS485_5, RS485_6, RS485_7, RS485_8, RS485_9,
            RS485_10, RS485_11, RS485_12, RS485_13, RS485_14, RS485_15, RS232_0, RS232_1, RS232_EXT_0, RS232_EXT_1,
            RS485_EXT_0, RS485_EXT_1, RS485_EXT_2),
    WIRE_1(TEMP_0, TEMP_1, TEMP_2, TEMP_3, TEMP_4, TEMP_5, TEMP_6, TEMP_7, IBUTTON_1, IBUTTON_2, IBUTTON_STATUS),
    DS1923(DS1923_0, DS1923_1, DS1923_2, DS1923_3, DS1923_4, DS1923_5, DS1923_6, DS1923_7),
    CAN_STANDARD(CAN_A0, CAN_A1, CAN_B0, CAN_B1),
    CAN_8_BIT(CAN_8_BIT_R0, CAN_8_BIT_R1, CAN_8_BIT_R2, CAN_8_BIT_R3, CAN_8_BIT_R4, CAN_8_BIT_R5, CAN_8_BIT_R6,
            CAN_8_BIT_R7, CAN_8_BIT_R8, CAN_8_BIT_R9, CAN_8_BIT_R10, CAN_8_BIT_R11, CAN_8_BIT_R12, CAN_8_BIT_R13,
            CAN_8_BIT_R14, CAN_8_BIT_R15, CAN_8_BIT_R16, CAN_8_BIT_R17, CAN_8_BIT_R18, CAN_8_BIT_R19, CAN_8_BIT_R20,
            CAN_8_BIT_R21, CAN_8_BIT_R22, CAN_8_BIT_R23, CAN_8_BIT_R24, CAN_8_BIT_R25, CAN_8_BIT_R26, CAN_8_BIT_R27,
            CAN_8_BIT_R28, CAN_8_BIT_R29, CAN_8_BIT_R30),
    CAN_16_BIT(CAN_16_BIT_R0, CAN_16_BIT_R1, CAN_16_BIT_R2, CAN_16_BIT_R3, CAN_16_BIT_R4, CAN_16_BIT_R5, CAN_16_BIT_R6,
            CAN_16_BIT_R7, CAN_16_BIT_R8, CAN_16_BIT_R9, CAN_16_BIT_R10, CAN_16_BIT_R11, CAN_16_BIT_R12, CAN_16_BIT_R13,
            CAN_16_BIT_R14),
    CAN_32_BIT(CAN_32_BIT_R0, CAN_32_BIT_R1, CAN_32_BIT_R2, CAN_32_BIT_R3, CAN_32_BIT_R4, CAN_32_BIT_R5, CAN_32_BIT_R6,
            CAN_32_BIT_R7, CAN_32_BIT_R8, CAN_32_BIT_R9, CAN_32_BIT_R10, CAN_32_BIT_R11, CAN_32_BIT_R12, CAN_32_BIT_R13,
            CAN_32_BIT_R14),

    ADDITIONAL(ECO_DRIVE, REFRIGERATOR_DATA),
    USER_TAGS(USER_0, USER_1, USER_2, USER_3, USER_4, USER_5, USER_6, USER_7),
    EXTENDED_TAGS(EXTENDED);

    private final Set<Tag> tags;

    TagGroup(Tag... codes) {
        tags = Arrays.stream(codes).collect(Collectors.toSet());
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public static TagGroup fromCode(Integer tagCode) {
        for (final TagGroup tagGroup: values()) {
            for (final Tag tag: tagGroup.getTags()) {
                if (tag.equals(Tag.fromCode(tagCode)))
                    return tagGroup;
            }
        }
        throw new IllegalStateException("Tag with code '" + Integer.toHexString(tagCode) + "' not found in groups");
    }

}
