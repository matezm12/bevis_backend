package com.bevis.ipfs.pinata;

import com.bevis.ipfs.pinata.dto.PinResponse;

import java.io.File;

public interface PinataIpfsService {
    PinResponse pinFileToIpfs(File file);
}
