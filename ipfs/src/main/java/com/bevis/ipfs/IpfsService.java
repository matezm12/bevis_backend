package com.bevis.ipfs;

import com.bevis.files.dto.File;
import com.bevis.ipfs.dto.IpfsFile;

public interface IpfsService {
    boolean pinFile(String ipfsHash);

    IpfsFile postFileAndPin(File file);

}
