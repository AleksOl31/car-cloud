package ru.alexanna.carcloud.service.terminal.protocol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.alexanna.carcloud.dto.DecodedResultPacket;
import ru.alexanna.carcloud.dto.MonitoringPackage;
import ru.alexanna.carcloud.dto.RegInfo;
import ru.alexanna.carcloud.entities.Item;
import ru.alexanna.carcloud.service.Observable;
import ru.alexanna.carcloud.service.Observer;
import ru.alexanna.carcloud.service.services.ItemService;
import ru.alexanna.carcloud.service.services.TerminalMessageService;

import java.net.SocketAddress;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
public class DecodedPacketDirector implements Observer {
    private Item connectedItem = null;
    private boolean isAuthorized = false;
    private SocketAddress remoteAddress;
    private final TerminalMessageService terminalMessageService;
    private final ItemService itemService;
    private final Observable itemsController;

    public DecodedPacketDirector(TerminalMessageService terminalMessageService, ItemService itemService, Observable itemsController) {
        this.terminalMessageService = terminalMessageService;
        this.itemService = itemService;
        this.itemsController = itemsController;
    }

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
        if (connectedItem != null) {
            terminalMessageService.saveAll(monitoringPackages, connectedItem);
        } else {
            itemsController.removeObserver(this);
            throw new NoSuchElementException("This item has been deregistered");
        }
    }

    private void login(MonitoringPackage registrationPackage) {
        Item storedItem = findStoredItem(registrationPackage.getRegInfo().getImei());
        Item updatedItem = setRegAndConnectedInfo(storedItem, registrationPackage.getRegInfo());
        connectedItem = itemService.save(updatedItem);
        isAuthorized = true;
        itemsController.addObserver(this);
        log.info("The device is registered with IMEI {}, name '{}' and remote address {}",
                connectedItem.getImei(), connectedItem.getName(), connectedItem.getRemoteAddress());
    }

    private Item findStoredItem(String imei) {
        return itemService.findItem(imei).orElseThrow(() ->
                new NoSuchElementException("This item is not registered"));
    }

    private Item setRegAndConnectedInfo(Item upgradableItem, RegInfo regInfo) {
        upgradableItem.setRegInfo(regInfo);
        upgradableItem.setConnected(remoteAddress.toString());
        return upgradableItem;
    }

    public void logout() {
        if (connectedItem != null) {
            itemsController.removeObserver(this);
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

    @Override
    public void updateItem(Long itemId) {
        if (Objects.equals(connectedItem.getId(), itemId)) {
            connectedItem = itemService.findItem(connectedItem.getImei()).orElse(null);
        }
    }
}
