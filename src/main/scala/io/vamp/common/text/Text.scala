package io.vamp.common.text

object Text {

  /**
   * Converts given string to lower camel case format, e.g. "lower-camel' -> "lowerCamel".
   * Hyphens and underscores are used as delimiters and will be removed from the result.
   * Each substring a part of the first one is only capitalized - no other case conversions are applied.
   *
   * @param input String
   * @return String
   */
  def toLowerCamelCase(input: String): String = {
    val words = input.trim.replace('-', '_').split('_').filter(!_.isEmpty)
    words.headOption.fold("") { firstWord =>
      (firstLetterLowerCase(firstWord) +: words.tail.map(_.capitalize)).mkString
    }
  }



  /** Returns the string with first character converted to lower case.
    * If the first character of the string is lowercase, it is returned unchanged.
    * Shameless copy of scala.collection.immutable.StringLike#capitalize with change toLower
    */
  def firstLetterLowerCase(input: String): String =
    if (input == null) null
    else if (input.length == 0) ""
    else if (input.charAt(0).isLower) input
    else {
      val chars = input.toCharArray
      chars(0) = chars(0).toLower
      new String(chars)
    }


  /**
   * Converts given string to upper camel case format, e.g. "upper_camel' -> "UpperCamel".
   * Hyphens and underscores are used as delimiters and will be removed from the result.
   * Each substring is only capitalized - no other case conversions are applied.
   *
   * @param s String
   * @return String
   */
  def toUpperCamelCase(s: String): String = toLowerCamelCase(s).capitalize

  /**
   * Converts given string to snake case format, e.g. "UpperCamel' -> "upper_camel".
   *
   * @param s String
   * @return String
   */
  def toSnakeCase(s: String, dash: Boolean = true): String = {
    var lower = false
    val snake = new StringBuilder

    for (c <- s.toCharArray) {
      val previous = lower
      lower = !Character.isUpperCase(c)
      if (previous && !lower)
        if (dash) snake.append("-") else snake.append("_")
      snake.append(c)
    }

    snake.toString().toLowerCase
  }
}
