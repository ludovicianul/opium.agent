package com.insidecoding.opium.agent.job;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.insidecoding.opium.agent.entity.Device;
import com.insidecoding.opium.agent.os.OsCommand;
import com.insidecoding.opium.agent.service.NetworkService;

@Component
public class InspectDevicesJob implements Runnable {

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(InspectDevicesJob.class);

  @Autowired
  @Qualifier("adb")
  private OsCommand adbCommand;

  @Autowired
  private NetworkService netService;

  @Scheduled(cron = "0/10 * * * * ?")
  public void run() {
    try {
      LOG.info("Runing get devices...");
      String devicesListCommandOut = adbCommand.execute("devices", "-l");
      List<Device> devices = this.getDevices(devicesListCommandOut);
      LOG.info("Got devices: " + devices);

      if (devices.size() > 0) {
        netService.sendToMaster(devices);
      }
    } catch (Exception e) {
      LOG.warn("Something went wrong while getting devices: " + e.getMessage());
    }
  }

  public List<Device> getDevices(String adbOutput) throws IOException {
    List<Device> devicesList = new ArrayList<Device>();
    String[] devices = adbOutput.split(System.getProperty("line.separator"));

    LOG.info("Number of devices: " + (devices.length - 1));
    for (int i = 1; i < devices.length; i++) {
      String[] deviceInfo = devices[i].split(" +");
      String uid = deviceInfo[0];
      String authorized = deviceInfo[1];

      if (!uid.trim().isEmpty() && !"*".equals(uid)) {
        Device device = this.getDeviceInfo(uid, authorized);
        devicesList.add(device);
      }
    }

    return devicesList;
  }

  private Device getDeviceInfo(String uid, String authorized) throws IOException {
    LOG.info("Getting device info for: " + uid);
    String ip = netService.getLocalhostIpv4();

    if ("unauthorized".equals(authorized)) {
      Device device = Device.newUnauthorizedDevice();
      device.setIp(ip);
      device.setUid(uid);

      return device;
    }

    String props = adbCommand.execute("-s", uid, "shell", "getprop");

    Properties deviceProps = new Properties();
    deviceProps.load(new StringReader(props));

    Device device = new Device();
    device.setUid(uid);
    device.setIp(ip);
    device.setBrand(deviceProps.getProperty("[ro.product.brand]"));
    device.setModel(deviceProps.getProperty("[ro.product.model]"));
    device.setName(deviceProps.getProperty("[ro.product.name]"));
    device.setManufacturer(deviceProps.getProperty("[ro.product.manufacturer]"));
    device.setAndroidVersion(deviceProps.getProperty("[ro.build.version.release]"));
    device.setSdkVersion(deviceProps.getProperty("[ro.build.version.sdk]"));
    device.setType(deviceProps.getProperty("[ro.build.characteristics]"));

    return device;
  }
}
