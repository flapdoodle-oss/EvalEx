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

import org.junit.jupiter.api.Test;

class TokenizerOperatorSeparationTest extends BaseParserTest {

  @Test
  void testInfixPrefix() throws ParseException {
    assertAllTokensParsedCorrectly(
        "2+-3",
      Token.of(1, "2", TokenType.NUMBER_LITERAL),
      Token.of(2, "+", TokenType.INFIX_OPERATOR),
      Token.of(3, "-", TokenType.PREFIX_OPERATOR),
      Token.of(4, "3", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testPostfixInfix() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a++-3",
      Token.of(1, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, "++", TokenType.POSTFIX_OPERATOR),
      Token.of(4, "-", TokenType.INFIX_OPERATOR),
      Token.of(5, "3", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testPostfixInfixPrefix() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a++--3",
      Token.of(1, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, "++", TokenType.POSTFIX_OPERATOR),
      Token.of(4, "-", TokenType.INFIX_OPERATOR),
      Token.of(5, "-", TokenType.PREFIX_OPERATOR),
      Token.of(6, "3", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testPrefixPrefix() throws ParseException {
    assertAllTokensParsedCorrectly(
        "!-2",
      Token.of(1, "!", TokenType.PREFIX_OPERATOR),
      Token.of(2, "-", TokenType.PREFIX_OPERATOR),
      Token.of(3, "2", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testEqualsEqualsNumbers() throws ParseException {
    assertAllTokensParsedCorrectly(
        "3==3",
      Token.of(1, "3", TokenType.NUMBER_LITERAL),
      Token.of(2, "==", TokenType.INFIX_OPERATOR),
      Token.of(4, "3", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testEqualsEqualsVariables() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a==b",
      Token.of(1, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, "==", TokenType.INFIX_OPERATOR),
      Token.of(4, "b", TokenType.VARIABLE_OR_CONSTANT));
  }

  @Test
  void testEqualsNumbers() throws ParseException {
    assertAllTokensParsedCorrectly(
        "3=3",
      Token.of(1, "3", TokenType.NUMBER_LITERAL),
      Token.of(2, "=", TokenType.INFIX_OPERATOR),
      Token.of(3, "3", TokenType.NUMBER_LITERAL));
  }

  @Test
  void testEqualsVariables() throws ParseException {
    assertAllTokensParsedCorrectly(
        "a=b",
      Token.of(1, "a", TokenType.VARIABLE_OR_CONSTANT),
      Token.of(2, "=", TokenType.INFIX_OPERATOR),
      Token.of(3, "b", TokenType.VARIABLE_OR_CONSTANT));
  }
}
