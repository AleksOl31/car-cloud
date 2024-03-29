package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags;

public enum Tag {
    HARD_VER(0x01, 1),
    SOFT_VER(0x02, 1),
    IMEI(0x03, 15),
    DEVICE_ID(0x04, 2),
    RECORD_NUMBER(0x10, 2),
    TIMESTAMP(0x20, 4),
    LOCATION(0x30, 9),
    MOTION_INFO(0x33, 4),
    HEIGHT(0x34, 2),
    HDOP(0x35, 1),
    DEVICE_STATUS(0x40, 2),
    SUPPLY_VOLTAGE(0x41, 2),
    BATTERY_VOLTAGE(0x42, 2),
    DEVICE_TEMP(0x43, 1),
    ACCELERATION(0x44, 4),
    OUTPUT_STATUS(0x45, 2),
    INPUT_STATUS(0x46, 2),
    EXTENDED_STATUS(0x48, 2),
    ANALOG_INPUT_0(0x50, 2),
    ANALOG_INPUT_1(0x51, 2),
    ANALOG_INPUT_2(0x52, 2),
    ANALOG_INPUT_3(0x53, 2),
    RS232_0(0x58, 2),
    RS232_1(0x59, 2),
    TEMP_0(0x70, 2),
    TEMP_1(0x71, 2),
    TEMP_2(0x72, 2),
    TEMP_3(0x73, 2),
    TEMP_4(0x74, 2),
    TEMP_5(0x75, 2),
    TEMP_6(0x76, 2),
    TEMP_7(0x77, 2),
    IBUTTON_1(0x90, 4),
    CAN_A0(0xC0, 4),
    CAN_A1(0xC1, 4),
    CAN_B0(0xC2, 4),
    CAN_B1(0xC3, 4),
    CAN_8_BIT_R0(0xC4, 1),
    CAN_8_BIT_R1(0xC5, 1),
    CAN_8_BIT_R2(0xC6, 1),
    CAN_8_BIT_R3(0xC7, 1),
    CAN_8_BIT_R4(0xC8, 1),
    CAN_8_BIT_R5(0xC9, 1),
    CAN_8_BIT_R6(0xCA, 1),
    CAN_8_BIT_R7(0xCB, 1),
    CAN_8_BIT_R8(0xCC, 1),
    CAN_8_BIT_R9(0xCD, 1),
    CAN_8_BIT_R10(0xCE, 1),
    CAN_8_BIT_R11(0xCF, 1),
    CAN_8_BIT_R12(0xD0, 1),
    CAN_8_BIT_R13(0xD1, 1),
    CAN_8_BIT_R14(0xD2, 1),
    IBUTTON_2(0xD3, 4),
    TOTAL_MILEAGE(0xD4, 4),
    IBUTTON_STATUS(0xD5, 1),
    CAN_16_BIT_R0(0xD6, 2),
    CAN_16_BIT_R1(0xD7, 2),
    CAN_16_BIT_R2(0xD8, 2),
    CAN_16_BIT_R3(0xD9, 2),
    CAN_16_BIT_R4(0xDA, 2),
    CAN_32_BIT_R0(0xDB, 4),
    CAN_32_BIT_R1(0xDC, 4),
    CAN_32_BIT_R2(0xDD, 4),
    CAN_32_BIT_R3(0xDE, 4),
    CAN_32_BIT_R4(0xDF, 4),
    ANALOG_INPUT_4(0x54, 2),
    ANALOG_INPUT_5(0x55, 2),
    ANALOG_INPUT_6(0x56, 2),
    ANALOG_INPUT_7(0x57, 2),
    DS1923_0(0x80, 3),
    DS1923_1(0x81, 3),
    DS1923_2(0x82, 3),
    DS1923_3(0x83, 3),
    DS1923_4(0x84, 3),
    DS1923_5(0x85, 3),
    DS1923_6(0x86, 3),
    DS1923_7(0x87, 3),
    RS485_0(0x60, 2),
    RS485_1(0x61, 2),
    RS485_2(0x62, 2),
    RS485_3(0x63, 3),
    RS485_4(0x64, 3),
    RS485_5(0x65, 3),
    RS485_6(0x66, 3),
    RS485_7(0x67, 3),
    RS485_8(0x68, 3),
    RS485_9(0x69, 3),
    RS485_10(0x6A, 3),
    RS485_11(0x6B, 3),
    RS485_12(0x6C, 3),
    RS485_13(0x6D, 3),
    RS485_14(0x6E, 3),
    RS485_15(0x6F, 3),
    RS232_EXT_0(0x88, 1),
    RS232_EXT_1(0x89, 1),
    RS485_EXT_0(0x8A, 1),
    RS485_EXT_1(0x8B, 1),
    RS485_EXT_2(0x8C, 1),
    ANALOG_INPUT_8(0x78, 2),
    ANALOG_INPUT_9(0x79, 2),
    ANALOG_INPUT_10(0x7A, 2),
    ANALOG_INPUT_11(0x7B, 2),
    ANALOG_INPUT_12(0x7C, 2),
    ANALOG_INPUT_13(0x7D, 2),
    MILLISECONDS(0x21, 2),
    CAN_8_BIT_R15(0xA0, 1),
    CAN_8_BIT_R16(0xA1, 1),
    CAN_8_BIT_R17(0xA2, 1),
    CAN_8_BIT_R18(0xA3, 1),
    CAN_8_BIT_R19(0xA4, 1),
    CAN_8_BIT_R20(0xA5, 1),
    CAN_8_BIT_R21(0xA6, 1),
    CAN_8_BIT_R22(0xA7, 1),
    CAN_8_BIT_R23(0xA8, 1),
    CAN_8_BIT_R24(0xA9, 1),
    CAN_8_BIT_R25(0xAA, 1),
    CAN_8_BIT_R26(0xAB, 1),
    CAN_8_BIT_R27(0xAC, 1),
    CAN_8_BIT_R28(0xAD, 1),
    CAN_8_BIT_R29(0xAE, 1),
    CAN_8_BIT_R30(0xAF, 1),
    CAN_16_BIT_R5(0xB0, 2),
    CAN_16_BIT_R6(0xB1, 2),
    CAN_16_BIT_R7(0xB2, 2),
    CAN_16_BIT_R8(0xB3, 2),
    CAN_16_BIT_R9(0xB4, 2),
    CAN_16_BIT_R10(0xB5, 2),
    CAN_16_BIT_R11(0xB6, 2),
    CAN_16_BIT_R12(0xB7, 2),
    CAN_16_BIT_R13(0xB8, 2),
    CAN_16_BIT_R14(0xB9, 2),
    CAN_32_BIT_R5(0xF0, 4),
    CAN_32_BIT_R6(0xF1, 4),
    CAN_32_BIT_R7(0xF2, 4),
    CAN_32_BIT_R8(0xF3, 4),
    CAN_32_BIT_R9(0xF4, 4),
    CAN_32_BIT_R10(0xF5, 4),
    CAN_32_BIT_R11(0xF6, 4),
    CAN_32_BIT_R12(0xF7, 4),
    CAN_32_BIT_R13(0xF8, 4),
    CAN_32_BIT_R14(0xF9, 4),
    REP_500(0x5A, 4),
    // Здесь 0x5B (см. описание протокола)
    REFRIGERATOR_DATA(0x5B, null),
    ECO_DRIVE(0x47, 4),
    PRESSURE_PRO(0x5C, 68),
    DBG_C11D(0x5D, 3),
    USER_0(0xE2, 4),
    USER_1(0xE3, 4),
    USER_2(0xE4, 4),
    USER_3(0xE5, 4),
    USER_4(0xE6, 4),
    USER_5(0xE7, 4),
    USER_6(0xE8, 4),
    USER_7(0xE9, 4),
    USER_ARRAY(0xEA, null),
    EXTENDED(0xFE, null);

    private final Integer code, length;
    Tag(Integer code, Integer length) {
        this.code = code;
        this.length = length;
    }

    public Integer getCode() {
        return code;
    }

    public Integer getLength() {
        return length;
    }

    public static Tag fromCode(Integer code) {
        for(final Tag tag: values()) {
            if (tag.getCode().equals(code))
                return tag;
        }
        throw new IllegalStateException("Unknown tag: '" + Integer.toHexString(code) + "'");
    }
}
