package com.bevis.assetimport;

import java.io.File;
import java.util.List;

public interface CodeReaderNotificationSender {
    void sendCodeReaderNotification(Object data, File file, List<String> receivers);
}
