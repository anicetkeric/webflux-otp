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


  static String? passwordValidation(String value){
    if(value.isEmpty){
      return "Field Can't be empty.";
    }else if(value.length < 6) {
      return "Password must be of six characters long.";
    }
    return null;
  }

  static bool checkValidEmail(String value){
    if (RegExp(
        r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
        .hasMatch(value)) {
      return true;
    } else {
      return false;
    }
  }
}