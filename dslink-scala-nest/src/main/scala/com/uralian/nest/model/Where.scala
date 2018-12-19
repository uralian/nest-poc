package com.uralian.nest.model

import com.uralian.util.JsonUtils._
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JNull

/**
  * A location info.
  *
  * @param whereId
  * @param name
  */
case class Where(whereId: String, name: String)

/**
  * JSON serializer for Where instances.
  */
object WhereSerializer extends CustomSerializer[Where](_ =>
  ( {
    case json => Where(whereId = json \ "where_id" asString, name = json \ "name" asString)
  }, {
    case _: Where => JNull
  }))