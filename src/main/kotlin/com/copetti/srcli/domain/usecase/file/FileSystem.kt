package com.copetti.srcli.domain.usecase.file

import java.nio.file.Files
import java.nio.file.Path

class FileSystem {

    fun writeFile(path: Path, content: String) {
        Files.writeString(path, content)
    }

    fun readFile(path: Path): String {
        return Files.readString(path)
    }
}