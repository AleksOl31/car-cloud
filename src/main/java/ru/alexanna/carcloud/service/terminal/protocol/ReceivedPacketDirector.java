package ru.alexanna.carcloud.service.terminal.protocol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.alexanna.carcloud.dto.DecodedResultPacket;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.service.services.ItemService;
import ru.alexanna.carcloud.service.services.TerminalMessageService;

import java.net.SocketAddress;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ReceivedPacketDirector {
    private Item connectedItem = null;
    private boolean isAuthorized = false;
    private final TerminalMessageService terminalMessageService;
    private final ItemService itemService;

    public void packetConsumer(SocketAddress address, DecodedResultPacket packet) {
        if (isAuthorized) {
            saveMonitoringPackages(packet.getMonitoringPackages());
        } else {
            login(address, packet.getMonitoringPackages().get(0));
        }
    }

    private void saveMonitoringPackages(List<MonitoringPackage> monitoringPackages) {
        terminalMessageService.saveAll(monitoringPackages, connectedItem);
    }

    private void login(SocketAddress address, MonitoringPackage monitoringPackage) {
        String receivedImei = monitoringPackage.getRegInfo().getImei();
        Item storedItem = itemService.findItem(receivedImei).orElseThrow();
        storedItem.setDeviceId(monitoringPackage.getRegInfo().getDeviceId());
        storedItem.setHardVer(monitoringPackage.getRegInfo().getHardVer());
        storedItem.setSoftVer(monitoringPackage.getRegInfo().getSoftVer());
        storedItem.setConnectionState(true);
        storedItem.setRemoteAddress(address.toString());
        connectedItem = itemService.save(storedItem);
        isAuthorized = true;
    }

    public void logout() {
        if (connectedItem != null) {
            Item storedItem = itemService.findItem(connectedItem.getImei()).orElse(connectedItem);
            log.debug("Disconnected address {}, stored address {}", connectedItem.getRemoteAddress(), storedItem.getRemoteAddress());
            if (connectedItem.getRemoteAddress().equals(storedItem.getRemoteAddress())) {
                connectedItem.setConnectionState(false);
                connectedItem.setRemoteAddress(null);
                itemService.save(connectedItem);
            }
        }
    }

    public void logReadOperation(SocketAddress address) {
        if (connectedItem != null)
            log.info("Data received from device with IMEI {}, name '{}' and address {} ",
                    connectedItem.getImei(),
                    connectedItem.getName(),
                    address);
    }

}
