package ru.alexanna.carcloud.service.terminal.protocol;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.dto.DecodedResultPacket;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.dto.RegInfo;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.service.services.ItemService;
import ru.alexanna.carcloud.service.services.TerminalMessageService;

import java.net.SocketAddress;
import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class DecodedPacketDirector {
    private Item connectedItem = null;
    private boolean isAuthorized = false;
    private final TerminalMessageService terminalMessageService;
    private final ItemService itemService;
    private SocketAddress remoteAddress;

    public void consumePacket(SocketAddress remoteAddress, DecodedResultPacket packet) {
        this.remoteAddress = remoteAddress;
        if (isAuthorized) {
            saveMonitoringPackages(packet.getMonitoringPackages());
        } else {
            MonitoringPackage registrationPackage = packet.getMonitoringPackages().get(0);
            login(registrationPackage);
        }
    }

    private void saveMonitoringPackages(List<MonitoringPackage> monitoringPackages) {
        terminalMessageService.saveAll(monitoringPackages, connectedItem);
    }

    private void login(MonitoringPackage registrationPackage) {
        Item storedItem = findStoredItem(registrationPackage.getRegInfo());
        Item updatedItem = updateItemInfo(storedItem, registrationPackage.getRegInfo());
        connectedItem = itemService.save(updatedItem);
        isAuthorized = true;
        log.info("The device is registered with IMEI {}, name '{}' and remote address {}",
                connectedItem.getImei(), connectedItem.getName(), connectedItem.getRemoteAddress());
    }

    private Item findStoredItem(RegInfo regInfo) {
        String receivedImei = regInfo.getImei();
        return itemService.findItem(receivedImei).orElseThrow();
    }

    private Item updateItemInfo(Item upgradableItem, RegInfo regInfo) {
        upgradableItem.setRegInfo(regInfo);
        upgradableItem.setConnected(remoteAddress.toString());
        return upgradableItem;
    }

    public void logout() {
        if (connectedItem != null) {
            Item storedItem = itemService.findItem(connectedItem.getImei()).orElse(new Item());
            log.debug("Disconnected address {}, stored address {}", connectedItem.getRemoteAddress(), storedItem.getRemoteAddress());
            if (connectedItem.getRemoteAddress().equals(storedItem.getRemoteAddress())) {
                connectedItem.setDisconnected();
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
