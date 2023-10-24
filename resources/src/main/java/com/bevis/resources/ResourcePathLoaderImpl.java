package com.bevis.resources;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.FileSystems;

@Slf4j
@Service
@RequiredArgsConstructor
class ResourcePathLoaderImpl implements ResourcePathLoader {

    private final ResProps resProps;

    @Override
    public String getResourceFolder(String relativeFolder) {
        try {
            return  FileSystems
                    .getDefault()
                    .getPath(resProps.getBaseFolder(), relativeFolder)
                    .toUri()
                    .toURL()
                    .toString();
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResourcePathException("Error building resource folder: " + e.getMessage(), e);

        }
    }

    @Override
    public String getFontsFolder() {
        return getResourceFolder(resProps.getFontsFolder());
    }
}
