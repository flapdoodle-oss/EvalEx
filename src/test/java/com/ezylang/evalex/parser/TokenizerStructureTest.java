/*
  Copyright 2012-2022 Udo Klimaschewski

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package com.ezylang.evalex.parser;

import com.ezylang.evalex.config.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenizerStructureTest extends BaseParserTest {

  @Test
  void testStructureSimple() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a.b",
      Token.of(1, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, ".", TokenType.STRUCTURE_SEPARATOR),
      Token.of(3, "b", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testStructureLeftIsE() throws ParseException {
    assertAllTokensParsedCorrectly(
        "e.b",
      Token.of(1, "e", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, ".", TokenType.STRUCTURE_SEPARATOR),
      Token.of(3, "b", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testStructureRightIsE() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a.e",
      Token.of(1, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, ".", TokenType.STRUCTURE_SEPARATOR),
      Token.of(3, "e", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testStructureBothAreE() throws ParseException {
    assertAllTokensParsedCorrectly(
        "e.e",
      Token.of(1, "e", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, ".", TokenType.STRUCTURE_SEPARATOR),
      Token.of(3, "e", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testStructureLeftEndsE() throws ParseException {
    assertAllTokensParsedCorrectly(
        "variable.a",
      Token.of(1, "variable", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(9, ".", TokenType.STRUCTURE_SEPARATOR),
      Token.of(10, "a", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testStructureRightStartsE() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a.end",
      Token.of(1, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, ".", TokenType.STRUCTURE_SEPARATOR),
      Token.of(3, "end", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testStructureSeparatorNotAllowedBegin() {
    assertThatThrownBy(() -> new Tokenizer(".", configuration).parse())
        .isEqualTo(new ParseException(1, 1, ".", "Structure separator not allowed here"));
  }

  @Test
  void testStructureSeparatorNotAllowedOperator() {
    assertThatThrownBy(() -> new Tokenizer("-.", configuration).parse())
        .isEqualTo(new ParseException(2, 2, ".", "Structure separator not allowed here"));
  }

  @Test
  void testStructureNotAllowed() {
    Configuration config =
        Configuration.builder().isStructuresAllowed(false).build();

    assertThatThrownBy(() -> new Tokenizer("a.b", config).parse())
        .isEqualTo(new ParseException(2, 2, ".", "Undefined operator '.'"));
  }
}
