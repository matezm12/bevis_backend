package com.bevis.filecore;

import com.bevis.common.async.TransactionalAsyncService;
import com.bevis.filecore.domain.File;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class FileEventListener {

    private final List<FileEventHandler> fileEventHandlers;
    private final TransactionalAsyncService asyncService;

    void onFileBlockUpdated(File file) {
        fileEventHandlers.forEach(h -> asyncService.runAfterCommit(() -> h.onFileBlockUpdated(file.getId())));
    }

}
