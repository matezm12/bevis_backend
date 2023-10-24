package com.bevis.ipfs;

import com.bevis.common.async.AsyncService;
import com.bevis.files.dto.File;
import com.bevis.ipfs.cluster.IpfsClusterProxy;
import com.bevis.ipfs.dto.IpfsPin;
import com.bevis.ipfs.pinata.PinataIpfsService;
import com.bevis.ipfs.pinata.dto.PinResponse;
import com.bevis.ipfs.dto.IpfsFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
class IpfsServiceImpl implements IpfsService {

    private final PinataIpfsService pinataIpfsService;
    private final AsyncService asyncService;
    private final IpfsClusterProxy ipfsClusterProxy;

    @Override
    public boolean pinFile(String ipfsHash) {
        IpfsPin ipfsPin = ipfsClusterProxy.pinFile(ipfsHash);
        return Objects.nonNull(ipfsPin.getHash());
    }

    @Override
    public IpfsFile postFileAndPin(File file) {
        IpfsFile ipfsFile = ipfsClusterProxy.addFile(file.getFile());
        if (Objects.nonNull(ipfsFile)) {
            asyncService.run0(() -> pinataIpfsService.pinFileToIpfs(file.getFile()));
        } else {
            final PinResponse pinResponse = pinataIpfsService.pinFileToIpfs(file.getFile());
            ipfsFile = new IpfsFile(file.getFileName(), pinResponse.getIpfsHash(), pinResponse.getPinSize());
        }
        log.debug("Published to IPFS: {}", ipfsFile);
        return ipfsFile;
    }

}
