package com.bevis.ipfs.cluster

import com.bevis.ipfs.dto.IpfsFile
import com.bevis.ipfs.dto.IpfsPin
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.io.File

@Service
class IpfsClusterProxyImpl(
    private val ipfsClusterService: IpfsClusterService
) : IpfsClusterProxy {

    override fun addFile(file: File): IpfsFile = runBlocking {
        return@runBlocking ipfsClusterService.addFile(file)
    }

    override fun pinFile(ipfsHash: String): IpfsPin = runBlocking {
        return@runBlocking ipfsClusterService.pinFile(ipfsHash)
    }
}
