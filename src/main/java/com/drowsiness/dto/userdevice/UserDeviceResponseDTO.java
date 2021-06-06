package com.drowsiness.dto.userdevice;

import com.drowsiness.model.Device;
import com.drowsiness.model.User;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class UserDeviceResponseDTO implements Serializable {
    private User user;

    private Device device;

    private boolean isConnected;

    private Long connectedAt;
}
