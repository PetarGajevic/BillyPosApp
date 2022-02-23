package com.wind.billypos.base

interface BaseMapper<InputData, OutputData> {

    fun toModel(model: InputData?): OutputData

    fun toListOfModel(listOfModels: List<InputData>): List<OutputData> =
        listOfModels.map { toModel(it) }

    fun toListOfModel(listOfModels: ArrayList<InputData>): ArrayList<OutputData> =
        listOfModels.map { toModel(it) }.toCollection(arrayListOf())

    fun toRemoteModel(model: OutputData?): InputData = TODO("This function is optional")

    fun toRemoteListOfModel(listOfModels: List<OutputData>): List<InputData> =
        listOfModels.map { toRemoteModel(it) }

    fun toRemoteListOfModel(listOfModels: ArrayList<OutputData>): ArrayList<InputData> =
        listOfModels.map { toRemoteModel(it) }.toCollection(arrayListOf())
}