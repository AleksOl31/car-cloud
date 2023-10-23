package ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.decoders;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.Tag;
import ru.alexanna.carcloud.service.terminal.protocol.galileo.tags.TagGroupDecoder;

public class NavigationInfoDecoder extends TagGroupDecoder {

    public NavigationInfoDecoder(int tagCode, MonitoringPackage sourceMonitoringPackage) {
        super(tagCode, sourceMonitoringPackage);
    }

    @Override
    public MonitoringPackage decode(ByteBuf byteBuf) {
        return getNavigationInfo(byteBuf);
    }

    private MonitoringPackage getNavigationInfo(ByteBuf byteBuf) {
        switch (Tag.fromCode(tagCode)) {
            case LOCATION:
                Location location = tagLOCATION(byteBuf);
                sourceMonitoringPackage.getNavigationInfo().setLatitude(location.getLatitude());
                sourceMonitoringPackage.getNavigationInfo().setLongitude(location.getLongitude());
                sourceMonitoringPackage.getNavigationInfo().setSatellitesNum(location.getSatellitesNum());
                sourceMonitoringPackage.getNavigationInfo().setCorrectness(location.getCorrectness());
                sourceMonitoringPackage.getNavigationInfo().setCorrect(location.getCorrect());
                break;
            case MOTION_INFO:
                MotionInfo motionInfo = tagMOTION_INFO(byteBuf);
                sourceMonitoringPackage.getNavigationInfo().setSpeed(motionInfo.getSpeed());
                sourceMonitoringPackage.getNavigationInfo().setCourse(motionInfo.getCourse());
                break;
            case HEIGHT:
                sourceMonitoringPackage.getNavigationInfo().setHeight(tagHEIGHT(byteBuf));
                break;
            case HDOP:
                int hdop = tagHDOP(byteBuf);
                if (sourceMonitoringPackage.getNavigationInfo().getCorrect()) {
                    if (sourceMonitoringPackage.getNavigationInfo().getCorrectness() == 0)
                        sourceMonitoringPackage.getNavigationInfo().setHdop(hdop / 10.);
                    else if (sourceMonitoringPackage.getNavigationInfo().getCorrectness() == 2)
                        sourceMonitoringPackage.getNavigationInfo().setHdop(hdop * 10.);
                }
                break;
            default:
                byteBuf.skipBytes(Tag.fromCode(tagCode).getLength());
        }
        return sourceMonitoringPackage;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Location {
        private Double latitude;
        private Double longitude;
        private Integer satellitesNum;
        private Integer correctness;
        private Boolean correct;

    }

    private static Location tagLOCATION(ByteBuf byteBuf) {
        byte firstByte = byteBuf.readByte();
        Integer satellites = firstByte & 0xf;
        int correctness = (firstByte & 0xf0) >> 4;
        Double latitude = byteBuf.readIntLE() / 1_000_000.;
        Double longitude = byteBuf.readIntLE() / 1_000_000.;
        Boolean isCorrect = correctness == 0 || correctness == 2;
        return new Location(latitude, longitude, satellites, correctness, isCorrect);
    }

    @Getter
    @AllArgsConstructor
    public static class MotionInfo {
        private double speed;
        private double course;

    }

    private static MotionInfo tagMOTION_INFO(ByteBuf byteBuf) {
        double speed = byteBuf.readUnsignedShortLE() / 10.;
        double course = byteBuf.readUnsignedShortLE() / 10.;
        return new MotionInfo(speed, course);
    }

    private static int tagHEIGHT(ByteBuf byteBuf) {
        return byteBuf.readShortLE();
    }

    private static int tagHDOP(ByteBuf byteBuf) {
        return byteBuf.readUnsignedByte();
    }
}
