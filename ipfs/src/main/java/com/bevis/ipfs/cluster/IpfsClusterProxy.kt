package com.bevis.ipfs.cluster

import com.bevis.ipfs.dto.IpfsFile
import com.bevis.ipfs.dto.IpfsPin
import java.io.File

interface IpfsClusterProxy {
    fun addFile(file: File): IpfsFile
    fun pinFile(ipfsHash: String): IpfsPin
}
