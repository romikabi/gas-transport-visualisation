package com.gas.transport.visualisation.web.controller

import com.gas.transport.visualisation.web.dto.ExcelRemoveDto
import com.gas.transport.visualisation.web.service.ExcelWorkbookUploadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class DataController {

    @Autowired
    private lateinit var uploadService: ExcelWorkbookUploadService

    @PostMapping("data/upload", consumes = ["multipart/form-data"])
    fun uploadExcel(@RequestParam("snapshotId") snapshotId: String, @RequestParam("year") year: Int, @RequestParam("file") file: MultipartFile) {
        uploadService.loadExcelBook(snapshotId, year, file)
    }

    @PostMapping("data/remove")
    fun removeExcel(@RequestBody filter: ExcelRemoveDto){
        uploadService.removeBook(filter)
    }


}