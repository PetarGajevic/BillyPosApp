package com.wind.billypos.base

interface BaseViewMapper<InputData, OutputData> {

    fun toModel(model: InputData?): OutputData

    fun toListOfModel(listOfModels: List<InputData>): List<OutputData> =
        listOfModels.map { toModel(it) }

    fun toListOfModel(listOfModels: ArrayList<InputData>): ArrayList<OutputData> =
        listOfModels.map { toModel(it) }.toCollection(arrayListOf())

    fun toLocalModel(model: OutputData?): InputData = TODO("This function is optional")

    fun toLocalListOfModel(listOfModels: List<OutputData>): List<InputData> =
        listOfModels.map { toLocalModel(it) }

    fun toLocalListOfModel(listOfModels: ArrayList<OutputData>): ArrayList<InputData> =
        listOfModels.map { toLocalModel(it) }.toCollection(arrayListOf())


}