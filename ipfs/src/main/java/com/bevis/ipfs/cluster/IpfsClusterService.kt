package com.bevis.ipfs.cluster

import com.bevis.ipfs.dto.IpfsFile
import com.bevis.ipfs.dto.IpfsPin
import java.io.File

interface IpfsClusterService {
    suspend fun addFile(file: File): IpfsFile
    suspend fun pinFile(ipfsHash: String): IpfsPin
}
