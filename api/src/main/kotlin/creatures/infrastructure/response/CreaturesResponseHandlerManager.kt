package com.jatec.creatures.infrastructure.response

import com.jatec.shared.infrastructure.response.ResponseHandler

class CreaturesResponseHandlerManager {
    val creaturesListResponseHandlerMap = HashMap<String, ResponseHandler>()
    val creaturesCreateOneResponseHandlerMap = HashMap<String, ResponseHandler>()
    val creaturesUpdateOneResponseHandlerMap = HashMap<String, ResponseHandler>()
}