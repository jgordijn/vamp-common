package io.vamp.common.text

import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{ Matchers, WordSpec }
import Text._

class TextSpec extends WordSpec with TableDrivenPropertyChecks with Matchers {
  val expectations =
    Table(
      ("input", "output"),
      ("", ""),
      ("    ", ""),
      ("correct", "correct"),
      ("correct   ", "correct"),
      ("Correct", "correct"),
      ("CORRECT", "cORRECT"),
      ("cOrReCT", "cOrReCT"),
      ("correct-camel", "correctCamel"),
      ("correct-Camel", "correctCamel"),
      ("Correct-camel", "correctCamel"),
      ("Correct-Camel", "correctCamel"),
      ("coRreCt-CaMel", "coRreCtCaMel"),
      ("correct-camel-case", "correctCamelCase"),
      ("correct_camel_case", "correctCamelCase"),
      ("_correct_camel_case_", "correctCamelCase"),
      ("_c_o_r_r_e_c_t_", "cORRECT"),      
      ("-c-o-r-r-e-c-t-", "cORRECT"),
      ("var5Var1", "var5Var1"),
      ("correct-_--__-__-__", "correct"))

  def testToLowerCamelCase(input: String, output: String) {
    s"convert input $input to $output" in {
      toLowerCamelCase(input) should be(output)
    }
  }

  "Text.toLowerCamelCase" should {
    forAll(expectations) { (input: String, output: String) ⇒
      testToLowerCamelCase(input, output)
    }
  }
}
