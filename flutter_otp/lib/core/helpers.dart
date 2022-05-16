import 'dart:math';

final _emailMaskRegExp = RegExp('^(.)(.*?)([^@]?)(?=@[^@]+\$)');

class Helpers {

  static String maskEmail(String input, [int minFill = 4, String fillChar = '*']) {
    return input.replaceFirstMapped(_emailMaskRegExp, (m) {
      var start = m.group(1);
      var middle = fillChar * max(minFill, m.group(2)!.length);
      var end = m.groupCount >= 3 ? m.group(3) : start;
      return start! + middle + end!;
    });
  }
}