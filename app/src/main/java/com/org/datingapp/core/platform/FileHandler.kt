package com.org.datingapp.core.platform

import android.content.Context
import android.net.Uri
import com.org.datingapp.core.extension.getFileName
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileHandler @Inject constructor(@ApplicationContext private val context : Context ) {

    fun getFileFromUri(uri : Uri) : File?  {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r", null)
        parcelFileDescriptor?.let {
            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(context.cacheDir, context.contentResolver.getFileName(uri))
            val outputStream = FileOutputStream(file)
            copy(inputStream, outputStream)
            return file
        }
        return null
    }

    private fun copy(source : InputStream, target : OutputStream) {
        val buf = ByteArray(DEFAULT_BUFFER_SIZE)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }


}